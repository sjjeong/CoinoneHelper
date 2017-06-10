package com.googry.coinonehelper.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.MainFragBinding;
import com.googry.coinonehelper.ui.main.orderbook.OrderbookPagerAdapter;
import com.viewpagerindicator.LinePageIndicator;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class MainFragment extends BaseFragment<MainFragBinding> implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private ViewPager mVpDashboard;
    private OrderbookPagerAdapter mOrderbookPagerAdapter;

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
        mBinding.setFragment(this);
        mVpDashboard = mBinding.vpDashboard;
        mOrderbookPagerAdapter = new OrderbookPagerAdapter(getChildFragmentManager());
        mVpDashboard.setAdapter(mOrderbookPagerAdapter);

        UnderlinePageIndicator pageIndicator = mBinding.indicator;
        pageIndicator.setViewPager(mVpDashboard);
        pageIndicator.setFades(false);

        mPresenter.start();
    }

    public void onClickChangeOrderbookType(View v){
        Log.i("googry","onClickChangeOrderbookType");
        switch (v.getId()){
            case R.id.btn_btc:{
                mVpDashboard.setCurrentItem(0);
                Log.i("googry","onClickChangeOrderbookType 0");
            }
            break;
            case R.id.btn_eth:{
                mVpDashboard.setCurrentItem(1);
                Log.i("googry","onClickChangeOrderbookType 1");
            }
            break;
            case R.id.btn_etc:{
                mVpDashboard.setCurrentItem(2);
                Log.i("googry","onClickChangeOrderbookType 2");
            }
            break;
            case R.id.btn_xrp:{
                mVpDashboard.setCurrentItem(3);
                Log.i("googry","onClickChangeOrderbookType 3");
            }
            break;
        }
    }
}
