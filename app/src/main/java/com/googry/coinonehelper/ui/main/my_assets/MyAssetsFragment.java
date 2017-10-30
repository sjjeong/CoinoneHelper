package com.googry.coinonehelper.ui.main.my_assets;

import android.os.Bundle;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.MyAssetsFragmentBinding;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public class MyAssetsFragment extends BaseFragment<MyAssetsFragmentBinding> implements MyAssetsContract.View {
    private MyAssetsContract.Presenter mPresenter;

    public static MyAssetsFragment newInstance() {

        Bundle args = new Bundle();

        MyAssetsFragment fragment = new MyAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(MyAssetsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_assets_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void newPresenter() {
        new MyAssetsPresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }
}
