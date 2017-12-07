package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.source.CoinoneCompleteOrderRepository;
import com.googry.coinonehelper.data.source.CompleteOrderDataSource;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class ConclusionHistoryViewModel implements CompleteOrderDataSource.OnCompleteOrderCallback {
    public final ObservableList<CommonOrder> conclusionHistories = new ObservableArrayList<>();


    private final String mCoinName;
    private final Context mContext;
    private CompleteOrderDataSource mCompleteOrderDataSource;
    private TradeFragment.OnTradeEventListener mOnTradeEventListener;

    public ConclusionHistoryViewModel(Context context, String coinName) {
        mCoinName = coinName;
        mContext = context;
        mCompleteOrderDataSource = new CoinoneCompleteOrderRepository(context, coinName);
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
        mOnTradeEventListener.onLoadFinishListener();
    }

    @Override
    public void onCompleteOrderLoadFailed(int errorCode) {
        Toast.makeText(mContext, CoinoneErrorCodeUtil.getErrorMsgWithErrorCode(errorCode), Toast.LENGTH_SHORT).show();
        mOnTradeEventListener.onLoadFinishListener();
    }

    public void setOnTradeEventListener(TradeFragment.OnTradeEventListener onTradeEventListener) {
        mOnTradeEventListener = onTradeEventListener;
    }
}