package com.googry.coinonehelper.ui.compare_another_exchange;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.CompareAnotherExchangeFragmentBinding;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class CompareAnotherExchangeFragment extends BaseFragment<CompareAnotherExchangeFragmentBinding> implements CompareAnotherExchangeContract.View {
    private CompareAnotherExchangeContract.Presenter mPresenter;

    public static CompareAnotherExchangeFragment newInstance() {
        CompareAnotherExchangeFragment fragment = new CompareAnotherExchangeFragment();
        return fragment;
    }

    @Override
    public void setPresenter(CompareAnotherExchangeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.compare_another_exchange_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void newPresenter() {
        new CompareAnotherExchangePresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }
}
