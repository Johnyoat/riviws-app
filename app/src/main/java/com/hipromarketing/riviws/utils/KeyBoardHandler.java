package com.hipromarketing.riviws.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

public class KeyBoardHandler {
    private AppCompatActivity activity;

    public static KeyBoardHandler getInstance(AppCompatActivity activity) {
        return new KeyBoardHandler(activity);
    }


    public KeyBoardHandler(AppCompatActivity activity) {
        this.activity = activity;
    }


    public static void hideKeyboard(Context context, View view) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }
}
