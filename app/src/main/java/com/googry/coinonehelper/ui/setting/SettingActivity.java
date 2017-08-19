package com.googry.coinonehelper.ui.setting;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.databinding.SettingActivityBinding;

/**
 * Created by seokjunjeong on 2017. 8. 19..
 */

public class SettingActivity extends AppCompatActivity {
    private SettingActivityBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.setting_activity);
        mBinding.toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        mBinding.setActivity(this);

    }

    // databinding
    public void onBackClick(View v) {
        finish();
    }
}
