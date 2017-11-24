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

public class TradeViewModel implements OrderbookDataSource.OnOrderbookCallback {
    public final ObservableList<CommonOrderbook> orderbooks = new ObservableArrayList<>();


    private OrderbookDataSource mOrderbookDataSource;
    private String mCoinType;

    public TradeViewModel(String coinType) {
        mCoinType = coinType;
        mOrderbookDataSource = new CoinoneOrderbookRepository(coinType);
        mOrderbookDataSource.setOnOrderbookCallback(this);
    }

    public void start() {

    }

    public void stop() {

    }

    @Override
    public void onOrderbookLoaded(List<CommonOrderbook> orderbooks) {
        orderbooks.clear();
        orderbooks.addAll(orderbooks);
    }

    @Override
    public void onOrderbookLoadFailed() {

    }
}

