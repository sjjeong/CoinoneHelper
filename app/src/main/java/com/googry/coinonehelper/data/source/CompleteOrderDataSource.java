package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CommonOrder;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 28..
 */

public interface CompleteOrderDataSource {

    void call();

    void setOnCompleteOrderCallback(OnCompleteOrderCallback onCompleteOrderCallback);

    interface OnCompleteOrderCallback {
        void onCompleteOrderLoaded(List<CommonOrder> completeOrders);

        void onCompleteOrderLoadFailed(int errorCode);
    }
}
