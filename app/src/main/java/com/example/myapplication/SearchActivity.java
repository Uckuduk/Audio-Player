package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import entity.*;

import java.io.IOException;


import static NetworkUtils.NetworkUtils.generateSearchURL;
import static NetworkUtils.NetworkUtils.getResponseFromURL;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener {
    Thread thread = null;
    private Data songInfo = null;
    private EditText searchField;
    private RecyclerView searchMusicList;
    private PlayList searchPlayList;
    private SearchMusicAdapter searchMusicAdapter;
    private ImageButton playButton, searchButton;
    private LinearLayout thisSongLink;
    private boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchMusicList = findViewById(R.id.rv_searchSongs);
        searchField = findViewById(R.id.et_searchLine);

        playButton = findViewById(R.id.sa_b_playButton);
        searchButton = findViewById(R.id.sa_b_search);
        thisSongLink = findViewById(R.id.l_searchSong);

        Player.player = new MediaPlayer();
        ThisTrack.track = null;

        thisSongLink.setOnClickListener(this);
        searchField.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        playButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Player.player.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (NowPlayingList.playList.count() != 0 && active) {
            if (NowPlayingList.thisIndex == NowPlayingList.playList.count() - 1) {
                ThisTrack.track = NowPlayingList.playList.get(0);
                NowPlayingList.thisIndex = 0;
            } else {
                NowPlayingList.thisIndex += 1;
                ThisTrack.track = NowPlayingList.playList.get(NowPlayingList.thisIndex);
            }

            Player.player.stop();
            Player.player = new MediaPlayer();
            Player.createPlayer();
            Player.startStreaming(getApplicationContext(), ThisTrack.track.getPreview());
            Player.player.start();
            Player.player.setOnCompletionListener(this);
        }

        active = true;

        TextView info = findViewById(R.id.tv_searchSongName);
        info.setText(ThisTrack.track.getTitle_short());
        info = findViewById(R.id.tv_searchArtistName);
        info.setText(ThisTrack.track.getArtist());

    }

    @Override
    public void onBackPressed() {
        if(songInfo != null && !playButton.isShown()){
            playButton.setVisibility(View.VISIBLE);
            thisSongLink.setVisibility(View.VISIBLE);

        }else {

            if (thread != null) {
                thread.interrupt();
                Intent intent = new Intent();
                intent.putExtra("Data", songInfo);

                setResult(RESULT_OK, intent);
                finish();
            }


            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_b_search:

                if (!searchField.getText().toString().equals("")) {
                    thread = new Thread(new DeezerSearchQuery());
                    thread.start();
                }
                break;

            case R.id.sa_b_playButton:
                if (Player.player.isPlaying()) {
                    Player.player.pause();
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                } else {
                    Player.player.start();
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }
                break;

            case R.id.l_searchSong:
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("Data", ThisTrack.track);
                startActivityForResult(intent, 1);
                break;

            case R.id.et_searchLine:
                thisSongLink.setVisibility(View.INVISIBLE);
                playButton.setVisibility(View.INVISIBLE);
        }


    }

    class DeezerSearchQuery implements Runnable {

        @Override
        public void run() {

            searchPlayList = new PlayList();

            Gson gson = new Gson();
            Data searchTrack;

            String response = null;

            try {
                response = getResponseFromURL(generateSearchURL(searchField.getText().toString()));

            } catch (IOException e) {
                e.printStackTrace();
            }

            Response resp = gson.fromJson(response, Response.class);

            for (int i = 0; i < resp.data.length; i++) {
                searchTrack = resp.data[i];
                searchPlayList.appendSong(searchTrack);
            }

            searchMusicList.post(new Runnable() {
                @Override
                public void run() {
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getParent());
                    searchMusicList.setLayoutManager(layoutManager);

                    searchMusicList.setHasFixedSize(true);

                    searchMusicAdapter = new SearchMusicAdapter(searchPlayList.count(), getParent(), searchPlayList);

                    searchMusicList.setAdapter(searchMusicAdapter);
                }
            });
        }
    }

    public void recyclerClick(int index, Data track) {
        NowPlayingList.playList = searchPlayList;
        NowPlayingList.thisIndex = index;

        Intent activityIntent = new Intent(this, MusicActivity.class);
        activityIntent.putExtra("Data", track);
        startActivityForResult(activityIntent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            TextView info = findViewById(R.id.tv_searchSongName);

            assert data != null;
            if (data.getSerializableExtra("Data") != null) {
                if(Player.player.isPlaying()) {
                    playButton.setImageResource(R.drawable.ic_baseline_pause_24);
                }else {
                    playButton.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                }

                thisSongLink.setVisibility(View.VISIBLE);
                playButton.setVisibility(View.VISIBLE);
                songInfo = (Data) data.getSerializableExtra("Data");
                info.setText(songInfo.getTitle_short());
                info = findViewById(R.id.tv_searchArtistName);
                info.setText(songInfo.getArtist());
            }
        }
    }
}
