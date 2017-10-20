package com.googry.coinonehelper.ui.main.coin_volume.coin_volume_detail;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinMarket;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 17..
 */

public interface CoinVolumeDetailContract {
    interface Presenter extends BasePresenter {
        void loadMarkets();
    }

    interface View extends BaseView<Presenter> {
        void refresh();

        void setData(ArrayList<CoinMarket> coinMarkets);

        void showPieChart(ArrayList<CoinMarket> coinMarkets, String coinName);
    }

}
