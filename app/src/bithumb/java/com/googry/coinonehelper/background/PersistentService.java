package com.googry.coinonehelper.background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.google.gson.Gson;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.UnitAlarm;
import com.googry.coinonehelper.data.remote.BithumbApiManager;
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

    private Realm mRealm;

    @Override
    public void onCreate() {
        unregisterRestartAlarm();
        super.onCreate();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.package_name),
                    getString(R.string.noti_channel_coinhelper), NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationChannel notificationChannel2 = new NotificationChannel(getString(R.string.package_name_manager),
                    getString(R.string.noti_channel_coinhelper_manager), NotificationManager.IMPORTANCE_NONE);
            notificationManager.createNotificationChannel(notificationChannel2);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            // 알림 데이터 모델 생성 및 데이터 셋팅
            stackBuilder.addParentStack(SplashActivity.class);
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            stackBuilder.addNextIntent(intent);

            PendingIntent contentIntent =
                    PendingIntent.getActivity(this, 1,
                            intent, PendingIntent.FLAG_CANCEL_CURRENT);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this, getString(R.string.package_name_manager))
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setLargeIcon(BitmapFactory.decodeResource(
                                    this.getResources(), R.mipmap.ic_launcher))
                            .setContentTitle(getString(R.string.app_name))
                            .setTicker(getString(R.string.app_name))
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setPriority(NotificationCompat.PRIORITY_MIN)
                            .setAutoCancel(true)
                            .setWhen(0)
                            .setContentText("코인헬퍼 매니저가 동작 중입니다...");
            mBuilder.setContentIntent(contentIntent);
            startForeground(
                    1,
                    mBuilder.build()
            );
        }



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
        mRealm = Realm.getDefaultInstance();

        countDownTimer();
        countDownTimer.start();
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                LogUtil.i(BuildConfig.FLAVOR + " service");
                BithumbApiManager.BithumbPublicApi api = BithumbApiManager.getApiManager().create(BithumbApiManager.BithumbPublicApi.class);
                Call<BithumbTicker> call = api.allTicker();
                call.enqueue(new Callback<BithumbTicker>() {
                    @Override
                    public void onResponse(Call<BithumbTicker> call, Response<BithumbTicker> response) {
                        if (response.body() == null) return;
                        BithumbTicker.Data ticker = response.body().data;
                        if (ticker.btc == null ||
                                ticker.bch == null ||
                                ticker.eth == null ||
                                ticker.etc == null ||
                                ticker.xrp == null ||
                                ticker.dash == null ||
                                ticker.ltc == null ||
                                ticker.xmr == null ||
                                ticker.zec == null ||
                                ticker.qtum == null ||
                                ticker.btg == null||
                                ticker.eos == null) {
                            return;
                        }
                        Gson gson = new Gson();
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.BTC,
                                gson.toJson(ticker.btc, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.BCH,
                                gson.toJson(ticker.bch, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.ETH,
                                gson.toJson(ticker.eth, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.ETC,
                                gson.toJson(ticker.etc, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.XRP,
                                gson.toJson(ticker.xrp, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.DASH,
                                gson.toJson(ticker.dash, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.LTC,
                                gson.toJson(ticker.ltc, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.XMR,
                                gson.toJson(ticker.xmr, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.ZEC,
                                gson.toJson(ticker.zec, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.QTUM,
                                gson.toJson(ticker.qtum, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.BTG,
                                gson.toJson(ticker.btg, BithumbTicker.Ticker.class)
                        );
                        PrefUtil.saveTicker(
                                getApplicationContext(),
                                CoinType.EOS,
                                gson.toJson(ticker.eos, BithumbTicker.Ticker.class)
                        );

                        // 코인 가격 설정 조건 체크
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<CoinNotification> realmResults
                                = realm.where(CoinNotification.class).findAll();
                        long targetPrice;
                        for (CoinType coinType : CoinType.values()) {
                            final UnitAlarm unitAlarm = mRealm.where(UnitAlarm.class)
                                    .equalTo("coinType", coinType.name())
                                    .findFirst();
                            if (unitAlarm == null) {
                                continue;
                            }
                            if (!unitAlarm.runFlag) {
                                continue;
                            }
                            targetPrice = 0;
                            String msg = "";
                            long id = System.currentTimeMillis();
                            switch (coinType) {
                                case BTC: {
                                    targetPrice = ticker.btc.last;
                                    id += 1;
                                }
                                break;
                                case BCH: {
                                    targetPrice = ticker.bch.last;
                                    id += 2;
                                }
                                break;
                                case ETH: {
                                    targetPrice = ticker.eth.last;
                                    id += 3;
                                }
                                break;
                                case ETC: {
                                    targetPrice = ticker.etc.last;
                                    id += 4;
                                }
                                break;
                                case XRP: {
                                    targetPrice = ticker.xrp.last;
                                    id += 5;
                                }
                                break;
                                case DASH: {
                                    targetPrice = ticker.dash.last;
                                    id += 6;
                                }
                                break;
                                case LTC: {
                                    targetPrice = ticker.ltc.last;
                                    id += 7;
                                }
                                break;
                                case XMR: {
                                    targetPrice = ticker.xmr.last;
                                    id += 8;
                                }
                                break;
                                case ZEC: {
                                    targetPrice = ticker.xmr.last;
                                    id += 9;
                                }
                                break;
                                case QTUM: {
                                    targetPrice = ticker.qtum.last;
                                    id += 10;
                                }
                                break;
                                case BTG: {
                                    targetPrice = ticker.btg.last;
                                    id += 11;
                                }
                                break;
                                case EOS: {
                                    targetPrice = ticker.eos.last;
                                    id += 12;
                                }
                                break;
                            }

                            msg += coinType.name() + " " +
                                    targetPrice;


                            long divider = unitAlarm.divider;
                            long prevPrice = unitAlarm.prevPrice;
                            if (targetPrice <= prevPrice - divider ||
                                    targetPrice >= prevPrice + divider) {

                                msg += String.format(" %d원 %s",
                                        (targetPrice - prevPrice),
                                        (targetPrice - prevPrice) >= 0 ? "up" : "down");
                                final long finalTargetPrice = targetPrice;
                                mRealm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        unitAlarm.prevPrice = finalTargetPrice;
                                        realm.copyToRealmOrUpdate(unitAlarm);
                                    }
                                });
                            } else {
                                continue;
                            }
                            showNofi(msg, (int) id);
                        }

                        for (final CoinNotification coinNotification : realmResults) {
                            targetPrice = 0;
                            String msg = "";
                            switch (coinNotification.getCoinType()) {
                                case BTC: {
                                    targetPrice = ticker.btc.last;
                                }
                                break;
                                case BCH: {
                                    targetPrice = ticker.bch.last;
                                }
                                break;
                                case ETH: {
                                    targetPrice = ticker.eth.last;
                                }
                                break;
                                case ETC: {
                                    targetPrice = ticker.etc.last;
                                }
                                break;
                                case XRP: {
                                    targetPrice = ticker.xrp.last;
                                }
                                break;
                                case DASH: {
                                    targetPrice = ticker.dash.last;
                                }
                                break;
                                case LTC: {
                                    targetPrice = ticker.ltc.last;
                                }
                                break;
                                case XMR: {
                                    targetPrice = ticker.xmr.last;
                                }
                                break;
                                case ZEC: {
                                    targetPrice = ticker.zec.last;
                                }
                                break;
                                case QTUM: {
                                    targetPrice = ticker.qtum.last;
                                }
                                break;
                                case BTG: {
                                    targetPrice = ticker.btg.last;
                                }
                                break;
                                case EOS: {
                                    targetPrice = ticker.eos.last;
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

                            showNofi(msg, (int) coinNotification.getCreatedTs());

                            // 팝업 띄우기
                            startActivity(new Intent(getApplicationContext(), PopupActivity.class)
                                    .putExtra(PopupActivity.EXTRA_ALARM_ID, coinNotification.getCreatedTs())
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }

                    }

                    @Override
                    public void onFailure(Call<BithumbTicker> call, Throwable t) {

                    }
                });
            }

            public void onFinish() {
                countDownTimer();
                countDownTimer.start();
            }
        };
    }

    private void showNofi(String msg, int createTs) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // 알림 데이터 모델 생성 및 데이터 셋팅
        stackBuilder.addParentStack(SplashActivity.class);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        stackBuilder.addNextIntent(intent);

        PendingIntent contentIntent =
                PendingIntent.getActivity(this, createTs,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);


        // 노티 띄우기
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, getString(R.string.package_name))
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(
                                getResources(), R.mipmap.ic_launcher))
                        .setContentTitle(getString(R.string.app_name))
                        .setTicker(getString(R.string.app_name))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentText(msg)
                        .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        notificationManager.notify(createTs, mBuilder.build());

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
