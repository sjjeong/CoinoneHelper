package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.text.TextUtils;
import android.widget.Toast;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.data.source.BuySellOrderDataSource;
import com.googry.coinonehelper.data.source.CancelOrderDataSource;
import com.googry.coinonehelper.data.source.CoinoneBuySellOrderRepository;
import com.googry.coinonehelper.data.source.CoinoneCancelOrderRepository;
import com.googry.coinonehelper.data.source.CoinoneLimitOrderbookRepository;
import com.googry.coinonehelper.data.source.LimitOrderDataSource;
import com.googry.coinonehelper.util.CoinoneErrorCodeUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class AskBidViewModel implements LimitOrderDataSource.OnLimitOrderCallback,
        CancelOrderDataSource.OnCancelOrderCallback, BuySellOrderDataSource.OnBuySellOrderCallback {
    public final ObservableField<String> amount = new ObservableField<>();
    public final ObservableField<String> price = new ObservableField<>();
    public final ObservableField<String> orderPrice = new ObservableField<>("0");
    public final ObservableList<CommonOrder> limitOrderAsks = new ObservableArrayList<>();
    public final ObservableList<CommonOrder> limitOrderBids = new ObservableArrayList<>();

    private final CommonOrder.AscendingPrice mAscendingPrice = new CommonOrder.AscendingPrice();
    private final CommonOrder.DescendingPrice mDescendingPrice = new CommonOrder.DescendingPrice();

    private final Context mContext;
    private final String mCoinName;
    private final CoinType mCoinType;

    private LimitOrderDataSource mLimitOrderDataSource;
    private CancelOrderDataSource mCancelOrderDataSource;
    private BuySellOrderDataSource mBuySellOrderDataSource;

    private boolean mIsAskAscending, mIsBidAscending;

    private Observable.OnPropertyChangedCallback mAmountCallback = new Observable.OnPropertyChangedCallback() {
        @Override
        public void onPropertyChanged(Observable observable, int i) {
            String _amount = amount.get();
            if (!TextUtils.isEmpty(_amount) && !_amount.startsWith(".")) {
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
    private OnTradeEventListener mOnTradeEventListener;

    public AskBidViewModel(Context context, String coinName, String price,
                           OnTradeEventListener onTradeEventListener, MarketAccount marketAccount) {
        mCoinName = coinName;
        mCoinType = CoinType.getCoinTypeFromTitle(mCoinName);
        this.price.set(price);
        mContext = context;
        mLimitOrderDataSource = new CoinoneLimitOrderbookRepository(context, coinName, marketAccount);
        mLimitOrderDataSource.setOnLimitOrderCallback(this);
        mCancelOrderDataSource = new CoinoneCancelOrderRepository(context, coinName, marketAccount);
        mCancelOrderDataSource.setOnCancelOrderCallback(this);
        mBuySellOrderDataSource = new CoinoneBuySellOrderRepository(context, coinName, marketAccount);
        mBuySellOrderDataSource.setOnBuySellOrderCallback(this);

        this.amount.addOnPropertyChangedCallback(mAmountCallback);
        this.price.addOnPropertyChangedCallback(mPriceCallback);
        mOnTradeEventListener = onTradeEventListener;

        call();
    }

    public void call() {
        mOnTradeEventListener.onCallRequest();
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
        if (TextUtils.isEmpty(price.get()) || TextUtils.isEmpty(amount.get())) {
            Toast.makeText(mContext, R.string.need_amount_price, Toast.LENGTH_SHORT).show();
            return;
        }
        mOnTradeEventListener.onCallRequest();
        mBuySellOrderDataSource.call(isAsk, Long.parseLong(price.get()), Double.parseDouble(amount.get()));
    }

    // databinding
    public void onCancelOrderClick(final CommonOrder order) {
        mOnTradeEventListener.onCallRequest();
        mCancelOrderDataSource.call(order);
    }

    @Override
    public void onLimitOrderLoaded(List<CommonOrder> asks, List<CommonOrder> bids) {
        limitOrderAsks.clear();
        limitOrderAsks.addAll(asks);
        limitOrderBids.clear();
        limitOrderBids.addAll(bids);
        mOnTradeEventListener.onLoadFinish();
    }

    @Override
    public void onLimitOrderLoadFailed(int errorCode) {
        CoinoneErrorCodeUtil.handleErrorCode(mContext, errorCode);
        mOnTradeEventListener.onLoadFinish();
    }

    private void setOrderPrice() {
        String _amount = this.amount.get();
        String _price = this.price.get();
        if (!TextUtils.isEmpty(_amount) && !TextUtils.isEmpty(_price) && !_amount.startsWith(".")) {
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
        mOnTradeEventListener.onLoadFinish();
    }

    @Override
    public void onCancelFailed(int errorCode) {
        CoinoneErrorCodeUtil.handleErrorCode(mContext, errorCode);
        mOnTradeEventListener.onLoadFinish();
    }

    @Override
    public void onBuySellOrderSuccess(CommonOrder commonOrder) {
        if (commonOrder.isAsk) {
            limitOrderAsks.add(0, commonOrder);
        } else {
            limitOrderBids.add(0, commonOrder);
        }
        mOnTradeEventListener.onLoadFinish();
    }

    @Override
    public void onBuySellOrderFailed(int errorCode) {
        CoinoneErrorCodeUtil.handleErrorCode(mContext, errorCode);
        mOnTradeEventListener.onLoadFinish();
    }

    // databinding
    public void onPriceSort(boolean isAsk) {
        if (isAsk) {
            Collections.sort(limitOrderAsks, mIsAskAscending ? mAscendingPrice : mDescendingPrice);
            mIsAskAscending = !mIsAskAscending;
        } else {
            Collections.sort(limitOrderBids, mIsBidAscending ? mAscendingPrice : mDescendingPrice);
            mIsBidAscending = !mIsBidAscending;
        }
    }

}
