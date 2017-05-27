package com.googry.coinonehelper.ui.main;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
