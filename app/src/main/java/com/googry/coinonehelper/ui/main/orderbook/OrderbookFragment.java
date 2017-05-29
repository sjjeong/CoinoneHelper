package com.googry.coinonehelper.ui.main.orderbook;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.databinding.OrderbookFragBinding;
import com.googry.coinonehelper.util.DialogUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookFragment extends BaseFragment<OrderbookFragBinding>
        implements OrderbookContract.View {
    private static final String COIN_TYPE = "coinType";

    private OrderbookContract.Presenter mPresenter;
    private RecyclerView mRvAskes, mRvBides;
    private OrderbookAdapter mAskAdapter, mBidAdapter;

    private String mCoinType;


    public static OrderbookFragment newInstance(String coinType) {
        OrderbookFragment orderbookFragment = new OrderbookFragment();
        Bundle bundle = new Bundle();
        bundle.putString(COIN_TYPE, coinType);
        orderbookFragment.setArguments(bundle);
        return orderbookFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.orderbook_frag;
    }

    @Override
    protected void initView() {
        new OrderbookPresenter(this);


        mRvAskes = mBinding.rvAskes;
        mRvBides = mBinding.rvBides;

        mAskAdapter = new OrderbookAdapter(getContext(), OrderbookAdapter.BookType.ASK);
        mBidAdapter = new OrderbookAdapter(getContext(), OrderbookAdapter.BookType.BID);

        mRvAskes.setAdapter(mAskAdapter);
        mRvBides.setAdapter(mBidAdapter);

        mCoinType = getArguments().getString(COIN_TYPE);

        mBinding.setTitle(mCoinType);
        mBinding.setPresenter(mPresenter);
        mPresenter.setCoinType(mCoinType);
        mPresenter.start();
    }

    @Override
    public void setPresenter(OrderbookContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.stop();
    }

    @Override
    public void showOrderbookList(ArrayList<CoinoneOrderbook.Book> askes,
                                  ArrayList<CoinoneOrderbook.Book> bides) {
        Collections.reverse(askes);
        mAskAdapter.setBooks(askes);
        mBidAdapter.setBooks(bides);

        mRvAskes.getLayoutManager().scrollToPosition(askes.size()-1);
    }

    @Override
    public void showProgressDialog() {
        DialogUtil.showProgressDialog(getContext());
    }

    @Override
    public void hideProgressDialog() {
        DialogUtil.hideProgressDialog();
    }
}
