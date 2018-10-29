package com.example.krist.hangman;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        sound = MediaPlayer.create(this, R.raw.animalmagic);
        sound.setLooping(true);
        setMusicImage(musicOn);
        if (musicOn) sound.start();

        musicBtn.setOnClickListener(soundClick);
        noBtn.setOnClickListener(langClick);
        ukBtn.setOnClickListener(langClick);

        loopScaleAnimation(findViewById(R.id.titletext));
//        setToolbar();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });

        ImageView menu_btn = findViewById(R.id.menu_btn);
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
            SharedPreferences.Editor editor = spf.edit();
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
        SharedPreferences.Editor editor = spf.edit();
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

    /**
     * Loops a scaling animation on a view
     * @param v view to be animated
     */
    void loopScaleAnimation(View v) {
        ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                v,
                PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                PropertyValuesHolder.ofFloat("scaleY", 1f));
        scaleDown.setDuration(1500);

        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

        scaleDown.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
