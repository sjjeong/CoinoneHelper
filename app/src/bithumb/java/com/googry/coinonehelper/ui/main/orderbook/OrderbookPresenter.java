package com.googry.coinonehelper.ui.main.orderbook;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.data.BithumbOrderbook;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.BithumbTrade;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.remote.BithumbApiManager;
import com.googry.coinonehelper.util.LogUtil;
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
    private Callback<BithumbOrderbook> mOrderbookCallback = new Callback<BithumbOrderbook>() {
        @Override
        public void onResponse(Call<BithumbOrderbook> call, Response<BithumbOrderbook> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            saveCoinoneOrderbook(response.body());
            loadCoinoneOrderbook();
        }

        @Override
        public void onFailure(Call<BithumbOrderbook> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };
    private Callback<BithumbTrade> mTradeCallback = new Callback<BithumbTrade>() {
        @Override
        public void onResponse(Call<BithumbTrade> call, Response<BithumbTrade> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            saveCoinoneTrade(response.body());
            loadCoinoneTrade();
        }


        @Override
        public void onFailure(Call<BithumbTrade> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    private Callback<BithumbSoloTicker.Ticker> mTickerCallback = new Callback<BithumbSoloTicker.Ticker>() {
        @Override
        public void onResponse(Call<BithumbSoloTicker.Ticker> call, Response<BithumbSoloTicker.Ticker> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            BithumbSoloTicker.Ticker ticker = response.body();
            LogUtil.i(mCoinType.name() + " : " +ticker.closingPrice);
            saveCoinoneTicker(response.body());
            loadCoinoneTicker();
        }

        @Override
        public void onFailure(Call<BithumbSoloTicker.Ticker> call, Throwable t) {
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
        mView.showOrderbookList(new Gson().fromJson(PrefUtil.loadOrderbook(mContext, mCoinType), BithumbOrderbook.class));
    }

    @Override
    public void loadCoinoneTrade() {
        mView.showTradeList(new Gson().fromJson(PrefUtil.loadCompleteOrder(mContext, mCoinType), BithumbTrade.class));
    }

    @Override
    public void loadCoinoneTicker() {
        mView.showTicker(new Gson().fromJson(PrefUtil.loadTicker(mContext, mCoinType), BithumbSoloTicker.Ticker.class));
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
        BithumbApiManager.BithumbPublicApi bithumbApi = BithumbApiManager.getApiManager().create(BithumbApiManager.BithumbPublicApi.class);
        Call<BithumbOrderbook> callOrderbook = bithumbApi.orderbook(coinType.name().toLowerCase());
        callOrderbook.enqueue(mOrderbookCallback);
        Call<BithumbTrade> callTrade = bithumbApi.trades(coinType.name().toLowerCase());
        callTrade.enqueue(mTradeCallback);
        Call<BithumbSoloTicker.Ticker> callTicker = bithumbApi.ticker(coinType.name().toLowerCase());
        callTicker.enqueue(mTickerCallback);

    }

    private void saveCoinoneOrderbook(BithumbOrderbook korbitOrderbook) {
        if (korbitOrderbook.data.asks != null) {
            korbitOrderbook.data.asks = new ArrayList<>(korbitOrderbook.data.asks.subList(0, korbitOrderbook.data.asks.size() < ORDERBOOK_CNT ? korbitOrderbook.data.asks.size() : ORDERBOOK_CNT));
        }
        if (korbitOrderbook.data.bids != null) {
            korbitOrderbook.data.bids = new ArrayList<>(korbitOrderbook.data.bids.subList(0, korbitOrderbook.data.bids.size() < ORDERBOOK_CNT ? korbitOrderbook.data.bids.size() : ORDERBOOK_CNT));
        }
        PrefUtil.saveOrderbook(mContext, mCoinType, new Gson().toJson(korbitOrderbook));
    }

    private void saveCoinoneTrade(BithumbTrade bithumbTrade) {
        if (bithumbTrade.completeOrders != null) {
            Collections.reverse(bithumbTrade.completeOrders);
            bithumbTrade.completeOrders = new ArrayList<>(bithumbTrade.completeOrders.subList(0, bithumbTrade.completeOrders.size() < TRADE_CNT ? bithumbTrade.completeOrders.size() : TRADE_CNT));
        }
        PrefUtil.saveCompleteOrder(mContext, mCoinType, new Gson().toJson(bithumbTrade));

    }

    private void saveCoinoneTicker(BithumbSoloTicker.Ticker ticker) {
        PrefUtil.saveTicker(mContext, mCoinType, new Gson().toJson(ticker));
    }
}
