package com.googry.coinonehelper.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.googry.coinonehelper.ui.main.MainActivity;

/**
 * Created by seokjunjeong on 2017. 5. 31..
 */

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
