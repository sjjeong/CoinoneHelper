package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.widget.Toast;

import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.source.CoinoneLimitOrderbookRepository;
import com.googry.coinonehelper.data.source.LimitOrderDataSource;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class AskBidViewModel implements LimitOrderDataSource.OnLimitOrderCallback {
    public final ObservableField<String> amount = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
    public final ObservableList<CommonOrder> limitOrderAsks = new ObservableArrayList<>();
    public final ObservableList<CommonOrder> limitOrderBids = new ObservableArrayList<>();

    private String mCoinName;
    private CoinType mCoinType;
    private final Context mContext;
    private LimitOrderDataSource mLimitOrderDataSource;

    public AskBidViewModel(Context context, String coinName, String price) {
        mCoinName = coinName;
        mCoinType = CoinType.getCoinTypeFromTitle(mCoinName);
        this.price.set(price);
        mContext = context;
        mLimitOrderDataSource = new CoinoneLimitOrderbookRepository(context, coinName);
        mLimitOrderDataSource.setOnLimitOrderCallback(this);
        mLimitOrderDataSource.call();
    }

    public void call() {
        mLimitOrderDataSource.call();
    }

    // databinding
    public void onMaxAmountClick() {

    }

    // databinding
    public void onUpPriceClick() {
        long _price = Long.parseLong(price.get());
        price.set(String.valueOf(_price + CoinType.getCoinDivider(mCoinType)));
    }

    // databinding
    public void onDownPriceClick() {
        long _price = Long.parseLong(price.get());
        price.set(String.valueOf(_price - CoinType.getCoinDivider(mCoinType)));
    }

    // databinding
    public void onDoOrderClick() {

    }

    @Override
    public void onLimitOrderLoaded(List<CommonOrder> asks, List<CommonOrder> bids) {
        limitOrderAsks.clear();
        limitOrderAsks.addAll(asks);
        limitOrderBids.clear();
        limitOrderBids.addAll(bids);
    }

    @Override
    public void onLimitOrderLoadFailed(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
