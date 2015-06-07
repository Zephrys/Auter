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

        Parse.initialize(this, "zU277hQ68BQBoxiWnprRysK8ZVP1NpkWL4lIzt0k", "oViPPq5knlwGskyrmNCuQfCzPpdYvxnxc1kTnYV5");
    }
}

