package com.googry.coinonehelper.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinoneLimitOrder;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;
import com.googry.coinonehelper.util.LogUtil;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 11. 27..
 */

public class CoinoneLimitOrderbookRepository implements LimitOrderDataSource {

    private OnLimitOrderCallback mOnLimitOrderCallback;

    private Context mContext;

    private String mCoinName;

    private MarketAccount mAccount;

    public CoinoneLimitOrderbookRepository(Context context, String coinName, MarketAccount account) {
        mContext = context;
        mCoinName = coinName;
        mAccount = account;
    }

    @Override
    public void call() {
        Call<CoinoneLimitOrder> limitOrderCall = CoinonePrivateApiUtil.getLimitOrder(mAccount, mCoinName);
        limitOrderCall.enqueue(new Callback<CoinoneLimitOrder>() {
            @Override
            public void onResponse(Call<CoinoneLimitOrder> call, Response<CoinoneLimitOrder> response) {
                if (mOnLimitOrderCallback == null) {
                    return;
                }
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        CoinonePrivateError error =
                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                        CoinonePrivateError.class);
                        mOnLimitOrderCallback.onLimitOrderLoadFailed(error.errorCode);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                CoinoneLimitOrder limitOrder = response.body();
                if (limitOrder == null) {
                    mOnLimitOrderCallback.onLimitOrderLoadFailed(405);
                    return;
                }

                Gson gson = new Gson();
                LogUtil.e(gson.toJson(limitOrder));

                ArrayList<CommonOrder> asks = new ArrayList<>();
                ArrayList<CommonOrder> bids = new ArrayList<>();

                if (limitOrder.errorCode == 0) {
                    ArrayList<CoinoneLimitOrder.Order> coinoneLimitOrders = new ArrayList<>(limitOrder.limitOrders);

                    for (CoinoneLimitOrder.Order coinoneLimitOrder : coinoneLimitOrders) {
                        if (coinoneLimitOrder.type.equals("ask")) {
                            asks.add(new CommonOrder(
                                    coinoneLimitOrder.index,
                                    coinoneLimitOrder.timestamp,
                                    coinoneLimitOrder.price,
                                    coinoneLimitOrder.qty,
                                    coinoneLimitOrder.orderId,
                                    true)
                            );
                        } else {
                            bids.add(new CommonOrder(
                                    coinoneLimitOrder.index,
                                    coinoneLimitOrder.timestamp,
                                    coinoneLimitOrder.price,
                                    coinoneLimitOrder.qty,
                                    coinoneLimitOrder.orderId,
                                    false)
                            );

                        }
                    }

                    LogUtil.e(asks.size() + ", " + bids.size());

                    mOnLimitOrderCallback.onLimitOrderLoaded(asks, bids);

                    return;
                }
                mOnLimitOrderCallback.onLimitOrderLoadFailed(limitOrder.errorCode);
            }

            @Override
            public void onFailure(Call<CoinoneLimitOrder> call, Throwable t) {
                mOnLimitOrderCallback.onLimitOrderLoadFailed(405);
            }
        });
    }

    @Override
    public void setOnLimitOrderCallback(OnLimitOrderCallback onLimitOrderCallback) {
        mOnLimitOrderCallback = onLimitOrderCallback;
    }
}
