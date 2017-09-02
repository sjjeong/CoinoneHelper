package com.googry.coinonehelper.korbit.ui.main.orderbook;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.CoinoneTrade;
import com.googry.coinonehelper.data.KorbitOrderbook;
import com.googry.coinonehelper.data.KorbitTrade;
import com.googry.coinonehelper.korbit.data.KorbitCoinType;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class KorbitOrderbookContract {
    public interface View extends BaseView<Presenter> {
        void showOrderbookList(KorbitOrderbook korbitOrderbook);

        void showTradeList(List<KorbitTrade> trade);

        void showTicker(CoinoneTicker.Ticker ticker);

        void showCoinoneServerDownProgressDialog();

        void hideCoinoneServerDownProgressDialog();
    }

    public interface Presenter extends BasePresenter {
        void setCoinType(KorbitCoinType coinType);

        void stop();

        void load();

        void loadCoinoneOrderbook();

        void loadCoinoneTrade();

        void loadCoinoneTicker();
    }
}
