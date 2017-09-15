package com.googry.coinonehelper;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinoneHelperApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        FirebaseApp.initializeApp(this);
    }
}
