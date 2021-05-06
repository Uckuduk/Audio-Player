package com.example.myapplication;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

public class Player{

    public static MediaPlayer player;

    public static void createPlayer(){
        player.setAudioAttributes(
                new AudioAttributes
                        .Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build());

    }

    public static void startStreaming(Context context, String uri){

        try {
            player.setDataSource(context,
                    Uri.parse(uri));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void check(){

    }

}
