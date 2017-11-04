package com.googry.coinonehelper.ui.main.orderbook;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.CoinoneTrade;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookContract {
    public interface View extends BaseView<Presenter> {
        void showOrderbookList(CoinoneOrderbook coinoneOrderbook);

        void showTradeList(CoinoneTrade trade);

        void showTicker(CoinoneTicker.Ticker ticker);

        void showCoinoneServerDownProgressDialog();

        void hideCoinoneServerDownProgressDialog();
    }

    public interface Presenter extends BasePresenter {
        void setCoinType(CoinType coinType);

        void stop();

        void load();
    }
}
