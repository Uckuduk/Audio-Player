package com.example.myapplication;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import entity.Track;

import java.io.Serializable;

public class MusicActivity extends AppCompatActivity {

    private Track track;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView info;

        setContentView(R.layout.activity_music);

        Intent intent = getIntent();

        if(intent.hasExtra("Track")){
            track = (Track) intent.getSerializableExtra("Track");
            info = findViewById(R.id.tv_music_Name);
            info.setText(track.getName());
            info = findViewById(R.id.tv_music_Artist);
            info.setText(track.getArtist());

        }
        else {
            track =null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("Track", track);
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}
