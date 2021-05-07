package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import entity.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import static NetworkUtils.NetworkUtils.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String PATH = "FavouriteTracksID.txt";
    final int REQUEST_CODE_RV_CLICK = 1;
    final int REQUEST_CODE_SEARCH = 2;

    Thread thread = null;
    Data songInfo;
    ImageButton search, playButton;
    LinearLayout thisSongLink;
    RecyclerView musicList;
    MusicAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FavouriteTracks.favouriteIds = new ArrayList<>();
        FavouriteTracks.tracks = new PlayList();

        readFavouriteFile();

        Player.player = new MediaPlayer();

        musicList = findViewById(R.id.rv_mySongs);

        thisSongLink = findViewById(R.id.l_song);
        search = findViewById(R.id.b_searchButton);
        playButton = findViewById(R.id.b_playButton);
        playButton.setOnClickListener(this);
        search.setOnClickListener(this);
        thisSongLink.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicList.getRecycledViewPool().clear();

        thread = new Thread(new DeezerFavouriteQuery());
        thread.start();
    }

    public void readFavouriteFile(){

        FileInputStream in = null;

        try {
            in = openFileInput(PATH);

            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            String text = new String(bytes);
            String[] IDs = text.split("\n");

            for (String id: IDs) {
                try {
                    FavouriteTracks.favouriteIds.add(Integer.parseInt(id));

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_song:
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("Track", ThisTrack.track);
                startActivityForResult(intent, REQUEST_CODE_RV_CLICK);
                break;

            case R.id.b_playButton:
                if(Player.player.isPlaying()) {
                    Player.player.pause();
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }else {
                    Player.player.start();
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }
                break;

            case R.id.b_searchButton:
                Intent activityIntent = new Intent(this, SearchActivity.class);

                startActivityForResult(activityIntent, REQUEST_CODE_SEARCH);
                break;

            default:
                break;
        }
    }

    public void recyclerClick(int index, Data track) {
        LinearLayout thisSongLink = findViewById(R.id.l_song);
        thisSongLink.setClickable(true);
        ImageButton button = findViewById(R.id.b_playButton);
        button.setClickable(true);

        NowPlayingList.playList = FavouriteTracks.tracks.reverse();
        NowPlayingList.index = index;
        Intent activityIntent = new Intent(this, MusicActivity.class);
        activityIntent.putExtra("Data", track);
        startActivityForResult(activityIntent, REQUEST_CODE_RV_CLICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            TextView info = findViewById(R.id.tv_songName);
            LinearLayout lInfo = findViewById(R.id.l_song);
            ImageButton button = findViewById(R.id.b_playButton);

            assert data != null;
            if (!(data.getSerializableExtra("Data") == null)) {
                if(Player.player.isPlaying()) {
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }else {
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }

                button.setVisibility(View.VISIBLE);
                lInfo.setVisibility(View.VISIBLE);
                songInfo = (Data) data.getSerializableExtra("Data");
                info.setText(songInfo.getTitle_short());
                info = findViewById(R.id.tv_artistName);
                info.setText(songInfo.getArtist());
            }
        }
    }

    class DeezerFavouriteQuery implements Runnable {

        @Override
        public void run() {

            Gson gson = new Gson();
            Data searchTrack;

            ArrayList<URL> responses = null;

            try {
                responses = findTrackById(FavouriteTracks.favouriteIds);
                for (URL url: responses) {
                   String response = getResponseFromURL(url);
                   searchTrack = gson.fromJson(response, Data.class);
                   if(!FavouriteTracks.tracks.contains(searchTrack)) {
                       FavouriteTracks.tracks.appendSong(searchTrack);
                       searchTrack.setFavourite(true);
                   }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            musicList.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getParent());
                    musicList.setLayoutManager(layoutManager);

                    musicList.setHasFixedSize(true);
                    PlayList playList = FavouriteTracks.tracks.reverse();
                    musicAdapter = new MusicAdapter(FavouriteTracks.tracks.count(), getParent(), playList);
                    musicList.setAdapter(musicAdapter);
                }
            });
        }
    }
}