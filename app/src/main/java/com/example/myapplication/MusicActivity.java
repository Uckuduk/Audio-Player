package com.example.myapplication;

import android.content.Intent;
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

        TextView info;

        setContentView(R.layout.activity_music);

        Intent intent = getIntent();

        if (intent.hasExtra("Data")) {
            
            track = (Data) intent.getSerializableExtra("Data");

            info = findViewById(R.id.tv_music_Name);
            info.setText(track.getTitle_short());
            info = findViewById(R.id.tv_music_Artist);
            info.setText(track.getArtist());

            /*MediaPlayer play = new MediaPlayer();

            Uri uri = Uri.parse(track.getPreview());

            try {
                play.setDataSource(getApplicationContext(),
                        Uri.parse("https://cdns-preview-c.dzcdn.net//stream//c-cca63b2c92773d54e61c5b4d17695bd2-8.mp3"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            play.prepareAsync();

            play.setVolume(1,1);
            play.start();*/

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
