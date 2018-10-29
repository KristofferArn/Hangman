package com.example.krist.hangman;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity{

    String language;
    boolean musicOn;
    SharedPreferences spf;
    MediaPlayer sound;

    ArrayList<String> words = new ArrayList<>();
    ArrayList<View> usedBtns = new ArrayList<>();
    ArrayList<Integer> hangmanDraw = new ArrayList<>();

    Round currentRound;
    int roundNr = 0;
    Random random = new Random();

    TextView wordView;
    ImageView hangmanImage;
    KeyboardFragment keyboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        spf = PreferenceManager.getDefaultSharedPreferences(this);
        language = spf.getString("language", "english");

        musicOn = spf.getBoolean("music", false);
        sound = MediaPlayer.create(this, R.raw.animalmagic);
        sound.setLooping(true);
        if (musicOn) sound.start();

        keyboardFragment = (KeyboardFragment) getSupportFragmentManager().findFragmentById(R.id.keyboardFragment);
        readFile();

        wordView = findViewById(R.id.word_view);
        hangmanImage = findViewById(R.id.hangman_image);
        fillHangmanDraw();
        newRound();
    }

    public void onClick(View view) {
        view.setOnClickListener(null);
        view.getBackground().setAlpha(0);
        ((Button)view).setTextColor(Color.parseColor("#FFFFFF"));
        usedBtns.add(view);

        String idName = getResources().getResourceEntryName(view.getId());
        char c = idName.charAt(0);
        if (currentRound.guess(c)) {
            wordView.setText(formatWord(currentRound.getGuessedWord()));
            if (currentRound.checkWin()) {
                Toast.makeText(this, "Congrats", Toast.LENGTH_SHORT).show();
                newRound();
            }
        } else {
            hangmanImage.setImageResource(hangmanDraw.get(currentRound.getInncorrectNr() - 1));
            if (currentRound.getInncorrectNr() == 8) {
                Toast.makeText(this, "Sorry", Toast.LENGTH_SHORT).show();
                newRound();
            }
        }
    }

    void readFile() {
        BufferedReader bufferedReader;
        String fileName;
        if (language.equals("norwegian")) fileName = "no_words.txt";
        else fileName = "uk_words.txt";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                words.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void newRound(){
        int wordIndex = random.nextInt(words.size());
        currentRound = new Round(words.get(wordIndex), roundNr++);
        words.remove(wordIndex);
        wordView.setText(formatWord(currentRound.getGuessedWord()));
        resetBtns();
        hangmanImage.setImageDrawable(null);
    }

    String formatWord (String s){
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < s.length(); i++){
            if (i != s.length()-1) {
                res.append(s.charAt(i)).append(" ");
            } else {
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }

    void resetBtns() {
        for (View usedBtn : usedBtns) {
            ((Button)usedBtn).setTextColor(Color.parseColor("#2f4a59"));
            usedBtn.getBackground().setAlpha(255);
            usedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GameActivity.this.onClick(v);
                }
            });
        }
        usedBtns.clear();
    }

    void fillHangmanDraw() {
        hangmanDraw.add(R.drawable.human00);
        hangmanDraw.add(R.drawable.human01);
        hangmanDraw.add(R.drawable.human02);
        hangmanDraw.add(R.drawable.human03);
        hangmanDraw.add(R.drawable.human04);
        hangmanDraw.add(R.drawable.human05);
        hangmanDraw.add(R.drawable.human06);
        hangmanDraw.add(R.drawable.human07);
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
}
