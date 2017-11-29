package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.source.CoinoneCompleteCorderRepository;
import com.googry.coinonehelper.data.source.CompleteOrderDataSource;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class ConclusionHistoryViewModel implements CompleteOrderDataSource.OnCompleteOrderCallback {
    public final ObservableList<CommonOrder> conclusionHistories = new ObservableArrayList<>();


    private final String mCoinName;
    private final Context mContext;
    private CompleteOrderDataSource mCompleteOrderDataSource;

    public ConclusionHistoryViewModel(Context context, String coinName) {
        mCoinName = coinName;
        mContext = context;
        mCompleteOrderDataSource = new CoinoneCompleteCorderRepository(context, coinName);
        mCompleteOrderDataSource.setOnCompleteOrderCallback(this);
        mCompleteOrderDataSource.call();
    }

    public void call() {
        mCompleteOrderDataSource.call();
    }

    @Override
    public void onCompleteOrderLoaded(List<CommonOrder> completeOrders) {
        conclusionHistories.clear();
        conclusionHistories.addAll(completeOrders);
    }

    @Override
    public void onCompleteOrderLoadFailed(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
