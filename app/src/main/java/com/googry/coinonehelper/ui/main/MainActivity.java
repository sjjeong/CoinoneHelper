package com.googry.coinonehelper.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.databinding.MainNavigationDrawerBinding;
import com.googry.coinonehelper.ui.chart.ChartActivity;
import com.googry.coinonehelper.ui.main.chatting.ChattingFragment;
import com.googry.coinonehelper.ui.main.coin_notification_add_alarm.CoinNotificationAddAlarmFragment;
import com.googry.coinonehelper.ui.main.coin_volume.CoinVolumeFragment;
import com.googry.coinonehelper.ui.main.compare_another_exchange.CompareAnotherExchangeFragment;
import com.googry.coinonehelper.ui.setting.SettingActivity;
import com.googry.coinonehelper.ui.widget.ExitAdDialog;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;
import com.googry.coinonehelper.util.ui.FragmentUtil;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;
import com.yarolegovich.slidingrootnav.callback.DragStateListener;

public class MainActivity extends BaseActivity<MainFragment> {
    private MainNavigationDrawerBinding mBinding;
    private SlidingRootNav mSlidingRootNav;
    private Toolbar mToolbar;
    private AdView mAdView;

    private CoinNotificationAddAlarmFragment mCoinNotificationAddAlarmFragment;
    private CompareAnotherExchangeFragment mCompareAnotherExchangeFragment;
    private ChattingFragment mChattingFragment;
    private CoinVolumeFragment mCoinVolumeFragment;

    private SlideMenuCoinTypeAdapter mSlideMenuCoinTypeAdapter;

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
        mAdView = (AdView) findViewById(R.id.ad_view);
        setAdSetting();
    }

    @Override
    protected void initToolbar(@Nullable Bundle savedInstanceState) {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        setTitle(R.string.btc);

        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(mToolbar)
                .withRootViewScale(0.8f)
                .withMenuOpened(true)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.main_navigation_drawer)
                .addDragStateListener(new DragStateListener() {
                    @Override
                    public void onDragStart() {
                        mSlideMenuCoinTypeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onDragEnd(boolean isMenuOpened) {
                    }
                })
                .inject();

        mBinding = DataBindingUtil.bind(mSlidingRootNav.getLayout().findViewById(R.id.root));
        mBinding.setActivity(this);

        mSlideMenuCoinTypeAdapter = new SlideMenuCoinTypeAdapter(new SlideMenuCoinTypeAdapter.OnCoinTypeItemClickListener() {
            @Override
            public void onCoinTypeItemClickListener(CoinType coinType) {
                if (FragmentUtil.getFragment(MainActivity.this, getFragmentContentId()) != mFragment) {
                    mFragment = MainFragment.newInstance(coinType);
                    FragmentUtil.replaceFragment(MainActivity.this, getFragmentContentId(), mFragment);
                } else {
                    mFragment.setCoinTypeUi(coinType);
                }
                mSlidingRootNav.closeMenu();
                getSupportActionBar().setTitle(CoinType.getCoinTitleRes(coinType));
            }
        });
        mBinding.rvCoinType.setAdapter(mSlideMenuCoinTypeAdapter);

    }

    @Override
    protected MainFragment getFragment() {
        return MainFragment.newInstance();
    }

    // databinding
    public void onCompareAnotherExchangeClick(View v) {
        if (mCompareAnotherExchangeFragment == null) {
            mCompareAnotherExchangeFragment = CompareAnotherExchangeFragment.newInstance();
        }
        replaceFragment(mCompareAnotherExchangeFragment, R.string.compare_another_exchange);
    }

    // databindng
    public void onCoinNotificationClick(View v) {
        if (mCoinNotificationAddAlarmFragment == null) {
            mCoinNotificationAddAlarmFragment = CoinNotificationAddAlarmFragment.newInstance();
        }
        replaceFragment(mCoinNotificationAddAlarmFragment, R.string.coin_notification_alarm);
    }

    // databinding
    public void onChattingClick(View v) {
        if (mChattingFragment == null) {
            mChattingFragment = ChattingFragment.newInstance();
        }
        replaceFragment(mChattingFragment, R.string.chatting);
        Toast.makeText(getApplicationContext(),"현재 개발중인 서비스입니다.\n서비스 이용이 제한적 일 수 있습니다.",Toast.LENGTH_LONG).show();
    }

    // databinding
    public void onShowCoinVolumeClick(View v) {
        if (mCoinVolumeFragment == null) {
            mCoinVolumeFragment = CoinVolumeFragment.newInstance();
        }
        replaceFragment(mCoinVolumeFragment, R.string.coin_volume);
    }

    // databinding
    public void onShowChartClick(View v) {
        startActivity(new Intent(getApplicationContext(), ChartActivity.class));
    }

    // databinding
    public void onSettingClick(View v){
        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
    }

    private void replaceFragment(Fragment fragment, @StringRes int titleRes) {
        FragmentUtil.replaceFragment(this, getFragmentContentId(), fragment);
        getSupportActionBar().setTitle(titleRes);
        mSlidingRootNav.closeMenu();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();

        super.onDestroy();
    }

    private void setAdSetting() {
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mChattingFragment != null) {
            mChattingFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        if (mSlidingRootNav.isMenuHidden()) {
            mSlidingRootNav.openMenu();
            return;
        }
        ExitAdDialog exitAdDialog = new ExitAdDialog();
        exitAdDialog.setCancelable(false);
        exitAdDialog.show(getSupportFragmentManager(), exitAdDialog.getTag());

    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resume the AdView.
        mAdView.resume();
    }
}
