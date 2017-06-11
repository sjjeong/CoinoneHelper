package com.googry.coinonehelper.ui.main.orderbook;

import android.util.Log;
import android.view.View;

import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.remote.ApiManager;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookPresenter implements OrderbookContract.Presenter {
    private OrderbookContract.View mView;
    private String mCoinType;

    private Timer mTimer;
    private TimerTask mTimerTask;

    public OrderbookPresenter(OrderbookContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        requestOrderbook(mCoinType);
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                requestOrderbook(mCoinType);
            }
        };
        mTimer.schedule(mTimerTask, 1000, 1000);

    }

    @Override
    public void setCoinType(String coinType) {
        mCoinType = coinType;
    }

    @Override
    public void stop() {
        if(mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void requestOrderbook(String coinType) {
        ApiManager.PublicApi api = ApiManager.getApiManager().create(ApiManager.PublicApi.class);
        Call<CoinoneOrderbook> call = api.orderbook(coinType);
        call.enqueue(callback);
    }

    private Callback<CoinoneOrderbook> callback = new Callback<CoinoneOrderbook>() {
        @Override
        public void onResponse(Call<CoinoneOrderbook> call, Response<CoinoneOrderbook> response) {
            CoinoneOrderbook coinoneOrderbook = response.body();
            mView.showOrderbookList(coinoneOrderbook.askes,
                    coinoneOrderbook.bides);
            mView.hideProgressDialog();
        }

        @Override
        public void onFailure(Call<CoinoneOrderbook> call, Throwable t) {

        }
    };
}
