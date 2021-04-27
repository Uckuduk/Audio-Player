package com.example.myapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import entity.*;
import java.io.IOException;
import java.io.Serializable;

import static NetworkUtils.NetworkUtils.generateSearchURL;
import static NetworkUtils.NetworkUtils.getResponseFromURL;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    Thread thread =null;
    final int REQUEST_CODE_RV_CLICK = 3;
    private Data songInfo = null;
    private EditText searchField;
    private RecyclerView searchMusicList;
    private PlayList searchPlayList;
    private SearchMusicAdapter searchMusicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchMusicList = findViewById(R.id.rv_searchSongs);
        searchField = findViewById(R.id.et_searchLine);

        Button play = findViewById(R.id.sa_b_playButton);
        Button search = findViewById(R.id.sa_b_search);
        LinearLayout thisSongLink = findViewById(R.id.l_searchSong);

        thisSongLink.setOnClickListener(this);
        search.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {

        if(thread != null) {
            thread.interrupt();
            Intent intent = new Intent();
            intent.putExtra("Data", songInfo);

            setResult(RESULT_OK, intent);
            finish();
        }

        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sa_b_search:

                if(!searchField.getText().toString().equals("")){
                    thread = new Thread(new DeezerSearchQuery());
                    thread.start();
                }
                break;

            case R.id.sa_b_playButton:
                if(Player.player.isPlaying())
                    Player.player.pause();
                else
                    Player.player.start();
                break;

            case R.id.l_searchSong:
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("Track", ThisTrack.track);
                startActivity(intent);
                break;
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

            for(int i = 0; i < resp.data.length; i++) {
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
        NowPlayingList.index = index;
        Intent activityIntent = new Intent(this, MusicActivity.class);
        activityIntent.putExtra("Data", track);
        startActivityForResult(activityIntent, 3);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            TextView info = findViewById(R.id.tv_searchSongName);
            LinearLayout lInfo = findViewById(R.id.l_searchSong);
            Button button = findViewById(R.id.sa_b_playButton);

            switch (requestCode) {
                case REQUEST_CODE_RV_CLICK:


                    if (data.getSerializableExtra("Data") == null || data==null) {
                        return;
                    } else {

                        lInfo.setVisibility(View.VISIBLE);
                        button.setVisibility(View.VISIBLE);
                        songInfo = (Data) data.getSerializableExtra("Data");
                        info.setText(songInfo.getTitle_short());
                        info = findViewById(R.id.tv_searchArtistName);
                        info.setText(songInfo.getArtist());
                    }

                    break;
                default:
                    break;
            }
        }
    }
}
