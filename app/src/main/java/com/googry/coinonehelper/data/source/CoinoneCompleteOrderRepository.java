package com.googry.coinonehelper.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinoneCompleteOrder;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 11. 28..
 */

public class CoinoneCompleteOrderRepository implements CompleteOrderDataSource {
    private OnCompleteOrderCallback mOnCompleteOrderCallback;

    private Context mContext;

    private String mCoinName;

    public CoinoneCompleteOrderRepository(Context context, String coinName) {
        mContext = context;
        mCoinName = coinName;
    }

    @Override
    public void call() {
        Call<CoinoneCompleteOrder> completeOrderCall = CoinonePrivateApiUtil.getCompleteOrder(mContext, mCoinName);
        completeOrderCall.enqueue(new Callback<CoinoneCompleteOrder>() {
            @Override
            public void onResponse(Call<CoinoneCompleteOrder> call, Response<CoinoneCompleteOrder> response) {
                if (mOnCompleteOrderCallback == null) {
                    return;
                }
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        CoinonePrivateError error =
                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                        CoinonePrivateError.class);
                        mOnCompleteOrderCallback.onCompleteOrderLoadFailed(error.errorCode);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                CoinoneCompleteOrder coinoneCompleteOrder = response.body();
                if (coinoneCompleteOrder == null) {
                    mOnCompleteOrderCallback.onCompleteOrderLoadFailed(405);
                    return;
                }

                ArrayList<CommonOrder> orders = new ArrayList<>();

                if (coinoneCompleteOrder.errorCode == 0) {
                    for (CoinoneCompleteOrder.Order completeOrder : coinoneCompleteOrder.completeOrders) {
                        orders.add(new CommonOrder(completeOrder.index,
                                completeOrder.timestamp * 1000,
                                completeOrder.price,
                                completeOrder.qty,
                                completeOrder.orderId,
                                completeOrder.type.equals("ask")));
                    }
                    mOnCompleteOrderCallback.onCompleteOrderLoaded(orders);
                    return;
                }
                mOnCompleteOrderCallback.onCompleteOrderLoadFailed(coinoneCompleteOrder.errorCode);
            }

            @Override
            public void onFailure(Call<CoinoneCompleteOrder> call, Throwable t) {
                mOnCompleteOrderCallback.onCompleteOrderLoadFailed(405);
            }
        });
    }

    @Override
    public void setOnCompleteOrderCallback(OnCompleteOrderCallback onCompleteOrderCallback) {
        mOnCompleteOrderCallback = onCompleteOrderCallback;
    }
}
