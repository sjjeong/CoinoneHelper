package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CommonOrder;

/**
 * Created by seokjunjeong on 2017. 12. 3..
 */

public interface BuySellOrderDataSource {

    void call(boolean isAsk, long price, double amount);

    void setOnBuySellOrderCallback(OnBuySellOrderCallback onBuySellOrderCallback);

    interface OnBuySellOrderCallback {
        void onBuySellOrderSuccess(CommonOrder commonOrder);

        void onBuySellOrderFailed(int errorCode);
    }
}
