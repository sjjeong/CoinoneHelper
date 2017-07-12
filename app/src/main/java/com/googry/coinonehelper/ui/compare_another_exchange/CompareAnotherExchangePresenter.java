package com.googry.coinonehelper.ui.compare_another_exchange;

import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.TradeSite;
import com.googry.coinonehelper.data.remote.BithumbApiManager;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class CompareAnotherExchangePresenter implements CompareAnotherExchangeContract.Presenter {
    private static final int COINONE = 0;
    private static final int BITHUMB = 1;
    private CompareAnotherExchangeContract.View mView;
    private boolean mAllLoad[] = new boolean[TradeSite.values().length];


    public CompareAnotherExchangePresenter(CompareAnotherExchangeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTicker();
    }

    @Override
    public void loadTicker() {
        loadCoinoneTicker();
        loadBithumbTicker();
    }

    private void loadCoinoneTicker() {
        mAllLoad[COINONE] = false;
        CoinoneApiManager.CoinonePublicApi api = CoinoneApiManager.getApiManager()
                .create(CoinoneApiManager.CoinonePublicApi.class);
        Call<CoinoneTicker> call = api.allTicker();
        call.enqueue(new Callback<CoinoneTicker>() {
            @Override
            public void onResponse(Call<CoinoneTicker> call, Response<CoinoneTicker> response) {
                mView.showCoinoneTicker(response.body());
                mAllLoad[COINONE] = true;
                isAllLoad();
            }

            @Override
            public void onFailure(Call<CoinoneTicker> call, Throwable t) {

            }
        });

    }

    private void loadBithumbTicker(){
        mAllLoad[BITHUMB] = false;
        BithumbApiManager.BithumbPublicApi api = BithumbApiManager.getApiManager()
                .create(BithumbApiManager.BithumbPublicApi.class);
        Call<BithumbTicker> call = api.allTicker();
        call.enqueue(new Callback<BithumbTicker>() {
            @Override
            public void onResponse(Call<BithumbTicker> call, Response<BithumbTicker> response) {
                mView.showBithumbTicker(response.body());
                mAllLoad[BITHUMB] = true;
                isAllLoad();
            }

            @Override
            public void onFailure(Call<BithumbTicker> call, Throwable t) {

            }
        });
    }

    private void isAllLoad() {
        for(boolean bool:mAllLoad){
            if(!bool)
                return;
        }
        mView.hideProgress();

    }
}
