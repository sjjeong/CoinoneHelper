package com.googry.coinonehelper.ui.compare_another_exchange;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.KorbitTicker;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public interface CompareAnotherExchangeContract {
    interface Presenter extends BasePresenter {
        void loadTicker();

    }

    interface View extends BaseView<Presenter> {
        void showCoinoneTicker(CoinoneTicker coinoneTicker);
        void showBithumbTicker(BithumbTicker bithumbTicker);
        void showKorbitTicker(KorbitTicker korbitTicker);
        void hideProgress();
        void showToast(String msg);
    }
}
