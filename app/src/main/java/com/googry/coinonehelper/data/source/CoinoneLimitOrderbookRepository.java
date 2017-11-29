package com.googry.coinonehelper.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinoneLimitOrder;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CommonOrder;
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

    public CoinoneLimitOrderbookRepository(Context context, String coinName) {
        mContext = context;
        mCoinName = coinName;
    }

    @Override
    public void call() {
        Call<CoinoneLimitOrder> limitOrderCall = CoinonePrivateApiUtil.getLimitOrder(mContext, mCoinName);
        limitOrderCall.enqueue(new Callback<CoinoneLimitOrder>() {
            @Override
            public void onResponse(Call<CoinoneLimitOrder> call, Response<CoinoneLimitOrder> response) {
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        CoinonePrivateError error =
                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                        CoinonePrivateError.class);
                        mOnLimitOrderCallback.onLimitOrderLoadFailed(error.errorMsg);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                CoinoneLimitOrder limitOrder = response.body();
                if (limitOrder == null) {
                    mOnLimitOrderCallback.onLimitOrderLoadFailed(mContext.getString(R.string.server_error));
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
                                    coinoneLimitOrder.price,
                                    coinoneLimitOrder.qty,
                                    true)
                            );
                        } else {
                            bids.add(new CommonOrder(
                                    coinoneLimitOrder.index,
                                    coinoneLimitOrder.price,
                                    coinoneLimitOrder.qty,
                                    false)
                            );

                        }
                    }

                    LogUtil.e(asks.size() + ", " + bids.size());

                    mOnLimitOrderCallback.onLimitOrderLoaded(asks, bids);

                    return;
                }
                mOnLimitOrderCallback.onLimitOrderLoadFailed(
                        CoinoneErrorCodeUtil.getErrorMsgWithErrorCode(limitOrder.errorCode));
            }

            @Override
            public void onFailure(Call<CoinoneLimitOrder> call, Throwable t) {
                mOnLimitOrderCallback.onLimitOrderLoadFailed(mContext.getString(R.string.server_error));
            }
        });
    }

    @Override
    public void setOnLimitOrderCallback(OnLimitOrderCallback onLimitOrderCallback) {
        mOnLimitOrderCallback = onLimitOrderCallback;
    }
}
