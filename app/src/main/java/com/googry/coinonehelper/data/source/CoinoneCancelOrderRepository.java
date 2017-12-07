package com.googry.coinonehelper.data.source;

import android.content.Context;

import com.google.gson.Gson;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 12. 2..
 */

public class CoinoneCancelOrderRepository implements CancelOrderDataSource {

    private OnCancelOrderCallback mOnCancelOrderCallback;
    private Context mContext;
    private String mCoinName;

    public CoinoneCancelOrderRepository(Context context, String coinName) {
        mContext = context;
        this.mCoinName = coinName;
    }

    @Override
    public void call(final CommonOrder commonOrder) {
        Call<CoinonePrivateError> call = CoinonePrivateApiUtil.getCancelOrder(mContext, commonOrder, mCoinName);
        call.enqueue(new Callback<CoinonePrivateError>() {
            @Override
            public void onResponse(Call<CoinonePrivateError> call, Response<CoinonePrivateError> response) {
                if (mOnCancelOrderCallback == null) {
                    return;
                }
                if (response.errorBody() != null) {
                    try {
                        String errorJson = response.errorBody().string();
                        CoinonePrivateError error =
                                new Gson().fromJson(CoinoneErrorCodeUtil.replaceBadQuotes(errorJson),
                                        CoinonePrivateError.class);
                        mOnCancelOrderCallback.onCancelFailed(error.errorCode);
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (response.body() != null) {
                    CoinonePrivateError error = response.body();
                    if (error.errorCode == 0) {
                        mOnCancelOrderCallback.onCancelSuccess(commonOrder);
                        return;
                    } else {
                        mOnCancelOrderCallback.onCancelFailed(error.errorCode);
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<CoinonePrivateError> call, Throwable t) {
                if (mOnCancelOrderCallback == null) {
                    return;
                }
                mOnCancelOrderCallback.onCancelFailed(405);
            }
        });
    }

    @Override
    public void setOnCancelOrderCallback(OnCancelOrderCallback onCancelOrderCallback) {
        mOnCancelOrderCallback = onCancelOrderCallback;
    }
}
