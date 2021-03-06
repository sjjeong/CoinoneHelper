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
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.UnitAlarm;
import com.googry.coinonehelper.data.remote.KorbitApiManager;
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

    private KorbitTicker mKorbitTicker;

    private Realm mRealm;

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
        mRealm = Realm.getDefaultInstance();

        countDownTimer();
        countDownTimer.start();
    }

    public void countDownTimer() {

        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                LogUtil.i(BuildConfig.FLAVOR + " service");
                loadKorbitTicker();
            }

            public void onFinish() {
                countDownTimer();
                countDownTimer.start();
            }
        };
    }

    private void loadKorbitTicker() {
        mKorbitTicker = new KorbitTicker();
        KorbitApiManager.KorbitPublicApi api = KorbitApiManager.getApiManager()
                .create(KorbitApiManager.KorbitPublicApi.class);
        Call<KorbitTicker.Ticker> btcCall = api.btcTicker();
        Call<KorbitTicker.Ticker> bchCall = api.bchTicker();
        Call<KorbitTicker.Ticker> ethCall = api.ethTicker();
        Call<KorbitTicker.Ticker> etcCall = api.etcTicker();
        Call<KorbitTicker.Ticker> xrpCall = api.xrpTicker();
        btcCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.btc = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
            }
        });
        bchCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.bch = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
            }
        });
        ethCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.eth = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
            }
        });
        etcCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.etc = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
            }
        });
        xrpCall.enqueue(new Callback<KorbitTicker.Ticker>() {
            @Override
            public void onResponse(Call<KorbitTicker.Ticker> call, Response<KorbitTicker.Ticker> response) {
                if (response.body() == null) {
                    return;
                }
                mKorbitTicker.xrp = response.body();
                isReadyShowKorbitTicker();
            }

            @Override
            public void onFailure(Call<KorbitTicker.Ticker> call, Throwable t) {
            }
        });

    }

    private void isReadyShowKorbitTicker() {
        if (mKorbitTicker.btc != null &&
                mKorbitTicker.bch != null &&
                mKorbitTicker.eth != null &&
                mKorbitTicker.etc != null &&
                mKorbitTicker.xrp != null) {
            isAllLoad();
        }
    }

    private void isAllLoad() {
        Gson gson = new Gson();
        PrefUtil.saveTicker(
                getApplicationContext(),
                CoinType.BTC,
                gson.toJson(mKorbitTicker.btc, KorbitTicker.Ticker.class)
        );
        PrefUtil.saveTicker(
                getApplicationContext(),
                CoinType.BCH,
                gson.toJson(mKorbitTicker.bch, KorbitTicker.Ticker.class)
        );
        PrefUtil.saveTicker(
                getApplicationContext(),
                CoinType.ETH,
                gson.toJson(mKorbitTicker.eth, KorbitTicker.Ticker.class)
        );
        PrefUtil.saveTicker(
                getApplicationContext(),
                CoinType.ETC,
                gson.toJson(mKorbitTicker.etc, KorbitTicker.Ticker.class)
        );
        PrefUtil.saveTicker(
                getApplicationContext(),
                CoinType.XRP,
                gson.toJson(mKorbitTicker.xrp, KorbitTicker.Ticker.class)
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
                    targetPrice = mKorbitTicker.btc.last;
                    id += 1;
                }
                break;
                case BCH: {
                    targetPrice = mKorbitTicker.bch.last;
                    id += 2;
                }
                break;
                case ETH: {
                    targetPrice = mKorbitTicker.eth.last;
                    id += 3;
                }
                break;
                case ETC: {
                    targetPrice = mKorbitTicker.etc.last;
                    id += 4;
                }
                break;
                case XRP: {
                    targetPrice = mKorbitTicker.xrp.last;
                    id += 5;
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
                    targetPrice = mKorbitTicker.btc.last;
                }
                break;
                case BCH: {
                    targetPrice = mKorbitTicker.bch.last;
                }
                break;
                case ETH: {
                    targetPrice = mKorbitTicker.eth.last;
                }
                break;
                case ETC: {
                    targetPrice = mKorbitTicker.etc.last;
                }
                break;
                case XRP: {
                    targetPrice = mKorbitTicker.xrp.last;
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
            mContext.startActivity(new Intent(mContext, PopupActivity.class)
                    .putExtra(PopupActivity.EXTRA_ALARM_ID, coinNotification.getCreatedTs())
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void showNofi(String msg, int createTs) {
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
                PendingIntent.getActivity(mContext, createTs,
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
        mNotificationManager.notify(createTs, mBuilder.build());
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
