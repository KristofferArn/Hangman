package com.example.krist.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    MediaPlayer sound;
    SharedPreferences spf;
    boolean musicOn;
    String language;

    ImageView musicBtn;
    ImageView playBtn;
    ImageView noBtn;
    ImageView ukBtn;
    ImageButton menuBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        musicBtn = findViewById(R.id.musicBtn);
        playBtn = findViewById(R.id.playBtn);
        noBtn = findViewById(R.id.noBtn);
        ukBtn = findViewById(R.id.ukBtn);
        menuBtn = findViewById(R.id.menu_btn);


        spf = PreferenceManager.getDefaultSharedPreferences(this);
        language = spf.getString("language", "english");
        setLanguage(language);

        musicOn = spf.getBoolean("music", false);
        sound = MediaPlayer.create(this, R.raw.swayingdaisies);
        sound.setLooping(true);
        setMusicImage(musicOn);
        if (musicOn) sound.start();

        musicBtn.setOnClickListener(soundClick);
        noBtn.setOnClickListener(langClick);
        ukBtn.setOnClickListener(langClick);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
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
            noBtn.setAlpha(1f);
            ukBtn.setAlpha(0.5f);
        } else if (language.equals("english")){
            playBtn.setImageResource(R.drawable.play);
            ukBtn.setAlpha(1f);
            noBtn.setAlpha(0.5f);
        }
        setMusicImage(musicOn);
        editor.apply();
    }

    /**
     * Shows menu with Help and Quit when user clicks view.
     * @param view view to be clicked.
     */
    public void showMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.game_menu, popupMenu.getMenu());
        if (language.equals("norwegian")) {
            popupMenu.getMenu().findItem(R.id.menu_quit).setTitle(R.string.no_quit);
            popupMenu.getMenu().findItem(R.id.menu_help).setTitle(R.string.no_help);
        }
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_help:
                        helpDialog();
                        break;
                    case R.id.menu_quit:
                        finishAffinity();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
    }

    /**
     * Shows a AlertDialog with help information about the game.
     */
    void helpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (language.equals("norwegian")) {
            builder.setMessage(R.string.no_game_description);
        } else if (language.equals("english")) {
            builder.setMessage(R.string.uk_game_description);
        }
        builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
