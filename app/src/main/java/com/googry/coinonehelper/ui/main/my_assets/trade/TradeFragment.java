package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.os.Bundle;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.TradeFragmentBinding;

/**
 * Created by seokjunjeong on 2017. 11. 13..
 */

public class TradeFragment extends BaseFragment<TradeFragmentBinding> {
    private static final String KEY_COIN_NAME = "KEY_COIN_NAME";
    private TradeViewModel mTradeViewModel;

    public static TradeFragment newInstance(String coinName) {
        
        Bundle args = new Bundle();
        args.putString(KEY_COIN_NAME, coinName);
        
        TradeFragment fragment = new TradeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    protected int getLayoutId() {
        return R.layout.trade_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void newPresenter() {
        mTradeViewModel = new TradeViewModel(getArguments().getString(KEY_COIN_NAME));
        mBinding.setViewModel(mTradeViewModel);
    }

    @Override
    protected void startPresenter() {
        mTradeViewModel.start();
    }
}
