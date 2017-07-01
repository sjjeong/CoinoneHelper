package com.googry.coinonehelper.ui.compare_another_exchange;

import android.support.v4.app.Fragment;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class CompareAnotherExchangeActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.compare_another_exchange_activity;
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
        return CompareAnotherExchangeFragment.newInstance();
    }
}
