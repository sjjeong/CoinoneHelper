package com.googry.coinonehelper.ui.coin_notification;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public class CoinNotificationPresenter implements CoinNotificationContract.Presenter {
    private CoinNotificationContract.View mView;

    public CoinNotificationPresenter(CoinNotificationContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
