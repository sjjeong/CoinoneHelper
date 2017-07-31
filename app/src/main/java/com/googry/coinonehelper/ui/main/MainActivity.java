package com.googry.coinonehelper.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.databinding.MainActivityBinding;
import com.googry.coinonehelper.ui.coin_notification.CoinNotificationActivity;
import com.googry.coinonehelper.ui.compare_another_exchange.CompareAnotherExchangeActivity;
import com.googry.coinonehelper.ui.widget.ExitAdDialog;

public class MainActivity extends BaseActivity<MainFragment> {
    private DrawerLayout mDrawerLayout;
    private MainActivityBinding mBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.main_activity;
    }

    @Override
    protected int getFragmentContentId() {
        return R.id.content_frame;
    }

    @Override
    protected void initView() {
        mBinding = DataBindingUtil.bind(findViewById(R.id.root));
        mBinding.setActivity(this);

    }

    @Override
    protected void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer);
    }

    @Override
    protected MainFragment getFragment() {
        return MainFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        ExitAdDialog exitAdDialog = new ExitAdDialog();
        exitAdDialog.setCancelable(false);
        exitAdDialog.show(getSupportFragmentManager(), exitAdDialog.getTag());

    }

    // databinding
    public void onCompareAnotherExchangeClick(View v) {
        startActivity(new Intent(getApplicationContext(), CompareAnotherExchangeActivity.class));
    }

    // databindng
    public void onCoinNotificationClick(View v) {
        startActivity(new Intent(getApplicationContext(), CoinNotificationActivity.class));
    }

}
