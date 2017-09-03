package com.googry.coinonehelper.ui.main.orderbook;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.KorbitOrderbook;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.KorbitTrade;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookContract {
    public interface View extends BaseView<Presenter> {
        void showOrderbookList(KorbitOrderbook korbitOrderbook);

        void showTradeList(List<KorbitTrade> trade);

        void showTicker(KorbitTicker.TickerDetailed ticker);

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
