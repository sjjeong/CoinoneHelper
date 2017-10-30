package com.googry.coinonehelper.ui.main.my_assets;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public class MyAssetsPresenter implements MyAssetsContract.Presenter {
    private MyAssetsContract.View mView;

    public MyAssetsPresenter(MyAssetsContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }
}
