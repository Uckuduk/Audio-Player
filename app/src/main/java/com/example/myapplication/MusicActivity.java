package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import entity.Data;
import entity.FavouriteTracks;
import entity.NowPlayingList;
import entity.ThisTrack;

import java.io.FileOutputStream;
import java.io.IOException;

public class MusicActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    final String PATH = "FavouriteTracksID.txt";
    private Data track;
    private MediaPlayer player;
    private String lastSong = null;
    private ImageButton playButton, favouriteButton, nextButton, previousButton;
    private SeekBar seekBar;
    private Thread thread = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        previousButton = findViewById(R.id.ma_b_previous);
        nextButton = findViewById(R.id.ma_b_next);
        playButton = findViewById(R.id.ma_b_play_pause);
        favouriteButton = findViewById(R.id.ma_b_favourite);
        seekBar = findViewById(R.id.ma_seekBar);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        favouriteButton.setOnClickListener(this);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) Player.player.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Intent intent = getIntent();

        if (intent.hasExtra("Data")) {
            track = (Data) intent.getSerializableExtra("Data");
            Data track1 = ThisTrack.track;

            play();

            setInfo();

        } else {
            track = null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void saveFavouriteFile() {

        FileOutputStream out = null;

        try {
            out = openFileOutput(PATH, MODE_PRIVATE);
            for (int id : FavouriteTracks.favouriteIds) {
                String txt = String.valueOf(id);
                out.write(txt.getBytes());
                out.write("\n".getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setInfo() {
        TextView info;
        ImageView image;

        info = findViewById(R.id.tv_music_Name);
        info.setText(track.getTitle_short());
        info = findViewById(R.id.tv_music_Artist);
        info.setText(track.getArtist());

        seekBar.setProgress(Player.player.getCurrentPosition());
        seekBar.setMax(Player.player.getDuration());

        thread = new Thread(new SeekBarAct());
        thread.start();

        if (Player.player.isPlaying()) {
            playButton.setImageResource(R.drawable.ic_baseline_pause_24);
        } else {
            playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }

        if (track.isFavourite()) {
            favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_green_24);
        } else {
            favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        }

        String str = track.getPicture();
        Uri uri = Uri.parse(str);
        image = findViewById(R.id.im_songImage);

        Picasso.with(this)
                .load(uri)
                .into(image);

    }

    private void play() {
        if (ThisTrack.track == null) {
            ThisTrack.track = track;
            Player.player.stop();
            Player.player = new MediaPlayer();
            Player.createPlayer();
            Player.startStreaming(getApplicationContext(), track.getPreview());
            Player.player.start();

        } else if ((track.getId() != ThisTrack.track.getId())) {
            ThisTrack.track = track;
            Player.player.stop();
            Player.player = new MediaPlayer();
            Player.createPlayer();
            Player.startStreaming(getApplicationContext(), track.getPreview());
            Player.player.start();
        }

        Player.player.setOnCompletionListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Data", track);

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ma_b_play_pause:
                if (Player.player.isPlaying()) {
                    Player.player.pause();
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    Player.player.start();
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }
                break;

            case R.id.ma_b_favourite:
                if (!track.isFavourite()) {
                    track.setFavourite(true);
                    FavouriteTracks.addFavourite(track);
                    favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_green_24);
                } else {
                    if (FavouriteTracks.favouriteIds.contains(track.getId())) {
                        track.setFavourite(false);
                        FavouriteTracks.deleteFavourite(track);
                        favouriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }
                }

                saveFavouriteFile();

                break;

            case R.id.ma_b_next:
                if (NowPlayingList.playList.count() != 0) {
                    if (NowPlayingList.thisIndex == NowPlayingList.playList.count() - 1) {
                        track = NowPlayingList.playList.get(0);
                        NowPlayingList.thisIndex = 0;
                    } else {
                        NowPlayingList.thisIndex += 1;
                        track = NowPlayingList.playList.get(NowPlayingList.thisIndex);
                    }

                    play();
                    setInfo();
                }
                break;
            case R.id.ma_b_previous:
                if (NowPlayingList.playList.count() != 0) {
                    if (NowPlayingList.thisIndex == 0) {
                        NowPlayingList.thisIndex = NowPlayingList.playList.count() - 1;
                        track = NowPlayingList.playList.get(NowPlayingList.thisIndex);
                    } else {
                        NowPlayingList.thisIndex -= 1;
                        track = NowPlayingList.playList.get(NowPlayingList.thisIndex);
                    }

                    play();
                    setInfo();

                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (NowPlayingList.playList.count() != 0) {
            if (NowPlayingList.thisIndex == NowPlayingList.playList.count() - 1) {
                track = NowPlayingList.playList.get(0);
                NowPlayingList.thisIndex = 0;
            } else {
                NowPlayingList.thisIndex += 1;
                track = NowPlayingList.playList.get(NowPlayingList.thisIndex);
            }

            play();
            setInfo();

        }
    }

    class SeekBarAct implements Runnable {

        @Override
        public void run() {
            int currentPosition = 0;
            int total = Player.player.getDuration();

            while (Player.player != null && currentPosition < total) {
                try {
                    Thread.sleep(1000);
                    currentPosition = Player.player.getCurrentPosition();
                } catch (InterruptedException e) {
                    return;
                } catch (Exception e) {
                    return;
                }
                seekBar.setProgress(currentPosition);
            }
        }
    }
}
