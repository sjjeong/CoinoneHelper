package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CommonOrderbook;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public interface OrderbookDataSource {
    void start();

    void stop();

    void setOnOrderbookCallback(OnOrderbookCallback onOrderbookCallback);

    interface OnOrderbookCallback {
        void onOrderbookLoaded(List<CommonOrderbook> asks, List<CommonOrderbook> bids);

        void onOrderbookLoadFailed();
    }
}
