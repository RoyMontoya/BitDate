package com.example.amado.bitdate;

import android.app.Application;

import com.firebase.client.Firebase;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

/**
 * Created by Amado on 03/06/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        Firebase.setAndroidContext(this);
        super.onCreate();
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "yyzYFJVHmU2h9LDn2AZL8Sv7Kl6VOioclSnMDeai", "c8l3L3IROZC4zGf7EUWQEsgUGH6dtIpJvbZlB5Pq");
        ParseFacebookUtils.initialize(this);
    }
}
