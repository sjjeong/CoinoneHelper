package com.googry.coinonehelper.ui.main;

import android.databinding.DataBindingUtil;
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
import com.googry.coinonehelper.ui.coin_notification_add_alarm.CoinNotificationAddAlarmFragment;
import com.googry.coinonehelper.ui.compare_another_exchange.CompareAnotherExchangeFragment;
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
        setSupportActionBar(mToolbar);
        setTitle(R.string.btc);

        mSlidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(mToolbar)
                .withMenuOpened(true)
                .withRootViewScale(0.8f)
                .withRootViewElevation(10)
                .withMenuLayout(R.layout.main_navigation_drawer)
                .inject();

        mBinding = DataBindingUtil.bind(mSlidingRootNav.getLayout().findViewById(R.id.root));
        mBinding.setActivity(this);
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

    // databinding
    public void onOrderbookClick(View v) {
        CoinType coinType = CoinType.BTC;
        switch (v.getId()) {
            case R.id.btn_btc: {
                coinType = CoinType.BTC;
                break;
            }
            case R.id.btn_bch: {
                Toast.makeText(getApplicationContext(), "아직 지원을 하지 않습니다.", Toast.LENGTH_LONG).show();
                return;
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
        FragmentUtil.replaceFragment(this, getFragmentContentId(), mCompareAnotherExchangeFragment);
        getSupportActionBar().setTitle(R.string.compare_another_exchange);
        mSlidingRootNav.closeMenu();
    }

    // databindng
    public void onCoinNotificationClick(View v) {
        if (mCoinNotificationAddAlarmFragment == null) {
            mCoinNotificationAddAlarmFragment = CoinNotificationAddAlarmFragment.newInstance();
        }
        FragmentUtil.replaceFragment(this, getFragmentContentId(), mCoinNotificationAddAlarmFragment);
        getSupportActionBar().setTitle(R.string.coin_notification_alarm);
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

}
