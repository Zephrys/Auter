package com.zephrys.auter;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //enable local data Storage
        Parse.enableLocalDatastore(this);

        // Initialize global stuff for Yourney
        Parse.initialize(this, "String 1", "String 2"); // Your Application ID and Client Key are defined elsewhere
    }
}

