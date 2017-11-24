package com.googry.coinonehelper.data.source;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public class CoinoneOrderbookRepository implements OrderbookDataSource {

    private OnOrderbookCallback mOnOrderbookCallback;
    private String mCoinType;

    public CoinoneOrderbookRepository(String coinType) {
        mCoinType = coinType;
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setOnOrderbookCallback(OnOrderbookCallback onOrderbookCallback) {
        mOnOrderbookCallback = onOrderbookCallback;
    }
}
