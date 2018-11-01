package com.example.krist.hangman;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    String language;
    boolean musicOn;
    SharedPreferences spf;
    MediaPlayer sound;

    ArrayList<String> words = new ArrayList<>();
    ArrayList<View> usedBtns = new ArrayList<>(); //Used to restore buttons when new round starts
    ArrayList<Integer> hangmanDraw = new ArrayList<>();

    Round currentRound;
    Random random = new Random();

    TextView wordView;
    ImageView hangmanImage;
    KeyboardFragment keyboardFragment;
    ImageButton menuBtn;

    //Popup after end of round
    AlertDialog popUpDialog;
    View popupView;
    Button nextWordBtn;
    Button homeBtn;
    TextView popupTitle;
    TextView solutionText;

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
        menuBtn = findViewById(R.id.menu_btn);

        fillHangmanDraw();
        setupPopup();
        newRound();
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

    /**
     * Method to be called when user clicks a button on the custom keyboard fragment.
     * @param view to be clicked
     */
    public void onClick(View view) {
        view.setOnClickListener(null);
        view.getBackground().setAlpha(0);
        ((Button) view).setTextColor(Color.parseColor("#FFFFFF"));
        usedBtns.add(view);

        String idName = getResources().getResourceEntryName(view.getId());
        char c = idName.charAt(0);
        if (currentRound.guess(c)) {
            wordView.setText(formatWord(currentRound.getGuessedWord()));
            if (currentRound.checkWin()) {
                showPopup(true, currentRound.getCurrentWord());
            }
        } else {
//            hangmanImage.setImageResource(hangmanDraw.get(currentRound.getInncorrectNr() - 1));
            if (currentRound.getInncorrectNr() == 8) {
                showPopup(false, currentRound.getCurrentWord());
            }
        }
    }

    /**
     * Reads the words from file and puts them in the word array.
     */
    void readFile() {
        BufferedReader bufferedReader;
        String fileName;
        if (language.equals("norwegian")) fileName = "no_words.txt";
        else fileName = "uk_words.txt";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(getAssets().open(fileName), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                words.add(line.toLowerCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a new round
     */
    void newRound() {
        int wordIndex = random.nextInt(words.size());
        currentRound = new Round(words.get(wordIndex));
        words.remove(wordIndex);
        wordView.setText(formatWord(currentRound.getGuessedWord()));
        resetBtns();
        hangmanImage.setImageDrawable(null);
    }

    /**
     * Formats a string to letter-space-letter-space... and so on.
     * @param s string to be formatted
     * @return formatted string
     */
    String formatWord(String s) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i != s.length() - 1) {
                if (s.charAt(i) == ' ') {
                    res.append("   ");
                } else {
                    res.append(s.charAt(i)).append(" ");
                }
            } else {
                res.append(s.charAt(i));
            }
        }
        return res.toString();
    }

    /**
     * Takes the buttons in "usedBtns" and revert them to their original state.
     */
    void resetBtns() {
        for (View usedBtn : usedBtns) {
            ((Button) usedBtn).setTextColor(Color.parseColor("#2f4a59"));
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

    /**
     * Sets up popup screen for after round.
     */
    void setupPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        popupView = getLayoutInflater().inflate(R.layout.end_of_game_popup, null);
        builder.setView(popupView);
        popUpDialog = builder.create();
        popUpDialog.setCanceledOnTouchOutside(false);

        popupTitle = popupView.findViewById(R.id.popup_title);
        solutionText = popupView.findViewById(R.id.solution);
        nextWordBtn = popupView.findViewById(R.id.new_word_btn);
        homeBtn = popupView.findViewById(R.id.home_btn);
        nextWordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound();
                popUpDialog.cancel();
            }
        });
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpDialog.cancel();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    /**
     * Shows the popup for end of round. Gives option to play another word or go back to homescreen.
     * @param win true if the user guess the word, else false.
     * @param correctWord the correct word for the round.
     */
    void showPopup(boolean win, String correctWord) {
        if (language.equals("norwegian")) {
            if (win) {
                popupTitle.setText(R.string.no_good_job);
            } else {
                popupTitle.setText(R.string.no_sorry);
            }
            solutionText.setText(getString(R.string.no_show_word, correctWord.toUpperCase()));
            nextWordBtn.setText(R.string.no_new_word);
            homeBtn.setText(R.string.no_home);
        } else if (language.equals("english")) {
            if (win) {
                popupTitle.setText(R.string.uk_good_job);
            } else {
                popupTitle.setText(R.string.uk_sorry);
            }
            solutionText.setText(getString(R.string.uk_show_word, correctWord.toUpperCase()));
            nextWordBtn.setText(R.string.uk_new_word);
            homeBtn.setText(R.string.uk_home);
        }
        popUpDialog.show();
    }

    /**
     * Fills an array with image ids for the hangman.
     */
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
}
