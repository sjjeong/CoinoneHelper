package com.googry.coinonehelper.data.source;

import com.googry.coinonehelper.data.CommonOrder;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 27..
 */

public interface LimitOrderDataSource {

    void call();

    void setOnLimitOrderCallback(OnLimitOrderCallback onLimitOrderCallback);

    interface OnLimitOrderCallback {
        void onLimitOrderLoaded(List<CommonOrder> asks, List<CommonOrder> bids);

        void onLimitOrderLoadFailed(int errorCode);
    }

}
