package com.example.myapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class Player {

    static MediaPlayer play = new MediaPlayer();

    public static void startStreaming(Context context, String uri){

        play.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());

        try {
            play.setDataSource(context,
                    Uri.parse(uri));
            play.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        play.start();
    }

}
