package com.googry.coinonehelper.ui.main.orderbook;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.KorbitOrderbook;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.KorbitTrade;
import com.googry.coinonehelper.data.remote.KorbitApiManager;
import com.googry.coinonehelper.util.PrefUtil;

import java.util.ArrayList;
import java.util.List;
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
    private Callback<KorbitOrderbook> mOrderbookCallback = new Callback<KorbitOrderbook>() {
        @Override
        public void onResponse(Call<KorbitOrderbook> call, Response<KorbitOrderbook> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            saveCoinoneOrderbook(response.body());
            loadCoinoneOrderbook();
        }

        @Override
        public void onFailure(Call<KorbitOrderbook> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };
    private Callback<List<KorbitTrade>> mTradeCallback = new Callback<List<KorbitTrade>>() {
        @Override
        public void onResponse(Call<List<KorbitTrade>> call, Response<List<KorbitTrade>> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            saveCoinoneTrade(response.body());
            loadCoinoneTrade();
        }


        @Override
        public void onFailure(Call<List<KorbitTrade>> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    private Callback<KorbitTicker.TickerDetailed> mTickerCallback = new Callback<KorbitTicker.TickerDetailed>() {
        @Override
        public void onResponse(Call<KorbitTicker.TickerDetailed> call, Response<KorbitTicker.TickerDetailed> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            saveCoinoneTicker(response.body());
            loadCoinoneTicker();
        }

        @Override
        public void onFailure(Call<KorbitTicker.TickerDetailed> call, Throwable t) {
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
        mView.showOrderbookList(new Gson().fromJson(PrefUtil.loadOrderbook(mContext, mCoinType), KorbitOrderbook.class));
    }

    @Override
    public void loadCoinoneTrade() {
        mView.showTradeList((List<KorbitTrade>) new Gson().fromJson(PrefUtil.loadCompleteOrder(mContext, mCoinType), new TypeToken<List<KorbitTrade>>() {
        }.getType()));
    }

    @Override
    public void loadCoinoneTicker() {
        mView.showTicker(new Gson().fromJson(PrefUtil.loadTicker(mContext, mCoinType), KorbitTicker.TickerDetailed.class));
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
        KorbitApiManager.KorbitPublicApi korbiApi = KorbitApiManager.getApiManager().create(KorbitApiManager.KorbitPublicApi.class);
        Call<KorbitOrderbook> callOrderbook = korbiApi.orderbook(coinType.name().toLowerCase() + "_krw");
        callOrderbook.enqueue(mOrderbookCallback);
        Call<List<KorbitTrade>> callTrade = korbiApi.trades(coinType.name().toLowerCase() + "_krw", "hour");
        callTrade.enqueue(mTradeCallback);
        Call<KorbitTicker.TickerDetailed> callTicker = korbiApi.ticker(coinType.name().toLowerCase() + "_krw");
        callTicker.enqueue(mTickerCallback);

    }

    private void saveCoinoneOrderbook(KorbitOrderbook korbitOrderbook) {
        if (korbitOrderbook.asks != null) {
            korbitOrderbook.asks = new ArrayList<>(korbitOrderbook.asks.subList(0, korbitOrderbook.asks.size() < ORDERBOOK_CNT ? korbitOrderbook.asks.size() : ORDERBOOK_CNT));
        }
        if (korbitOrderbook.bids != null) {
            korbitOrderbook.bids = new ArrayList<>(korbitOrderbook.bids.subList(0, korbitOrderbook.bids.size() < ORDERBOOK_CNT ? korbitOrderbook.bids.size() : ORDERBOOK_CNT));
        }
        PrefUtil.saveOrderbook(mContext, mCoinType, new Gson().toJson(korbitOrderbook));
    }

    private void saveCoinoneTrade(List<KorbitTrade> korbitTrade) {
        if (korbitTrade != null) {
            korbitTrade = new ArrayList<>(korbitTrade.subList(0, korbitTrade.size() < TRADE_CNT ? korbitTrade.size() : TRADE_CNT));
        }
        PrefUtil.saveCompleteOrder(mContext, mCoinType, new Gson().toJson(korbitTrade));

    }

    private void saveCoinoneTicker(KorbitTicker.TickerDetailed ticker) {
        PrefUtil.saveTicker(mContext, mCoinType, new Gson().toJson(ticker));
    }
}
