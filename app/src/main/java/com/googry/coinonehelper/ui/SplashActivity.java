package com.googry.coinonehelper.ui;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.background.PersistentService;
import com.googry.coinonehelper.background.RestartReceiver;
import com.googry.coinonehelper.ui.main.MainActivity;
import com.googry.coinonehelper.util.LogUtil;

/**
 * Created by seokjunjeong on 2017. 5. 31..
 */

public class SplashActivity extends AppCompatActivity {
    private Intent intent;
    private RestartReceiver restartReceiver;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

        if (BuildConfig.DEBUG) {
            startApp();
        } else {
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getString(R.string.admob_start_interstitial_id));
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    LogUtil.i("onAdClosed");
                    startApp();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    LogUtil.i("onAdFailedToLoad");
                    startApp();
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                    LogUtil.i("onAdLeftApplication");
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                    LogUtil.i("onAdOpened");
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    LogUtil.i("onAdLoaded");
                    mInterstitialAd.show();
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //브로드 캐스트 해제
        unregisterReceiver(restartReceiver);
    }

    private void startApp() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    /**
     * 데이터 초기화
     */
    private void initData() {

        //리스타트 서비스 생성
        restartReceiver = new RestartReceiver();
        intent = new Intent(SplashActivity.this, PersistentService.class);


        IntentFilter intentFilter = new IntentFilter("com.googry.coinonehelper.background.PersistentService");
        //브로드 캐스트에 등록
        registerReceiver(restartReceiver, intentFilter);
        // 서비스 시작
        startService(intent);


    }
}
