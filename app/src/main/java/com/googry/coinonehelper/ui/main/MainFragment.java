package com.googry.coinonehelper.ui.main;

import android.os.Bundle;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.MainFragBinding;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class MainFragment extends BaseFragment<MainFragBinding> implements MainContract.View {
    private MainContract.Presenter mPresenter;


    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_frag;
    }

    @Override
    protected void initView() {
        mPresenter.start();
    }
}
