package com.googry.coinonehelper.ui.main.coin_volume;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinMarketCap;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 9..
 */

public interface CoinVolumeContract {
    interface Presenter extends BasePresenter {
        void loadVolume();
    }

    interface View extends BaseView<Presenter> {
        void setData(ArrayList<CoinMarketCap> coinMarketCaps);

        void refresh();
    }

}
