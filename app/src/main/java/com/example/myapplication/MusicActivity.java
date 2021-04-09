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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if(Player.play.isPlaying()){
            Player.play.stop();
        }

        TextView info;

        setContentView(R.layout.activity_music);

        Intent intent = getIntent();

        if (intent.hasExtra("Data")) {
            
            track = (Data) intent.getSerializableExtra("Data");

            info = findViewById(R.id.tv_music_Name);
            info.setText(track.getTitle_short());
            info = findViewById(R.id.tv_music_Artist);
            info.setText(track.getArtist());

            Player.startStreaming(getApplicationContext(), track.getPreview());

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
