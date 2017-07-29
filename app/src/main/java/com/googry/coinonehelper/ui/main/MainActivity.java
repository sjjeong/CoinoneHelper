package com.googry.coinonehelper.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseActivity;
import com.googry.coinonehelper.databinding.MainActivityBinding;
import com.googry.coinonehelper.ui.coin_notification.CoinNotificationActivity;
import com.googry.coinonehelper.ui.compare_another_exchange.CompareAnotherExchangeActivity;
import com.googry.coinonehelper.ui.widget.ExitAdDialog;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

public class MainActivity extends BaseActivity<MainFragment> {
    private DrawerLayout mDrawerLayout;
    private MainActivityBinding mBinding;
    private RewardedVideoAd mCompareAnotherExchangeRewardedVideoAd;
    private RewardedVideoAdListener mCompareAnotherExchangeRewardedVideoAdListener =
            new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    //1
                    LogUtil.i("onRewardedVideoAdLoaded");
                }

                @Override
                public void onRewardedVideoAdOpened() {
                    //2
                    LogUtil.i("onRewardedVideoAdOpened");
                }

                @Override
                public void onRewardedVideoStarted() {
                    //3
                    LogUtil.i("onRewardedVideoStarted");
                }

                @Override
                public void onRewardedVideoAdClosed() {
                    //5
                    LogUtil.i("onRewardedVideoAdClosed");
                    mBinding.setIsSeeAd(PrefUtil.isShowCompareTradeSite(getApplicationContext()));
                    if (shouldLoadRewardAd()) {
                        mCompareAnotherExchangeRewardedVideoAd
                                .loadAd(getString(R.string.admob_compare_trade_site), new AdRequest.Builder().build());
                    } else {
                        startActivity(new Intent(getApplicationContext(), CompareAnotherExchangeActivity.class));
                    }
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    //4
                    LogUtil.i("onRewarded");
                    PrefUtil.saveShowCompareTradeSite(getApplicationContext(), true);
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {
                    LogUtil.i("onRewardedVideoAdLeftApplication");
                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    LogUtil.i("onRewardedVideoAdFailedToLoad");
                }
            };

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

        mBinding.setIsSeeAd(PrefUtil.isShowCompareTradeSite(getApplicationContext()));


        if (!PrefUtil.isShowCompareTradeSite(getApplicationContext()))
            LogUtil.i("no show reward ad");
        else {
            LogUtil.i("show reward ad");
        }

        // load rewarded ad
        if (shouldLoadRewardAd()) {
            mCompareAnotherExchangeRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            mCompareAnotherExchangeRewardedVideoAd.setRewardedVideoAdListener(mCompareAnotherExchangeRewardedVideoAdListener);
            if (!mCompareAnotherExchangeRewardedVideoAd.isLoaded()) {
                mCompareAnotherExchangeRewardedVideoAd
                        .loadAd(getString(R.string.admob_compare_trade_site), new AdRequest.Builder().build());
            }
        }

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

    @Override
    protected void onPause() {
        if (shouldLoadRewardAd())
            mCompareAnotherExchangeRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (shouldLoadRewardAd())
            mCompareAnotherExchangeRewardedVideoAd.resume(this);
        super.onResume();
    }

    /*
     * 리워드 광고를 불러야 하는지 결정하는 함수
     */
    private boolean shouldLoadRewardAd() {
        return !PrefUtil.isShowCompareTradeSite(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        if (shouldLoadRewardAd())
            mCompareAnotherExchangeRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    // databinding
    public void onCompareAnotherExchangeClick(View v) {
        if (!PrefUtil.isShowCompareTradeSite(getApplicationContext())) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("광고 시청이 필요합니다.")
                    .setMessage("거래소 별 가격 화면을 보기 위해서 최초 1회 광고 시청이 필요합니다.")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mCompareAnotherExchangeRewardedVideoAd.isLoaded()) {
                                mCompareAnotherExchangeRewardedVideoAd.show();
                            } else {
                                Toast.makeText(getApplicationContext(), "잠시 후에 다시 시도해 주세요.", Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create()
                    .show();
        } else {
            startActivity(new Intent(getApplicationContext(), CompareAnotherExchangeActivity.class));
        }
    }

    // databindng
    public void onCoinNotificationClick(View v) {
        startActivity(new Intent(getApplicationContext(), CoinNotificationActivity.class));
    }

}
