package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
/*import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;*/
import com.squareup.picasso.Picasso;
import entity.Data;

public class MusicActivity extends AppCompatActivity {

    private Data track;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView info;
        ImageView image;

        setContentView(R.layout.activity_music);

        Intent intent = getIntent();

        if (intent.hasExtra("Data")) {

            if(Player.player.isPlaying()){
                Player.player.stop();
            }

            track = (Data) intent.getSerializableExtra("Data");

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
