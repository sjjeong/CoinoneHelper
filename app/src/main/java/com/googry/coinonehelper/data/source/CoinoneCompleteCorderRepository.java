package com.googry.coinonehelper.data.source;

import android.content.Context;

import com.googry.coinonehelper.data.CoinoneCompleteOrder;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 11. 28..
 */

public class CoinoneCompleteCorderRepository implements CompleteOrderDataSource {
    private OnCompleteOrderCallback mOnCompleteOrderCallback;

    private Context mContext;

    private String mCoinName;

    public CoinoneCompleteCorderRepository(Context context, String coinName) {
        mContext = context;
        mCoinName = coinName;
    }

    @Override
    public void call() {
        Call<CoinoneCompleteOrder> completeOrderCall = CoinonePrivateApiUtil.getCompleteOrder(mContext, mCoinName);
        completeOrderCall.enqueue(new Callback<CoinoneCompleteOrder>() {
            @Override
            public void onResponse(Call<CoinoneCompleteOrder> call, Response<CoinoneCompleteOrder> response) {
                CoinoneCompleteOrder coinoneCompleteOrder = response.body();
                if (coinoneCompleteOrder == null) {
                    mOnCompleteOrderCallback.onCompleteOrderLoadFailed("null");
                    return;
                }

                ArrayList<CommonOrder> orders = new ArrayList<>();

                if (coinoneCompleteOrder.errorCode.equals("0")) {
                    for (CoinoneCompleteOrder.Order completeOrder : coinoneCompleteOrder.completeOrders) {
                        orders.add(new CommonOrder(completeOrder.index,
                                completeOrder.price,
                                completeOrder.qty,
                                completeOrder.type.equals("ask")));
                    }
                    mOnCompleteOrderCallback.onCompleteOrderLoaded(orders);
                    return;
                }
                mOnCompleteOrderCallback.onCompleteOrderLoadFailed(coinoneCompleteOrder.errorCode);
            }

            @Override
            public void onFailure(Call<CoinoneCompleteOrder> call, Throwable t) {
                mOnCompleteOrderCallback.onCompleteOrderLoadFailed("404");
            }
        });
    }

    @Override
    public void setOnCompleteOrderCallback(OnCompleteOrderCallback onCompleteOrderCallback) {
        mOnCompleteOrderCallback = onCompleteOrderCallback;
    }
}
