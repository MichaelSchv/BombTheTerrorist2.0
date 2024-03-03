package com.example.hw2.Utilities;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
public class SoundExecutor {
    private Context context;
    private Executor executor;
    private Handler handler;
    private MediaPlayer mediaPlayer;

    public SoundExecutor(Context context)
    {
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.handler = new Handler(Looper.getMainLooper());
    }
    public void playSound(int resId){
        executor.execute(() -> {
            mediaPlayer = MediaPlayer.create(context, resId);
            mediaPlayer.setLooping(false);
            mediaPlayer.setVolume(1.0f,1.0f);
            mediaPlayer.start();
        });
    }



    public void stopSound() {
        if (mediaPlayer != null) {
            executor.execute(() -> {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            });
        }
    }
}
