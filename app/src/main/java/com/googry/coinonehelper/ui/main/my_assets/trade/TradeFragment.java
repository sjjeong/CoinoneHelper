package com.googry.coinonehelper.ui.main.my_assets.trade;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.Gson;
import com.googry.coinonehelper.Injection;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.MarketAccount;
import com.googry.coinonehelper.databinding.TradeFragmentBinding;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.OrderbookAdapter;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.TradePagerAdapter;
import com.googry.coinonehelper.util.PrefUtil;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 11. 13..
 */

public class TradeFragment extends BaseFragment<TradeFragmentBinding>
        implements View.OnClickListener, OnTradeEventListener {
    private static final String KEY_COIN_NAME = "KEY_COIN_NAME";
    private static final int REQUEST_TIME = 1000;

    private OrderbookViewModel mOrderbookViewModel;
    private AskBidViewModel mAskBidViewModel;
    private ConclusionHistoryViewModel mConclusionHistoryViewModel;

    private TradePagerAdapter mTradePagerAdapter;
    private ProgressDialog mProgressDialog;
    private Handler dialogHideHandler = new Handler();
    private Runnable dialogHideRunnable = new Runnable() {
        @Override
        public void run() {
            hideLoadingDialog();
        }
    };

    private Realm mRealm;

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
        mBinding.rvAsk.setAdapter(new OrderbookAdapter());
        mBinding.rvBid.setAdapter(new OrderbookAdapter());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                RecyclerView.VERTICAL);
        mBinding.rvAsk.addItemDecoration(dividerItemDecoration);
        mBinding.rvBid.addItemDecoration(dividerItemDecoration);


        mTradePagerAdapter = new TradePagerAdapter(getChildFragmentManager(),
                getString(R.string.bid), getString(R.string.ask), getString(R.string.conclusion_history));
        mBinding.viewPager.setAdapter(mTradePagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(mTradePagerAdapter.getCount() - 1);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);

        getActivity().findViewById(R.id.iv_refresh).setOnClickListener(this);
    }

    @Override
    protected void newPresenter() {
        String coinName = getArguments().getString(KEY_COIN_NAME);
        mOrderbookViewModel = new OrderbookViewModel(coinName);
        mBinding.setViewModel(mOrderbookViewModel);
        mRealm = Injection.getSecureRealm();

        MarketAccount account = mRealm.where(MarketAccount.class).findFirst();

        mAskBidViewModel =
                new AskBidViewModel(getContext(), coinName,
                        String.valueOf(new Gson().fromJson(PrefUtil.loadTicker(getContext(),
                                CoinType.getCoinTypeFromTitle(coinName)),
                                CoinoneTicker.Ticker.class).last), this,
                        account);
        mTradePagerAdapter.setAskBidViewModel(mAskBidViewModel);
        mConclusionHistoryViewModel = new ConclusionHistoryViewModel(getContext(), coinName,
                this, account);
        mTradePagerAdapter.setConclusionHistoryViewModel(mConclusionHistoryViewModel);
    }

    @Override
    protected void startPresenter() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mOrderbookViewModel.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mOrderbookViewModel.stop();
    }

    @Override
    public void onDestroyView() {
        mRealm.close();
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh: {
                mAskBidViewModel.call();
                mConclusionHistoryViewModel.call();
            }
            break;
        }
    }

    @Override
    public void onCallRequest() {
        showLoadingDialog();
    }

    @Override
    public void onLoadFinish() {
        dialogHideHandler.removeCallbacks(dialogHideRunnable);
        dialogHideHandler.postDelayed(dialogHideRunnable, REQUEST_TIME);
    }

    private void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new android.app.ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.requesting));
            mProgressDialog.setCancelable(false);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
