package com.googry.coinonehelper.ui.main.orderbook;

import android.util.Log;

import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.CoinoneTrades;
import com.googry.coinonehelper.data.remote.ApiManager;
import com.googry.coinonehelper.util.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookPresenter implements OrderbookContract.Presenter {
    private static final int TRADE_CNT = 20;
    private static final int REFRESH_PERIOD = 3000;
    private OrderbookContract.View mView;
    private String mCoinType;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Callback<CoinoneOrderbook> callbackOrderbook = new Callback<CoinoneOrderbook>() {
        @Override
        public void onResponse(Call<CoinoneOrderbook> call, Response<CoinoneOrderbook> response) {
            mView.hideCoinoneServerDownProgressDialog();
            CoinoneOrderbook coinoneOrderbook = response.body();
            mView.showOrderbookList(new ArrayList<>(coinoneOrderbook.askes.subList(0, 20)),
                    new ArrayList<>(coinoneOrderbook.bides.subList(0, 20)));
        }

        @Override
        public void onFailure(Call<CoinoneOrderbook> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };
    private Callback<CoinoneTrades> callbackTrades = new Callback<CoinoneTrades>() {
        @Override
        public void onResponse(Call<CoinoneTrades> call, Response<CoinoneTrades> response) {
            mView.hideCoinoneServerDownProgressDialog();
            CoinoneTrades coinoneTrades = response.body();
            Collections.reverse(coinoneTrades.completeOrders);
            mView.showTradeList(new ArrayList<>(coinoneTrades.completeOrders.subList(0, TRADE_CNT)));
        }


        @Override
        public void onFailure(Call<CoinoneTrades> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    public OrderbookPresenter(OrderbookContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        requestOrderbook(mCoinType);
    }

    @Override
    public void setCoinType(String coinType) {
        mCoinType = coinType;
    }

    @Override
    public void stop() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    @Override
    public void load() {
        requestOrderbook(mCoinType);
    }

    private void makeTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                requestOrderbook(mCoinType);
            }
        };
        mTimer.schedule(mTimerTask, 1000, REFRESH_PERIOD);
    }

    private void requestOrderbook(String coinType) {
        LogUtil.i(mCoinType + " : request");
        ApiManager.PublicApi api = ApiManager.getApiManager().create(ApiManager.PublicApi.class);
        Call<CoinoneOrderbook> callOrderbook = api.orderbook(coinType);
        callOrderbook.enqueue(callbackOrderbook);
        Call<CoinoneTrades> callTrade = api.trades(coinType, "hour");
        callTrade.enqueue(callbackTrades);

    }
}
