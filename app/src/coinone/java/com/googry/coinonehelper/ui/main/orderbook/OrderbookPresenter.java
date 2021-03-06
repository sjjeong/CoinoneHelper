package com.googry.coinonehelper.ui.main.orderbook;

import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.CoinoneTrade;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookPresenter implements OrderbookContract.Presenter {
    private static final int REFRESH_PERIOD = 5000;
    private OrderbookContract.View mView;
    private CoinType mCoinType;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Callback<CoinoneOrderbook> mOrderbookCallback = new Callback<CoinoneOrderbook>() {
        @Override
        public void onResponse(Call<CoinoneOrderbook> call, Response<CoinoneOrderbook> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            mView.showOrderbookList(response.body());
        }

        @Override
        public void onFailure(Call<CoinoneOrderbook> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };
    private Callback<CoinoneTrade> mTradeCallback = new Callback<CoinoneTrade>() {
        @Override
        public void onResponse(Call<CoinoneTrade> call, Response<CoinoneTrade> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            mView.showTradeList(response.body());
        }


        @Override
        public void onFailure(Call<CoinoneTrade> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    private Callback<CoinoneTicker.Ticker> mTickerCallback = new Callback<CoinoneTicker.Ticker>() {
        @Override
        public void onResponse(Call<CoinoneTicker.Ticker> call, Response<CoinoneTicker.Ticker> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            mView.showTicker(response.body());
        }

        @Override
        public void onFailure(Call<CoinoneTicker.Ticker> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    public OrderbookPresenter(OrderbookContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void setCoinType(CoinType coinType) {
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
        makeTimer();
    }

    private void makeTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                requestOrderbook(mCoinType);
            }
        };
        mTimer.schedule(mTimerTask, REFRESH_PERIOD, REFRESH_PERIOD);
    }

    private void requestOrderbook(CoinType coinType) {
        CoinoneApiManager.CoinonePublicApi api = CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePublicApi.class);
        Call<CoinoneOrderbook> callOrderbook = api.orderbook(coinType.name());
        callOrderbook.enqueue(mOrderbookCallback);
        Call<CoinoneTrade> callTrade = api.trades(coinType.name(), "hour");
        callTrade.enqueue(mTradeCallback);
        Call<CoinoneTicker.Ticker> callTicker = api.ticker(coinType.name());
        callTicker.enqueue(mTickerCallback);

    }
}
