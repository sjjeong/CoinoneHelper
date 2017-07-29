package com.googry.coinonehelper.ui.compare_another_exchange;

import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.PoloniexTicker;
import com.googry.coinonehelper.databinding.CompareAnotherExchangeFragmentBinding;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class CompareAnotherExchangeFragment extends BaseFragment<CompareAnotherExchangeFragmentBinding> implements CompareAnotherExchangeContract.View {
    private CompareAnotherExchangeContract.Presenter mPresenter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
        mSwipeRefreshLayout = mBinding.swipeRefreshLayout;
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadTicker();
            }
        });

    }

    @Override
    protected void newPresenter() {
        new CompareAnotherExchangePresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void showCoinoneTicker(CoinoneTicker coinoneTicker) {
        mBinding.setCoinoneTicker(coinoneTicker);
    }

    @Override
    public void showBithumbTicker(BithumbTicker bithumbTicker) {
        mBinding.setBithumbTicker(bithumbTicker);
    }

    @Override
    public void showKorbitTicker(KorbitTicker korbitTicker) {
        mBinding.setKorbitTicker(korbitTicker);
    }

    @Override
    public void showPoloniexTicker(PoloniexTicker poloniexTicker) {
        mBinding.setPoloniexTicker(poloniexTicker);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showToast(String msg) {
        if (getContext() != null)
            Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
