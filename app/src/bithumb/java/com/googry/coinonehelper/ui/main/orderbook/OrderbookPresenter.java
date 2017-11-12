package com.googry.coinonehelper.ui.main.orderbook;

import com.googry.coinonehelper.data.BithumbOrderbook;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.BithumbTrade;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.remote.BithumbApiManager;
import com.googry.coinonehelper.util.LogUtil;

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
    private BithumbApiManager.BithumbPublicApi mBithumbApi;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private int mDelay, mCallDuration;
    private Callback<BithumbOrderbook> mOrderbookCallback = new Callback<BithumbOrderbook>() {
        @Override
        public void onResponse(Call<BithumbOrderbook> call, Response<BithumbOrderbook> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            mView.showOrderbookList(response.body());
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
            mView.showTradeList(response.body());
        }


        @Override
        public void onFailure(Call<BithumbTrade> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    private Callback<BithumbSoloTicker> mTickerCallback = new Callback<BithumbSoloTicker>() {
        @Override
        public void onResponse(Call<BithumbSoloTicker> call, Response<BithumbSoloTicker> response) {
            mView.hideCoinoneServerDownProgressDialog();
            if (response.body() == null) return;
            mView.showTicker(response.body().ticker);
        }

        @Override
        public void onFailure(Call<BithumbSoloTicker> call, Throwable t) {
            mView.showCoinoneServerDownProgressDialog();

        }
    };

    public OrderbookPresenter(OrderbookContract.View view) {
        mView = view;
        mView.setPresenter(this);
        mBithumbApi = BithumbApiManager.getApiManager().create(BithumbApiManager.BithumbPublicApi.class);
    }

    @Override
    public void start() {
    }

    @Override
    public void setCoinType(CoinType coinType) {
        mCoinType = coinType;
    }

    @Override
    public void setDelayCallDuration(int delay, int callDuration) {
        mDelay = delay;
        mCallDuration = callDuration;
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
//        requestOrderbook(mCoinType);
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
        LogUtil.e(mCoinType.name() + " delay : " + mDelay + ", callDuration : " + mCallDuration);
        mTimer.schedule(mTimerTask, mDelay, mCallDuration);
    }

    private void requestOrderbook(CoinType coinType) {
        Call<BithumbOrderbook> callOrderbook = mBithumbApi.orderbook(coinType.name().toLowerCase());
        callOrderbook.enqueue(mOrderbookCallback);
        Call<BithumbTrade> callTrade = mBithumbApi.trades(coinType.name().toLowerCase());
        callTrade.enqueue(mTradeCallback);
        Call<BithumbSoloTicker> callTicker = mBithumbApi.ticker(coinType.name().toLowerCase());
        callTicker.enqueue(mTickerCallback);

    }

}
