package com.googry.coinonehelper.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.data.CoinoneLimitOrder;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 12. 3..
 */

public class CoinoneBuySellOrderRepository implements BuySellOrderDataSource {

    private OnBuySellOrderCallback mOnBuySellOrderCallback;

    private Context mContext;

    private String mCoinName;

    private MarketAccount mAccount;

    public CoinoneBuySellOrderRepository(Context context, String coinName, MarketAccount account) {
        mContext = context;
        mCoinName = coinName;
        mAccount = account;
    }

    @Override
    public void call(final boolean isAsk, final long price, final double amount) {
        Call<CoinoneLimitOrder.Order> call =
                CoinonePrivateApiUtil.getBuySellOrder(mAccount, mCoinName, price, amount, isAsk);
        call.enqueue(new Callback<CoinoneLimitOrder.Order>() {
            @Override
            public void onResponse(Call<CoinoneLimitOrder.Order> call, Response<CoinoneLimitOrder.Order> response) {
                if (mOnBuySellOrderCallback == null) {
                    return;
                }
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        CoinonePrivateError error =
                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                        CoinonePrivateError.class);
                        mOnBuySellOrderCallback.onBuySellOrderFailed(error.errorCode);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.body() != null) {
                    CoinoneLimitOrder.Order order = response.body();
                    if (order.errorCode == 0) {
                        CommonOrder commonOrder =
                                new CommonOrder(-1, System.currentTimeMillis(),
                                        price, amount, order.orderId, isAsk);
                        mOnBuySellOrderCallback.onBuySellOrderSuccess(commonOrder);
                        return;
                    } else {
                        mOnBuySellOrderCallback.onBuySellOrderFailed(order.errorCode);
                        return;
                    }

                }
            }

            @Override
            public void onFailure(Call<CoinoneLimitOrder.Order> call, Throwable t) {
                if (mOnBuySellOrderCallback == null) {
                    return;
                }
                mOnBuySellOrderCallback.onBuySellOrderFailed(405);
            }
        });
    }

    @Override
    public void setOnBuySellOrderCallback(OnBuySellOrderCallback onBuySellOrderCallback) {
        mOnBuySellOrderCallback = onBuySellOrderCallback;

    }
}
