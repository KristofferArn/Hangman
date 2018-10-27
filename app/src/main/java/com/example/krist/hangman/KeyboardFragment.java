package com.example.krist.hangman;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    void onClick(View v){
        Button btn = view.findViewById(v.getId());
        Toast.makeText(getActivity(), btn.getText(), Toast.LENGTH_SHORT).show();
    }
}
