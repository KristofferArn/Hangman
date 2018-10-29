package com.example.krist.hangman;

public class Round {
    private String currentWord;
    private String guessedWord;
    private int roundNr;
    private int inncorrectNr;

    Round(String currentWord, int roundNr) {
        this.currentWord = currentWord;
        this.roundNr = roundNr;
        this.inncorrectNr = 0;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentWord.length(); i++) {
            if (currentWord.charAt(i) == ' ') {
                s.append(' ');
            } else {
                s.append('_');
            }
        }
        this.guessedWord = s.toString();
    }

    boolean guess(char c) {
        if (currentWord.indexOf(c) >= 0) {
            StringBuilder sb = new StringBuilder(guessedWord);
            for (int index = currentWord.indexOf(c);
                 index >= 0;
                 index = currentWord.indexOf(c, index + 1)) {
                sb.setCharAt(index, c);
            }
            guessedWord = sb.toString();
            return true;
        } else {
            inncorrectNr++;
            return false;
        }
    }

    boolean checkWin() {
        return currentWord.equals(guessedWord);
    }

    String getCurrentWord() {
        return currentWord;
    }

    void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    String getGuessedWord() {
        return guessedWord;
    }

    void setGuessedWord(String guessedWord) {
        this.guessedWord = guessedWord;
    }

    int getRoundNr() {
        return roundNr;
    }

    void setRoundNr(int roundNr) {
        this.roundNr = roundNr;
    }

    int getInncorrectNr() {
        return inncorrectNr;
    }

    void setInncorrectNr(int inncorrectNr) {
        this.inncorrectNr = inncorrectNr;
    }
}
