package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CommonOrder;

/**
 * Created by seokjunjeong on 2018. 1. 2..
 */

public interface BalanceDataSource {

    void call();

    void setOnCancelOrderCallback(CancelOrderDataSource.OnCancelOrderCallback onCancelOrderCallback);

    interface OnCancelOrderCallback{
        void onCancelSuccess(CommonOrder commonOrder);

        void onCancelFailed(int errorCode);
    }
}
