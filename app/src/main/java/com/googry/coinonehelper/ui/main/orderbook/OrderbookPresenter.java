package com.googry.coinonehelper.ui.main.orderbook;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.CoinoneTrade;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;
import com.googry.coinonehelper.util.PrefUtil;

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
    private static final int ORDERBOOK_CNT = 20;
    private static final int TRADE_CNT = 20;
    private static final int REFRESH_PERIOD = 5000;
    private Context mContext;
    private OrderbookContract.View mView;
    private CoinType mCoinType;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private Callback<CoinoneOrderbook> mOrderbookCallback = new Callback<CoinoneOrderbook>() {
        @Override
        public void onResponse(Call<CoinoneOrderbook> call, Response<CoinoneOrderbook> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            saveCoinoneOrderbook(response.body());
            loadCoinoneOrderbook();
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
            saveCoinoneTrade(response.body());
            loadCoinoneTrade();
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
            saveCoinoneTicker(response.body());
            loadCoinoneTicker();
        }

        @Override
        public void onFailure(Call<CoinoneTicker.Ticker> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    public OrderbookPresenter(Context context, OrderbookContract.View view) {
        mContext = context;
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadCoinoneOrderbook();
        loadCoinoneTrade();
        loadCoinoneTicker();
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

    @Override
    public void loadCoinoneOrderbook() {
        mView.showOrderbookList(new Gson().fromJson(PrefUtil.loadOrderbook(mContext, mCoinType), CoinoneOrderbook.class));
    }

    @Override
    public void loadCoinoneTrade() {
        mView.showTradeList(new Gson().fromJson(PrefUtil.loadCompleteOrder(mContext, mCoinType), CoinoneTrade.class));
    }

    @Override
    public void loadCoinoneTicker() {
        mView.showTicker(new Gson().fromJson(PrefUtil.loadTicker(mContext, mCoinType), CoinoneTicker.Ticker.class));
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

    private void requestOrderbook(CoinType coinType) {
        CoinoneApiManager.CoinonePublicApi api = CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePublicApi.class);
        Call<CoinoneOrderbook> callOrderbook = api.orderbook(coinType.name());
        callOrderbook.enqueue(mOrderbookCallback);
        Call<CoinoneTrade> callTrade = api.trades(coinType.name(), "hour");
        callTrade.enqueue(mTradeCallback);
        Call<CoinoneTicker.Ticker> callTicker = api.ticker(coinType.name());
        callTicker.enqueue(mTickerCallback);

    }

    private void saveCoinoneOrderbook(CoinoneOrderbook coinoneOrderbook) {
        coinoneOrderbook.askes = new ArrayList<>(coinoneOrderbook.askes.subList(0, coinoneOrderbook.askes.size() < ORDERBOOK_CNT ? coinoneOrderbook.askes.size() : ORDERBOOK_CNT));
        coinoneOrderbook.bides = new ArrayList<>(coinoneOrderbook.bides.subList(0, coinoneOrderbook.bides.size() < ORDERBOOK_CNT ? coinoneOrderbook.bides.size() : ORDERBOOK_CNT));
        PrefUtil.saveOrderbook(mContext, mCoinType, new Gson().toJson(coinoneOrderbook));
    }

    private void saveCoinoneTrade(CoinoneTrade coinoneTrade) {
        Collections.reverse(coinoneTrade.completeOrders);
        coinoneTrade.completeOrders = new ArrayList<>(coinoneTrade.completeOrders.subList(0, TRADE_CNT));
        PrefUtil.saveCompleteOrder(mContext, mCoinType, new Gson().toJson(coinoneTrade));
    }

    private void saveCoinoneTicker(CoinoneTicker.Ticker ticker) {
        PrefUtil.saveTicker(mContext, mCoinType, new Gson().toJson(ticker));
    }
}
