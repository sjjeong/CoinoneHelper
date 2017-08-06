package com.googry.coinonehelper.ui.coin_notification_add_alarm;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinNotification;

import io.realm.RealmResults;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public interface CoinNotificationAddAlarmContract {
    interface Presenter extends BasePresenter {
        void requestAddAlarm();

        void loadCoinNotificationList();
    }

    interface View extends BaseView<Presenter> {
        void showAddAlarmPopup();

        void showCoinNotificationList(RealmResults<CoinNotification> realmResults);
    }
}
