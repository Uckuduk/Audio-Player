package com.example.myapplication;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import entity.Data;
import entity.PlayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public PlayList playList = new PlayList();

    final int REQUEST_CODE_RV_CLICK = 1;
    final int REQUEST_CODE_SEARCH = 2;

    Data songInfo;
    Button search, play;

    class AUTH implements Runnable{

        @Override
        public void run() {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView musicList = findViewById(R.id.rv_mySongs);

        search = findViewById(R.id.b_searchButton);
        play = findViewById(R.id.b_playButton);
        play.setOnClickListener(this);
        search.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        musicList.setLayoutManager(layoutManager);

        musicList.setHasFixedSize(true);

        MusicAdapter musicAdapter = new MusicAdapter(playList.count(), this, playList);

        musicList.setAdapter(musicAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.l_song:

                break;

            case R.id.b_playButton:
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
            switch(requestCode) {

                case REQUEST_CODE_RV_CLICK:

                    if (data == null) {
                        return;
                    } else {
                        lInfo.setVisibility(View.VISIBLE);
                        songInfo = (Data) data.getSerializableExtra("Data");
                        info.setText(songInfo.getTitle_short());
                        info = findViewById(R.id.tv_searchArtistName);
                        info.setText(songInfo.getArtist());
                    }
                    break;

                case REQUEST_CODE_SEARCH:
                    if (data == null) {
                        return;
                    } else {

                        lInfo.setVisibility(View.VISIBLE);
                        info = findViewById(R.id.tv_songName);
                        songInfo = (Data) data.getSerializableExtra("Data");
                        info.setText(songInfo.getTitle_short());
                        info = findViewById(R.id.tv_artistName);
                        info.setText(songInfo.getArtist());
                    }
                    break;
            }
        }
    }
}