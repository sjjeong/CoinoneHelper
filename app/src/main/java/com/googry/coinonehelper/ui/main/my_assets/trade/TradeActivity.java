package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;

/**
 * Created by seokjunjeong on 2017. 11. 13..
 */

public class TradeActivity extends BaseActivity<TradeFragment> {
    public static final String EXTRA_COIN_TYPE = "EXTRA_COIN_TYPE";
    private String mCoinName;

    @Override
    protected int getLayoutId() {
        return R.layout.trade_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.content_frame;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initToolbar(@Nullable Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getString(R.string.coin_trade, mCoinName));
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected TradeFragment getFragment() {
        mCoinName = getIntent().getStringExtra(EXTRA_COIN_TYPE);
        TradeFragment fragment = TradeFragment.newInstance(mCoinName);
        return fragment;
    }
}
