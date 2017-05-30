package com.googry.coinonehelper.ui.main.orderbook;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinoneOrderbook;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookContract {
    public interface View extends BaseView<Presenter>{
        void showOrderbookList(ArrayList<CoinoneOrderbook.Book> askes,
                                 ArrayList<CoinoneOrderbook.Book> bides);
        void showProgressDialog();
        void hideProgressDialog();
    }

    public interface Presenter extends BasePresenter{
        void setCoinType(String coinType);
        void stop();
    }
}
