package com.googry.coinonehelper.ui.compare_another_exchange;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class CompareAnotherExchangePresenter implements CompareAnotherExchangeContract.Presenter {
    private CompareAnotherExchangeContract.View mView;

    public CompareAnotherExchangePresenter(CompareAnotherExchangeContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
