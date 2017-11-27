package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.googry.coinonehelper.data.CommonOrderbook;
import com.googry.coinonehelper.data.source.CoinoneOrderbookRepository;
import com.googry.coinonehelper.data.source.OrderbookDataSource;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 13..
 */

public class OrderbookViewModel implements OrderbookDataSource.OnOrderbookCallback {
    public final ObservableList<CommonOrderbook> asks = new ObservableArrayList<>();
    public final ObservableList<CommonOrderbook> bids = new ObservableArrayList<>();


    private OrderbookDataSource mOrderbookDataSource;
    private String mCoinType;

    public OrderbookViewModel(String coinType) {
        mCoinType = coinType;
        mOrderbookDataSource = new CoinoneOrderbookRepository(coinType);
        mOrderbookDataSource.setOnOrderbookCallback(this);
    }

    public void start() {
        mOrderbookDataSource.start();
    }

    public void stop() {
        mOrderbookDataSource.stop();
    }


    @Override
    public void onOrderbookLoaded(List<CommonOrderbook> asks, List<CommonOrderbook> bids) {
        this.asks.clear();
        this.asks.addAll(asks);
        this.bids.clear();
        this.bids.addAll(bids);
    }

    @Override
    public void onOrderbookLoadFailed() {

    }
}

