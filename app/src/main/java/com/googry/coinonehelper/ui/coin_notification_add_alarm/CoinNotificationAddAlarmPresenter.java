package com.googry.coinonehelper.ui.coin_notification_add_alarm;

import com.googry.coinonehelper.data.CoinNotification;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public class CoinNotificationAddAlarmPresenter implements CoinNotificationAddAlarmContract.Presenter {
    private CoinNotificationAddAlarmContract.View mView;

    public CoinNotificationAddAlarmPresenter(CoinNotificationAddAlarmContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCoinNotificationList();
    }

    @Override
    public void requestAddAlarm() {
        mView.showAddAlarmPopup();
    }

    @Override
    public void loadCoinNotificationList() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<CoinNotification> realmResults = realm.where(CoinNotification.class).findAll();
        mView.showCoinNotificationList(realmResults);
    }
}
