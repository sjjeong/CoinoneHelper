package com.googry.coinonehelper.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;

import com.google.gson.Gson;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 6. 1..
 */

public class PersistentService extends Service {

    private static final int COUNT_DOWN_INTERVAL = 1000 * 60;
    private static final int MILLISINFUTURE = 86400 * 1000;

    private CountDownTimer countDownTimer;

    @Override
    public void onCreate() {
        unregisterRestartAlarm();
        super.onCreate();

        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();

        /**
         * 서비스 종료 시 알람 등록을 통해 서비스 재 실행
         */
        registerRestartAlarm();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 데이터 초기화
     */
    private void initData() {


        countDownTimer();
        countDownTimer.start();
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                CoinoneApiManager.CoinonePublicApi api = CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePublicApi.class);
                Call<CoinoneTicker> call = api.allTicker();
                call.enqueue(new Callback<CoinoneTicker>() {
                    @Override
                    public void onResponse(Call<CoinoneTicker> call, Response<CoinoneTicker> response) {
                        if (response.body() == null) return;
                        CoinoneTicker coinoneTicker = response.body();
                        Gson gson = new Gson();
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.BTC,
                                gson.toJson(coinoneTicker.btc, CoinoneTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.ETH,
                                gson.toJson(coinoneTicker.eth, CoinoneTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.ETC,
                                gson.toJson(coinoneTicker.etc, CoinoneTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.XRP,
                                gson.toJson(coinoneTicker.xrp, CoinoneTicker.Ticker.class)
                        );
                    }

                    @Override
                    public void onFailure(Call<CoinoneTicker> call, Throwable t) {

                    }
                });
            }

            public void onFinish() {
                countDownTimer();
                countDownTimer.start();
            }
        };
    }


    /**
     * 알람 매니져에 서비스 등록
     */
    private void registerRestartAlarm() {
        LogUtil.i("registerRestartAlarm");
        Intent intent = new Intent(PersistentService.this, RestartReceiver.class);
        intent.setAction("ACTION.RESTART.PersistentService");
        PendingIntent sender = PendingIntent.getBroadcast(PersistentService.this, 0, intent, 0);

        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 1 * 1000;

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /**
         * 알람 등록
         */
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 1 * 1000, sender);

    }

    /**
     * 알람 매니져에 서비스 해제
     */
    private void unregisterRestartAlarm() {
        LogUtil.i("unregisterRestartAlarm");

        Intent intent = new Intent(PersistentService.this, RestartReceiver.class);
        intent.setAction("ACTION.RESTART.PersistentService");
        PendingIntent sender = PendingIntent.getBroadcast(PersistentService.this, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        /**
         * 알람 취소
         */
        alarmManager.cancel(sender);


    }
}
