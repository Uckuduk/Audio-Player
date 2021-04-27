package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class MusicActivity extends AppCompatActivity implements View.OnClickListener {
    final String PATH = "FavouriteTracksID.txt";
    private Data track;
    private MediaPlayer player;
    private String lastSong = null;
    private ImageButton playButton, favouriteButton, nextButton, previousButton;
    private Boolean favourite = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        previousButton = findViewById(R.id.ma_b_previous);
        nextButton = findViewById(R.id.ma_b_next);
        playButton = findViewById(R.id.ma_b_play_pause);
        favouriteButton = findViewById(R.id.ma_b_favourite);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        favouriteButton.setOnClickListener(this);

        Intent intent = getIntent();

        if(intent.hasExtra("Track")){
            track = (Data) intent.getSerializableExtra("Track");
            setInfo();
        }else
            if (intent.hasExtra("Data")) {

            if(Player.player.isPlaying()){
                Player.player.stop();
            }

            track = (Data) intent.getSerializableExtra("Data");

            setInfo();

            play();

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

    private void setInfo(){
        TextView info;
        ImageView image;

        ThisTrack.track = track;
        info = findViewById(R.id.tv_music_Name);
        info.setText(track.getTitle_short());
        info = findViewById(R.id.tv_music_Artist);
        info.setText(track.getArtist());

        String str = track.getPicture();
        Uri uri = Uri.parse(str);
        image = findViewById(R.id.im_songImage);

        Picasso.with(this)
                .load(uri)
                .into(image);

    }

    private void play(){
        Player.player.stop();
        Player.player = new MediaPlayer();
        Player.createPlayer();
        Player.startStreaming(getApplicationContext(), track.getPreview());
        Player.player.start();}

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra("Data", track);

        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.ma_b_play_pause:
                if(Player.player.isPlaying())
                    Player.player.pause();
                else
                    Player.player.start();
                break;

            case R.id.ma_b_favourite:
                if(!track.isFavourite()){
                    track.setFavourite(true);
                    FavouriteTracks.addFavourite(track);

                }
                else {
                    if(FavouriteTracks.favouriteIds.contains(track.getId())){
                        track.setFavourite(false);
                        FavouriteTracks.deleteFavourite(track);
                    }
                }

                saveFavouriteFile();

                break;

            case R.id.ma_b_next:
                if(NowPlayingList.playList.count() != 0) {
                    if (NowPlayingList.index == NowPlayingList.playList.count() - 1) {
                        track = NowPlayingList.playList.get(0);
                        NowPlayingList.index = 0;
                    } else {
                        NowPlayingList.index += 1;
                        track = NowPlayingList.playList.get(NowPlayingList.index);
                    }

                    setInfo();
                    play();

                }
                break;
            case R.id.ma_b_previous:
                if(NowPlayingList.playList.count() != 0) {
                    if (NowPlayingList.index == 0) {
                        NowPlayingList.index = NowPlayingList.playList.count() - 1;
                        track = NowPlayingList.playList.get(NowPlayingList.index);
                    } else {
                        NowPlayingList.index -= 1;
                        track = NowPlayingList.playList.get(NowPlayingList.index);
                    }

                    setInfo();
                    play();
                }
                break;

            default:
                break;
        }
    }
}
