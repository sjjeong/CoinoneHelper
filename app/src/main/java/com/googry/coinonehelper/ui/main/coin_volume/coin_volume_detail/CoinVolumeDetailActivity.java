package com.googry.coinonehelper.ui.main.coin_volume.coin_volume_detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.data.CoinMarketCap;

/**
 * Created by seokjunjeong on 2017. 10. 17..
 */

public class CoinVolumeDetailActivity extends BaseActivity<CoinVolumeDetailFragment> {
    public static final String KEY_COIN_MARKET_CAP = "KEY_COIN_MARKET_CAP";

    @Override
    protected int getLayoutId() {
        return R.layout.simple_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.content_frame;
    }

    @Override
    protected void initView() {
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initToolbar(@Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(((CoinMarketCap)getIntent().getParcelableExtra(KEY_COIN_MARKET_CAP)).name);
    }

    @Override
    protected CoinVolumeDetailFragment getFragment() {
        return CoinVolumeDetailFragment.newInstance(((CoinMarketCap)getIntent().getParcelableExtra(KEY_COIN_MARKET_CAP)));
    }
}
