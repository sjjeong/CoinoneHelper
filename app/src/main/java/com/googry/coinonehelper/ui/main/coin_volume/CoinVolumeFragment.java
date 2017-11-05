package com.googry.coinonehelper.ui.main.coin_volume;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinMarketCap;
import com.googry.coinonehelper.databinding.CoinVolumeFragmentBinding;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 9..
 */

public class CoinVolumeFragment extends BaseFragment<CoinVolumeFragmentBinding> implements CoinVolumeContract.View {
    private CoinVolumeContract.Presenter mPresenter;
    private CoinVolumeAdapter mCoinVolumeAdapter;

    public static CoinVolumeFragment newInstance() {

        Bundle args = new Bundle();

        CoinVolumeFragment fragment = new CoinVolumeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(CoinVolumeContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.coin_volume_fragment;
    }

    @Override
    protected void initView() {
        mCoinVolumeAdapter = new CoinVolumeAdapter();
        mBinding.rvCoinList.setAdapter(mCoinVolumeAdapter);

        mBinding.swipeRefreshLayout.setRefreshing(true);
        mBinding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.loadVolume();
            }
        });
    }

    @Override
    protected void newPresenter() {
        new CoinVolumePresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void setData(ArrayList<CoinMarketCap> coinMarketCaps) {
        mCoinVolumeAdapter.setCoinMarketCaps(coinMarketCaps);
    }

    @Override
    public void refresh() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBinding.swipeRefreshLayout.setRefreshing(false);
                    mCoinVolumeAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}
