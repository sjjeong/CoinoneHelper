package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.CommonOrderbook;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public class CoinoneOrderbookRepository implements OrderbookDataSource {
    private static final int REFRESH_PERIOD = 5000;
    private static final int ORDERBOOK_CNT = 30;

    private OnOrderbookCallback mOnOrderbookCallback;
    private String mCoinType;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private Callback<CoinoneOrderbook> mOrderbookCallback = new Callback<CoinoneOrderbook>() {
        @Override
        public void onResponse(Call<CoinoneOrderbook> call, Response<CoinoneOrderbook> response) {
            CoinoneOrderbook coinoneOrderbook = response.body();
            if (coinoneOrderbook == null) {
                mOnOrderbookCallback.onOrderbookLoadFailed();
                return;
            }

            if (coinoneOrderbook.asks == null || coinoneOrderbook.bids == null) {
                mOnOrderbookCallback.onOrderbookLoadFailed();
                return;
            }

            ArrayList<CommonOrderbook> asks = new ArrayList<>();
            ArrayList<CommonOrderbook> bids = new ArrayList<>();

            coinoneOrderbook.asks = new ArrayList<>
                    (coinoneOrderbook.asks.size() >= ORDERBOOK_CNT ?
                            coinoneOrderbook.asks.subList(0, ORDERBOOK_CNT) :
                            coinoneOrderbook.asks.subList(0, coinoneOrderbook.asks.size()));
            coinoneOrderbook.bids = new ArrayList<>
                    (coinoneOrderbook.bids.size() >= ORDERBOOK_CNT ?
                            coinoneOrderbook.bids.subList(0, ORDERBOOK_CNT) :
                            coinoneOrderbook.bids.subList(0, coinoneOrderbook.bids.size()));

            for (CoinoneOrderbook.Book ask : coinoneOrderbook.asks) {
                asks.add(new CommonOrderbook(ask.price, ask.qty, true));
            }
            for (CoinoneOrderbook.Book bid : coinoneOrderbook.bids) {
                bids.add(new CommonOrderbook(bid.price, bid.qty, false));
            }
            mOnOrderbookCallback.onOrderbookLoaded(asks, bids);
        }

        @Override
        public void onFailure(Call<CoinoneOrderbook> call, Throwable t) {
            mOnOrderbookCallback.onOrderbookLoadFailed();
        }
    };

    public CoinoneOrderbookRepository(String coinType) {
        mCoinType = coinType;
    }


    @Override
    public void start() {
        requestOrderbook();
        makeTimer();
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
    public void setOnOrderbookCallback(OnOrderbookCallback onOrderbookCallback) {
        mOnOrderbookCallback = onOrderbookCallback;
    }

    private void makeTimer() {
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                requestOrderbook();
            }
        };
        mTimer.schedule(mTimerTask, REFRESH_PERIOD, REFRESH_PERIOD);
    }

    private void requestOrderbook() {
        CoinoneApiManager.CoinonePublicApi api = CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePublicApi.class);
        Call<CoinoneOrderbook> callOrderbook = api.orderbook(mCoinType);
        callOrderbook.enqueue(mOrderbookCallback);
    }
}
