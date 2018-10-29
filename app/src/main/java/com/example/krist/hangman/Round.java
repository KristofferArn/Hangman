package com.example.krist.hangman;

public class Round {
    private String currentWord;
    private String guessedWord;
    private int roundNr;
    private int inncorrectNr;

    public Round(String currentWord, int roundNr) {
        this.currentWord = currentWord;
        this.roundNr = roundNr;
        this.inncorrectNr = 0;
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < currentWord.length(); i++) {
            s.append('_');
        }
        this.guessedWord = s.toString();
    }

    public boolean guess(char c) {
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

    public boolean checkWin() {
        return currentWord.equals(guessedWord);
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }

    public String getGuessedWord() {
        return guessedWord;
    }

    public void setGuessedWord(String guessedWord) {
        this.guessedWord = guessedWord;
    }

    public int getRoundNr() {
        return roundNr;
    }

    public void setRoundNr(int roundNr) {
        this.roundNr = roundNr;
    }

    public int getInncorrectNr() {
        return inncorrectNr;
    }

    public void setInncorrectNr(int inncorrectNr) {
        this.inncorrectNr = inncorrectNr;
    }
}
