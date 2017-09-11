package com.googry.coinonehelper.background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;
import com.googry.coinonehelper.ui.PopupActivity;
import com.googry.coinonehelper.ui.SplashActivity;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

import io.realm.Realm;
import io.realm.RealmResults;
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

    private Context mContext;

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
        mContext = this;

        countDownTimer();
        countDownTimer.start();
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                LogUtil.i(BuildConfig.FLAVOR + " service");
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
                                CoinType.BCH,
                                gson.toJson(coinoneTicker.bch, CoinoneTicker.Ticker.class)
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
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.QTUM,
                                gson.toJson(coinoneTicker.qtum, CoinoneTicker.Ticker.class)
                        );

                        // 코인 가격 설정 조건 체크
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<CoinNotification> realmResults
                                = realm.where(CoinNotification.class).findAll();
                        for (final CoinNotification coinNotification : realmResults) {
                            long targetPrice = 0;
                            String msg = "";
                            switch (coinNotification.getCoinType()) {
                                case BTC: {
                                    targetPrice = coinoneTicker.btc.last;
                                }
                                break;
                                case BCH: {
                                    targetPrice = coinoneTicker.bch.last;
                                }
                                break;
                                case ETH: {
                                    targetPrice = coinoneTicker.eth.last;
                                }
                                break;
                                case ETC: {
                                    targetPrice = coinoneTicker.etc.last;
                                }
                                break;
                                case XRP: {
                                    targetPrice = coinoneTicker.xrp.last;
                                }
                                break;
                            }
                            msg += coinNotification.getCoinType().name() + " " +
                                    targetPrice;
                            if (coinNotification.getPriceDirection() ==
                                    CoinNotification.PriceDirection.Up &&
                                    targetPrice >= coinNotification.getTargetPrice()) {
                                // 상승 가격 도달
                                msg += " up";
                            } else if (coinNotification.getPriceDirection() ==
                                    CoinNotification.PriceDirection.Down &&
                                    targetPrice <= coinNotification.getTargetPrice()) {
                                // 하락 가격 도달
                                msg += " down";
                            } else {
                                continue;
                            }
                            NotificationManager mNotificationManager;
                            mNotificationManager =
                                    (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

                            TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);

                            // 알림 데이터 모델 생성 및 데이터 셋팅
                            stackBuilder.addParentStack(SplashActivity.class);
                            Intent intent = new Intent(mContext, SplashActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            stackBuilder.addNextIntent(intent);

                            PendingIntent contentIntent =
                                    PendingIntent.getActivity(mContext, (int) coinNotification.getCreatedTs(),
                                            intent, PendingIntent.FLAG_CANCEL_CURRENT);

                            // 노티 띄우기
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(mContext)
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setLargeIcon(BitmapFactory.decodeResource(
                                                    mContext.getResources(), R.mipmap.ic_launcher))
                                            .setContentTitle(mContext.getString(R.string.app_name))
                                            .setTicker(mContext.getString(R.string.app_name))
                                            .setDefaults(Notification.DEFAULT_ALL)
                                            .setContentText(msg)
                                            .setAutoCancel(true);
                            mBuilder.setContentIntent(contentIntent);
                            mNotificationManager.notify((int) coinNotification.getCreatedTs(), mBuilder.build());

                            // 팝업 띄우기
                            mContext.startActivity(new Intent(mContext, PopupActivity.class)
                                    .putExtra(PopupActivity.EXTRA_ALARM_ID, coinNotification.getCreatedTs())
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
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
