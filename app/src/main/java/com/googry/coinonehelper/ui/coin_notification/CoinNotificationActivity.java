package com.googry.coinonehelper.ui.coin_notification;

import android.support.v4.app.Fragment;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public class CoinNotificationActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.coin_notification_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.content_frame;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initToolbar() {

    }

    @Override
    protected Fragment getFragment() {
        return CoinNotificationFragment.newInstance();
    }
}
