package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.os.Bundle;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.AskBidFragmentBinding;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.AskBidAdapter;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class AskBidFragment extends BaseFragment<AskBidFragmentBinding> {
    private static final String KEY_IS_ASK = "IS_ASK";
    private boolean mIsAsk;

    private AskBidViewModel mAskBidViewModel;

    public static AskBidFragment newInstance(boolean isAsk) {

        Bundle args = new Bundle();
        args.putBoolean(KEY_IS_ASK, isAsk);

        AskBidFragment fragment = new AskBidFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ask_bid_fragment;
    }

    @Override
    protected void initView() {
        mIsAsk = getArguments().getBoolean(KEY_IS_ASK);
        mBinding.setIsAsk(mIsAsk);
        AskBidAdapter askBidAdapter = new AskBidAdapter();
        askBidAdapter.setAskBidViewModel(mAskBidViewModel);
        mBinding.rvContent.setAdapter(askBidAdapter);
    }

    @Override
    protected void newPresenter() {
        mBinding.setViewModel(mAskBidViewModel);
    }

    @Override
    protected void startPresenter() {
    }

    public void setAskBidViewModel(AskBidViewModel askBidViewModel) {
        mAskBidViewModel = askBidViewModel;
    }
}
