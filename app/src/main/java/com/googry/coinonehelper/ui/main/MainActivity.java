package com.googry.coinonehelper.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.databinding.MainNavigationDrawerBinding;
import com.googry.coinonehelper.ui.main.chatting.ChattingFragment;
import com.googry.coinonehelper.ui.main.coin_notification_add_alarm.CoinNotificationAddAlarmFragment;
import com.googry.coinonehelper.ui.main.compare_another_exchange.CompareAnotherExchangeFragment;
import com.googry.coinonehelper.ui.setting.SettingActivity;
import com.googry.coinonehelper.ui.widget.ExitAdDialog;
import com.googry.coinonehelper.util.ui.FragmentUtil;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

public class MainActivity extends BaseActivity<MainFragment> {
    private MainNavigationDrawerBinding mBinding;
    private SlidingRootNav mSlidingRootNav;
    private Toolbar mToolbar;
    private AdView mAdView;

    private CoinNotificationAddAlarmFragment mCoinNotificationAddAlarmFragment;
    private CompareAnotherExchangeFragment mCompareAnotherExchangeFragment;
    private ChattingFragment mChattingFragment;

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
        setAddSetting();
    }

    @Override
    protected void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        setTitle(R.string.btc);

        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(mToolbar)
                .withRootViewScale(0.8f)
                .withMenuOpened(true)
                .withMenuLayout(R.layout.main_navigation_drawer)
                .inject();

        mBinding = DataBindingUtil.bind(mSlidingRootNav.getLayout().findViewById(R.id.root));
        mBinding.setActivity(this);
    }

    @Override
    protected MainFragment getFragment() {
        return MainFragment.newInstance();
    }

    // databinding
    public void onOrderbookClick(View v) {
        CoinType coinType = CoinType.BTC;
        switch (v.getId()) {
            case R.id.btn_btc: {
                coinType = CoinType.BTC;
                break;
            }
            case R.id.btn_bch: {
                coinType = CoinType.BCH;
                break;
            }
            case R.id.btn_eth: {
                coinType = CoinType.ETH;
                break;
            }
            case R.id.btn_etc: {
                coinType = CoinType.ETC;
                break;
            }
            case R.id.btn_xrp: {
                coinType = CoinType.XRP;
                break;
            }
        }
        if (FragmentUtil.getFragment(this, getFragmentContentId()) != mFragment) {
            mFragment = MainFragment.newInstance(coinType);
            FragmentUtil.replaceFragment(this, getFragmentContentId(), mFragment);
        } else {
            mFragment.setCoinTypeUi(coinType);
        }
        mSlidingRootNav.closeMenu();
        getSupportActionBar().setTitle(((TextView) v).getText().toString());
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

    private void setAddSetting() {
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
        if (!mSlidingRootNav.isMenuHidden()) {
            mSlidingRootNav.closeMenu();
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
