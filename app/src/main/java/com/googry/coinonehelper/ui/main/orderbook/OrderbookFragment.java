package com.googry.coinonehelper.ui.main.orderbook;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                RecyclerView.VERTICAL);
        mRvAskes.addItemDecoration(dividerItemDecoration);
        mRvBides.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvAskes.getLayoutManager();
        layoutManager.setReverseLayout(true);
        mRvAskes.setLayoutManager(layoutManager);

        mCoinType = getArguments().getString(COIN_TYPE);

        mBinding.setPresenter(mPresenter);
        mPresenter.setCoinType(mCoinType);
    }

    @Override
    public void setPresenter(OrderbookContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void showOrderbookList(ArrayList<CoinoneOrderbook.Book> askes,
                                  ArrayList<CoinoneOrderbook.Book> bides) {
        if (mAskAdapter.getItemCount() == 0 &&
                mBidAdapter.getItemCount() == 0) {
            mAskAdapter.setBooks(askes);
            mBidAdapter.setBooks(bides);
        }

        int askSize = askes.size();
        for (int i = 0; i < askSize; i++) {
            CoinoneOrderbook.Book prevBook = mAskAdapter.getBook(i);
            CoinoneOrderbook.Book book = askes.get(i);
            if (prevBook.price == book.price &&
                    prevBook.qty == book.qty) {
                continue;
            }
            mAskAdapter.setBook(book, i);
        }
        int bidSize = bides.size();
        for (int i = 0; i < bidSize; i++) {
            CoinoneOrderbook.Book prevBook = mBidAdapter.getBook(i);
            CoinoneOrderbook.Book book = bides.get(i);
            if (prevBook.price == book.price &&
                    prevBook.qty == book.qty) {
                continue;
            }
            mBidAdapter.setBook(book, i);
        }
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
