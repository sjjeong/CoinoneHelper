package com.googry.coinonehelper.ui.main.orderbook;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.BithumbOrderbook;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.BithumbTrade;
import com.googry.coinonehelper.data.CoinType;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookContract {
    public interface View extends BaseView<Presenter> {
        void showOrderbookList(BithumbOrderbook bithumbOrderbook);

        void showTradeList(BithumbTrade trade);

        void showTicker(BithumbSoloTicker.Ticker ticker);

        void showCoinoneServerDownProgressDialog();

        void hideCoinoneServerDownProgressDialog();
    }

    public interface Presenter extends BasePresenter {
        void setCoinType(CoinType coinType);

        void stop();

        void load();

        void loadCoinoneOrderbook();

        void loadCoinoneTrade();

        void loadCoinoneTicker();
    }
}
