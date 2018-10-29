package com.example.krist.hangman;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class KeyboardFragment extends Fragment {
    View view;
    String language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        SharedPreferences spf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        language = spf.getString("language", "english");
        if (language.equals("norwegian")) {
            view = inflater.inflate(R.layout.norwegian_keyboard_fragment, container, false);
        } else {
            view = inflater.inflate(R.layout.english_keyboard_fragment, container, false);
        }
        return view;
    }
}
