package com.googry.coinonehelper.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.MainFragmentBinding;
import com.googry.coinonehelper.ui.main.orderbook.OrderbookPagerAdapter;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class MainFragment extends BaseFragment<MainFragmentBinding> implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private ViewPager mVpDashboard;
    private OrderbookPagerAdapter mOrderbookPagerAdapter;
    private AdView mAdView;

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
        return R.layout.main_fragment;
    }

    @Override
    protected void initView() {
        mBinding.setFragment(this);
        mVpDashboard = mBinding.vpDashboard;
        mVpDashboard.setOffscreenPageLimit(3);
        mOrderbookPagerAdapter = new OrderbookPagerAdapter(getChildFragmentManager());
        mVpDashboard.setAdapter(mOrderbookPagerAdapter);
        UnderlinePageIndicator pageIndicator = mBinding.indicator;
        pageIndicator.setViewPager(mVpDashboard);
        pageIndicator.setFades(false);

        mAdView = mBinding.adView;

        setAddSetting();
    }

    @Override
    protected void newPresenter() {
        new MainPresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Resume the AdView.
        mAdView.resume();
    }

    @Override
    public void onPause() {
        // Pause the AdView.
        mAdView.pause();

        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Destroy the AdView.
        mAdView.destroy();

        super.onDestroy();
    }

    private void setAddSetting() {
        MobileAds.initialize(getContext(), getString(R.string.admob_app_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    // databinding
    public void onClickChangeOrderbookType(View v) {
        switch (v.getId()) {
            case R.id.btn_btc: {
                mVpDashboard.setCurrentItem(0);
            }
            break;
            case R.id.btn_eth: {
                mVpDashboard.setCurrentItem(1);
            }
            break;
            case R.id.btn_etc: {
                mVpDashboard.setCurrentItem(2);
            }
            break;
            case R.id.btn_xrp: {
                mVpDashboard.setCurrentItem(3);
            }
            break;
        }
    }
}
