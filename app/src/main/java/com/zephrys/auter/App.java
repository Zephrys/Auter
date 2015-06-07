package com.zephrys.auter;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //enable local data Storage
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "9NwVNUzm8puqlnZbeAWWChBDJNeibAt6bA0ZbDtp", "WTsHfOfYJVK0ysD1z8OCZP32dy2s3BCOtE9TF0aQ");
    }
}

