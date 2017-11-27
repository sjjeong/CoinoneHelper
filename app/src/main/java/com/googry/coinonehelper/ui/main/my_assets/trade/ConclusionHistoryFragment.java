package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.os.Bundle;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.ConclusionHistoryFragmentBinding;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.ConclusionHistoryAdapter;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class ConclusionHistoryFragment extends BaseFragment<ConclusionHistoryFragmentBinding> {
    private ConclusionHistoryViewModel mConclusionHistoryViewModel;

    public static ConclusionHistoryFragment newInstance() {

        Bundle args = new Bundle();

        ConclusionHistoryFragment fragment = new ConclusionHistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.conclusion_history_fragment;
    }

    @Override
    protected void initView() {
        mBinding.rvContent.setAdapter(new ConclusionHistoryAdapter());
    }

    @Override
    protected void newPresenter() {
        mBinding.setViewModel(mConclusionHistoryViewModel);
    }

    @Override
    protected void startPresenter() {

    }

    public void setConclusionHistoryViewModel(ConclusionHistoryViewModel conclusionHistoryViewModel) {
        mConclusionHistoryViewModel = conclusionHistoryViewModel;
    }
}
