package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.widget.Toast;

import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.source.CancelOrderDataSource;
import com.googry.coinonehelper.data.source.CoinoneCancelOrderRepository;
import com.googry.coinonehelper.data.source.CoinoneLimitOrderbookRepository;
import com.googry.coinonehelper.data.source.LimitOrderDataSource;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class AskBidViewModel implements LimitOrderDataSource.OnLimitOrderCallback,
        CancelOrderDataSource.OnCancelOrderCallback {
    public final ObservableField<String> amount = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
    public final ObservableField<String> orderPrice = new ObservableField<>("0");
    public final ObservableList<CommonOrder> limitOrderAsks = new ObservableArrayList<>();
    public final ObservableList<CommonOrder> limitOrderBids = new ObservableArrayList<>();

    private final Context mContext;
    private final String mCoinName;
    private final CoinType mCoinType;

    private LimitOrderDataSource mLimitOrderDataSource;
    private CancelOrderDataSource mCancelOrderDataSource;

    private Observable.OnPropertyChangedCallback mAmountCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            String _amount = amount.get();
            if (!TextUtils.isEmpty(_amount)) {
                double __amount = Double.parseDouble(_amount);
                if (__amount - (Math.floor(__amount * 10000)) / 10000 > 0) {
                    amount.set(String.format("%.4f", Double.parseDouble(_amount)));
                }
            }
            setOrderPrice();
        }
    };

    private Observable.OnPropertyChangedCallback mPriceCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            String _price = price.get();
            if (!TextUtils.isEmpty(_price)) {
                long __price = Long.parseLong(_price);
                if (__price < 0) {
                    price.set("0");
                }
            }
            setOrderPrice();
        }
    };

    public AskBidViewModel(Context context, String coinName, String price) {
        mCoinName = coinName;
        mCoinType = CoinType.getCoinTypeFromTitle(mCoinName);
        this.price.set(price);
        mContext = context;
        mLimitOrderDataSource = new CoinoneLimitOrderbookRepository(context, coinName);
        mLimitOrderDataSource.setOnLimitOrderCallback(this);
        mLimitOrderDataSource.call();
        mCancelOrderDataSource = new CoinoneCancelOrderRepository(context, coinName);
        mCancelOrderDataSource.setOnCancelOrderCallback(this);

        this.amount.addOnPropertyChangedCallback(mAmountCallback);
        this.price.addOnPropertyChangedCallback(mPriceCallback);
    }

    public void call() {
        mLimitOrderDataSource.call();
    }

    // databinding
    public void onUpPriceClick() {
        long _price = Long.parseLong(TextUtils.isEmpty(price.get()) ? "0" : price.get()) + CoinType.getCoinDivider(mCoinType);
        price.set(String.valueOf(_price));
    }

    // databinding
    public void onDownPriceClick() {
        long _price = Long.parseLong(TextUtils.isEmpty(price.get()) ? "0" : price.get()) - CoinType.getCoinDivider(mCoinType);
        price.set(String.valueOf(_price < 0 ? 0 : _price));
    }

    // databinding
    public void onDoOrderClick(boolean isAsk) {

    }

    // databinding
    public void onCancelOrderClick(final CommonOrder order) {
        mCancelOrderDataSource.call(order);
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

    private void setOrderPrice() {
        String _amount = this.amount.get();
        String _price = this.price.get();
        if (!TextUtils.isEmpty(_amount) && !TextUtils.isEmpty(_price)) {
            long _orderPrice = (long) (Long.parseLong(_price) * Double.parseDouble(_amount));
            this.orderPrice.set(String.format("%,d", _orderPrice));
        } else {
            this.orderPrice.set("0");
        }
    }

    @Override
    public void onCancelSuccess(CommonOrder commonOrder) {
        if (commonOrder.isAsk) {
            limitOrderAsks.remove(commonOrder);
        } else {
            limitOrderBids.remove(commonOrder);
        }
    }

    @Override
    public void onCancelFailed(String errorMsg) {
        Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
    }
}
