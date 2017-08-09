package com.googry.coinonehelper.ui;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinNotification;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 8. 10..
 */

public class PopupActivity extends AppCompatActivity {
    public static final String EXTRA_ALARM_ID = "EXTRA_ALARM_ID";
    private AlertDialog mDialog;
    private Realm mRealm;
    private CoinNotification mCoinNotification;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

        setContentView(R.layout.popup_activity);

        long alarmId = getIntent().getLongExtra(EXTRA_ALARM_ID, 0);

        mRealm = Realm.getDefaultInstance();
        mCoinNotification = mRealm.where(CoinNotification.class)
                .equalTo("createdTs", alarmId)
                .findFirst();

        if (mCoinNotification != null) {
            showMessagePopup(mCoinNotification);
        } else {
            finishWithNoTransition();
        }
    }

    @Override
    protected void onDestroy() {
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                mCoinNotification.deleteFromRealm();
            }
        });
        if (mDialog != null) mDialog.dismiss();
        super.onDestroy();
    }

    private void showMessagePopup(@NonNull final CoinNotification coinNotification) {
        mDialog = new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(R.string.app_name)
                .setMessage(String.format("%s %,d원 도달",coinNotification.getCoinType().name(),
                        coinNotification.getTargetPrice()))
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        finishWithNoTransition();
                    }
                })
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // cancel the notification
                        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE))
                                .cancel((int) coinNotification.getCreatedTs());
                        finish();
                    }
                })
                .setNegativeButton("앱으로 이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                        startActivity(intent);
                    }
                })
                .create();

        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }

    private void finishWithNoTransition() {
        finish();
        overridePendingTransition(0, 0);
    }

}
