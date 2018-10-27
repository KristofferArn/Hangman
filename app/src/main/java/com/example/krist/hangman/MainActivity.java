package com.example.krist.hangman;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer sound;
    private SharedPreferences spf;
    private boolean musicOn;

    private ImageView musicBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        musicBtn = findViewById(R.id.musicBtn);
        musicBtn.setOnClickListener(soundClick);

        spf = PreferenceManager.getDefaultSharedPreferences(this);
        sound = MediaPlayer.create(this, R.raw.pingpongmusic);
        sound.setLooping(true);
        musicOn = spf.getBoolean("music", false);
        setMusicImage(musicOn);
        if (musicOn) sound.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (musicOn) sound.pause();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (musicOn) sound.start();
    }

    View.OnClickListener soundClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = spf.edit();
            if (musicOn) {
                musicOn = false;
                editor.putBoolean("music", false);
                setMusicImage(musicOn);
                sound.pause();
            } else {
                musicOn = true;
                editor.putBoolean("music", true);
                setMusicImage(musicOn);
                sound.start();
            }
            editor.apply();
        }
    };

    void setMusicImage(boolean music) {
        if (music) {
            musicBtn.setImageResource(R.drawable.musicon);
        } else {
            musicBtn.setImageResource(R.drawable.musicoff);
        }
    }
}
