package com.example.myapplication;

import NetworkUtils.NetworkUtils;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
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
import java.nio.Buffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import static NetworkUtils.NetworkUtils.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    final String PATH = "FavouriteTracksIDs.txt";
    final int REQUEST_CODE_RV_CLICK = 1;
    final int REQUEST_CODE_SEARCH = 2;

    Thread thread = null;
    Data songInfo;
    Button search, play;
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
        play = findViewById(R.id.b_playButton);
        play.setOnClickListener(this);
        search.setOnClickListener(this);
        thisSongLink.setOnClickListener(this);

        thread = new Thread(new DeezerFavouriteQuery());
        thread.start();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        super.onBackPressed();
        saveFavouriteFile();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_song:
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("Track", ThisTrack.track);
                startActivity(intent);
                break;

            case R.id.b_playButton:
                if(Player.player.isPlaying())
                    Player.player.pause();
                else
                    Player.player.start();
                break;

            case R.id.b_searchButton:
                Intent activityIntent = new Intent(this, SearchActivity.class);

                startActivityForResult(activityIntent, REQUEST_CODE_SEARCH);
                break;

            default:
                break;
        }
    }

    public void recyclerClick(View holder, Data track) {
        LinearLayout thisSongLink = findViewById(R.id.l_song);
        thisSongLink.setClickable(true);
        Button button = findViewById(R.id.b_playButton);
        button.setClickable(true);

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
            Button button = findViewById(R.id.b_playButton);
            switch(requestCode) {

                case REQUEST_CODE_RV_CLICK:

                    if (data.getSerializableExtra("Data") == null || data==null) {
                        return;
                    } else {
                        button.setVisibility(View.VISIBLE);
                        lInfo.setVisibility(View.VISIBLE);
                        songInfo = (Data) data.getSerializableExtra("Data");
                        info.setText(songInfo.getTitle_short());
                        info = findViewById(R.id.tv_artistName);
                        info.setText(songInfo.getArtist());
                    }
                    break;

                case REQUEST_CODE_SEARCH:
                    if (data.getSerializableExtra("Data") == null || data==null) {
                        return;
                    } else {
                        button.setVisibility(View.VISIBLE);
                        lInfo.setVisibility(View.VISIBLE);
                        info = findViewById(R.id.tv_songName);
                        songInfo = (Data) data.getSerializableExtra("Data");
                        info.setText(songInfo.getTitle_short());
                        info = findViewById(R.id.tv_artistName);info.setText(songInfo.getArtist());
                    }

                    break;
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
                   FavouriteTracks.tracks.appendSong(searchTrack);
                   searchTrack.setFavourite(true);
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
                    musicAdapter = new MusicAdapter(FavouriteTracks.tracks.count(), getParent(), FavouriteTracks.tracks);
                    musicList.setAdapter(musicAdapter);
                }
            });
        }
    }
}