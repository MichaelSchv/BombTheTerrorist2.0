package com.example.hw2;

import android.app.Application;

import com.example.hw2.Utilities.SharedPreferencesManager;

public class App extends Application {

    public void onCreate() {
        super.onCreate();
        SharedPreferencesManager.init(this);
    }
}
