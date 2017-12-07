package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CommonOrder;

/**
 * Created by seokjunjeong on 2017. 12. 2..
 */

public interface CancelOrderDataSource {

    void call(CommonOrder commonOrder);

    void setOnCancelOrderCallback(OnCancelOrderCallback onCancelOrderCallback);

    interface OnCancelOrderCallback{
        void onCancelSuccess(CommonOrder commonOrder);

        void onCancelFailed(int errorCode);
    }
}
