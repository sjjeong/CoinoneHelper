package com.googry.coinonehelper.ui.main.my_assets;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneBalance;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public class MyAssetsPresenter implements MyAssetsContract.Presenter {
    private MyAssetsContract.View mView;

    private Context mContext;

    private MarketAccount mAccount;

    public MyAssetsPresenter(MyAssetsContract.View view,
                             Context context,
                             MarketAccount account) {
        mView = view;
        mView.setPresenter(this);
        mContext = context;
        mAccount = account;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadBalance() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Call<CoinoneTicker> coinoneTickerCall = CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePublicApi.class).allTicker();
                coinoneTickerCall.enqueue(new Callback<CoinoneTicker>() {
                    @Override
                    public void onResponse(Call<CoinoneTicker> call, Response<CoinoneTicker> response) {
                        CoinoneTicker ticker = response.body();
                        if (ticker == null) {
                            mView.hideLoadingDialog();
                            Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Gson gson = new Gson();
                        PrefUtil.saveTicker(mContext, CoinType.BTC, gson.toJson(ticker.btc, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.BCH, gson.toJson(ticker.bch, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.ETH, gson.toJson(ticker.eth, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.ETC, gson.toJson(ticker.etc, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.XRP, gson.toJson(ticker.xrp, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.QTUM, gson.toJson(ticker.qtum, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.LTC, gson.toJson(ticker.ltc, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.IOTA, gson.toJson(ticker.iota, CoinoneTicker.Ticker.class));
                        PrefUtil.saveTicker(mContext, CoinType.BTG, gson.toJson(ticker.btg, CoinoneTicker.Ticker.class));

                        Call<CoinoneBalance> coinoneBalanceCall = CoinonePrivateApiUtil.getBalance(mAccount);
                        coinoneBalanceCall.enqueue(new Callback<CoinoneBalance>() {
                            @Override
                            public void onResponse(Call<CoinoneBalance> call, Response<CoinoneBalance> response) {
                                mView.hideLoadingDialog();

                                if (response.errorBody() != null) {
                                    try {
                                        String errorJson = response.errorBody().string();
                                        CoinonePrivateError error =
                                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                                        CoinonePrivateError.class);
                                        CoinoneErrorCodeUtil.handleErrorCode(mContext, error.errorCode);
                                        return;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                                CoinoneBalance balance = response.body();
                                if (balance == null) {
                                    Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (balance.errorCode == 0) {
                                    mView.showBalance(balance);
                                    return;
                                }
                                CoinoneErrorCodeUtil.handleErrorCode(mContext, balance.errorCode);
                            }

                            @Override
                            public void onFailure(Call<CoinoneBalance> call, Throwable t) {
                                mView.hideLoadingDialog();
                                CoinoneErrorCodeUtil.handleErrorCode(mContext, 405);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<CoinoneTicker> call, Throwable t) {
                        CoinoneErrorCodeUtil.handleErrorCode(mContext, 405);
                    }
                });

            }
        });

    }

    @Override
    public void checkRegisterAccount() {
        if (mAccount != null) {
            mView.showLoadingDialog();
            loadBalance();
        } else {
            mView.showSettingUi();
        }
    }
}
