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

    MediaPlayer sound;
    SharedPreferences spf;
    boolean musicOn;
    String language;

    ImageView musicBtn;
    ImageView playBtn;
    ImageView noBtn;
    ImageView ukBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        musicBtn = findViewById(R.id.musicBtn);
        playBtn = findViewById(R.id.playBtn);
        noBtn = findViewById(R.id.noBtn);
        ukBtn = findViewById(R.id.ukBtn);


        spf = PreferenceManager.getDefaultSharedPreferences(this);
        language = spf.getString("language", "english");
        setLanguage(language);

        musicOn = spf.getBoolean("music", false);
        sound = MediaPlayer.create(this, R.raw.pingpongmusic);
        sound.setLooping(true);
        setMusicImage(musicOn);
        if (musicOn) sound.start();

        musicBtn.setOnClickListener(soundClick);
        noBtn.setOnClickListener(langClick);
        ukBtn.setOnClickListener(langClick);
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

    View.OnClickListener langClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.noBtn:
                    setLanguage("norwegian");
                    break;
                case R.id.ukBtn:
                    setLanguage("english");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * Sets the Music: on/off image
     * @param music boolean if music in on or off
     */
    void setMusicImage(boolean music) {
        if (music) {
            if (language.equals("norwegian")) musicBtn.setImageResource(R.drawable.musikkpaa);
            else musicBtn.setImageResource(R.drawable.musicon);
        } else {
            if (language.equals("norwegian")) musicBtn.setImageResource(R.drawable.musikkav);
            else musicBtn.setImageResource(R.drawable.musicoff);
        }
    }

    /**
     * Stores a given language in shared preferences and updates the UI accordingly
     * @param lang string to set language (norwegian/english)
     */
    void setLanguage(String lang) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = spf.edit();
        editor.putString("language", lang);
        language = lang;
        if (language.equals("norwegian")) {
            playBtn.setImageResource(R.drawable.spill);
            noBtn.setBackgroundColor(getResources().getColor(android.R.color.white));
            ukBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        } else if (language.equals("english")){
            playBtn.setImageResource(R.drawable.play);
            ukBtn.setBackgroundColor(getResources().getColor(android.R.color.white));
            noBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        setMusicImage(musicOn);
        editor.apply();
    }
}
