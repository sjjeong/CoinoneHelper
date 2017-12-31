package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.MarketAccount;
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
    private OnTradeEventListener mOnTradeEventListener;

    public ConclusionHistoryViewModel(Context context, String coinName,
                                      OnTradeEventListener onTradeEventListener,
                                      MarketAccount account) {
        mCoinName = coinName;
        mContext = context;
        mCompleteOrderDataSource = new CoinoneCompleteOrderRepository(context, coinName, account);
        mCompleteOrderDataSource.setOnCompleteOrderCallback(this);

        mOnTradeEventListener = onTradeEventListener;
        call();
    }

    public void call() {
        mOnTradeEventListener.onCallRequest();
        mCompleteOrderDataSource.call();
    }

    @Override
    public void onCompleteOrderLoaded(List<CommonOrder> completeOrders) {
        conclusionHistories.clear();
        conclusionHistories.addAll(completeOrders);
        mOnTradeEventListener.onLoadFinish();
    }

    @Override
    public void onCompleteOrderLoadFailed(int errorCode) {
        CoinoneErrorCodeUtil.handleErrorCode(mContext, errorCode);
        mOnTradeEventListener.onLoadFinish();
    }

}
