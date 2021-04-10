package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import entity.Data;

import java.io.IOException;

public class MusicActivity extends AppCompatActivity {

    private Data track;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView info;

        setContentView(R.layout.activity_music);

        Intent intent = getIntent();

        if (intent.hasExtra("Data")) {
            
            track = (Data) intent.getSerializableExtra("Data");

            if(Player.player.isPlaying()){
                Player.player.stop();
            }

            info = findViewById(R.id.tv_music_Name);
            info.setText(track.getTitle_short());
            info = findViewById(R.id.tv_music_Artist);
            info.setText(track.getArtist());

            Player.player = new MediaPlayer();
            Player.createPlayer();
            Player.startStreaming(getApplicationContext(), track.getPreview());
            Player.player.start();

        } else {
            track = null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Data", track);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }


}
