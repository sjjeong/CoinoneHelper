package com.googry.coinonehelper.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.databinding.MainActivityBinding;
import com.googry.coinonehelper.ui.coin_notification_add_alarm.CoinNotificationAddAlarmActivity;
import com.googry.coinonehelper.ui.compare_another_exchange.CompareAnotherExchangeActivity;
import com.googry.coinonehelper.ui.widget.ExitAdDialog;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragListener;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;
import com.yarolegovich.slidingrootnav.transform.RootTransformation;

public class MainActivity extends BaseActivity<MainFragment> {
    private MainActivityBinding mBinding;
    private SlidingRootNav mSlidingRootNav;
    private Toolbar mToolbar;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBinding = DataBindingUtil.bind(findViewById(R.id.root));
        mBinding.setActivity(this);
    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(mToolbar)
                .withMenuOpened(true)
                .withRootViewScale(0.8f)
                .withRootViewElevation(10)
                .withMenuLayout(R.layout.main_navigation_drawer)
                .inject();
    }

    @Override
    protected MainFragment getFragment() {
        return MainFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        if (!mSlidingRootNav.isMenuHidden()) {
            mSlidingRootNav.closeMenu();
            return;
        }
        ExitAdDialog exitAdDialog = new ExitAdDialog();
        exitAdDialog.setCancelable(false);
        exitAdDialog.show(getSupportFragmentManager(), exitAdDialog.getTag());

    }

    // databinding
    public void onCompareAnotherExchangeClick(View v) {
        mSlidingRootNav.closeMenu();
        startActivity(new Intent(getApplicationContext(), CompareAnotherExchangeActivity.class));
    }

    // databindng
    public void onCoinNotificationClick(View v) {
        mSlidingRootNav.closeMenu();
        startActivity(new Intent(getApplicationContext(), CoinNotificationAddAlarmActivity.class));
    }

}
