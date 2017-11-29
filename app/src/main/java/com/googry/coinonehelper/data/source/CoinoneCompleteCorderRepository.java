package com.googry.coinonehelper.data.source;

import android.content.Context;
import android.widget.Toast;

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
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        CoinonePrivateError error =
                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                        CoinonePrivateError.class);
                        mOnCompleteOrderCallback.onCompleteOrderLoadFailed(error.errorMsg);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                CoinoneCompleteOrder coinoneCompleteOrder = response.body();
                if (coinoneCompleteOrder == null) {
                    mOnCompleteOrderCallback.onCompleteOrderLoadFailed(mContext.getString(R.string.server_error));
                    return;
                }

                ArrayList<CommonOrder> orders = new ArrayList<>();

                if (coinoneCompleteOrder.errorCode == 0) {
                    for (CoinoneCompleteOrder.Order completeOrder : coinoneCompleteOrder.completeOrders) {
                        orders.add(new CommonOrder(completeOrder.index,
                                completeOrder.price,
                                completeOrder.qty,
                                completeOrder.type.equals("ask")));
                    }
                    mOnCompleteOrderCallback.onCompleteOrderLoaded(orders);
                    return;
                }
                mOnCompleteOrderCallback.onCompleteOrderLoadFailed(
                        CoinoneErrorCodeUtil.getErrorMsgWithErrorCode(coinoneCompleteOrder.errorCode));
            }

            @Override
            public void onFailure(Call<CoinoneCompleteOrder> call, Throwable t) {
                mOnCompleteOrderCallback.onCompleteOrderLoadFailed(mContext.getString(R.string.server_error));
            }
        });
    }

    @Override
    public void setOnCompleteOrderCallback(OnCompleteOrderCallback onCompleteOrderCallback) {
        mOnCompleteOrderCallback = onCompleteOrderCallback;
    }
}
