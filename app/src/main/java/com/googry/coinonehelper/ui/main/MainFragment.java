package com.googry.coinonehelper.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.databinding.MainFragmentBinding;
import com.googry.coinonehelper.ui.main.orderbook.OrderbookPagerAdapter;
import com.googry.coinonehelper.util.LogUtil;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class MainFragment extends BaseFragment<MainFragmentBinding> implements MainContract.View {
    public static final String EXTRA_COIN_TYPE = "EXTRA_COIN_TYPE";
    private MainContract.Presenter mPresenter;
    private ViewPager mVpDashboard;
    private OrderbookPagerAdapter mOrderbookPagerAdapter;
    private CoinType mCoinType;

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    public static MainFragment newInstance(CoinType coinType) {
        MainFragment mainFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_COIN_TYPE, coinType);
        mainFragment.setArguments(bundle);
        return mainFragment;
    }


    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.main_fragment;
    }

    @Override
    protected void initView() {
        mBinding.setFragment(this);
        mCoinType = (CoinType) getArguments().getSerializable(EXTRA_COIN_TYPE);
        mVpDashboard = mBinding.vpDashboard;
        mVpDashboard.setOffscreenPageLimit(CoinType.values().length - 1);
        mVpDashboard.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                        CoinType.getCoinTitleRes(CoinType.values()[position]));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mOrderbookPagerAdapter = new OrderbookPagerAdapter(getChildFragmentManager());
        mVpDashboard.setAdapter(mOrderbookPagerAdapter);
        mVpDashboard.setCurrentItem(mCoinType == null ? 0 : mCoinType.ordinal());
    }

    @Override
    protected void newPresenter() {
        new MainPresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }


    public void setCoinTypeUi(CoinType coinType) {
        mVpDashboard.setCurrentItem(coinType.ordinal());
    }
}
