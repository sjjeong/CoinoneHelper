package com.googry.coinonehelper.ui.main.orderbook;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.BithumbOrderbook;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.BithumbTrade;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.databinding.OrderbookFragmentBinding;
import com.googry.coinonehelper.util.DialogUtil;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookFragment extends BaseFragment<OrderbookFragmentBinding>
        implements OrderbookContract.View {
    private static final String KEY_COIN_TYPE = "coinType";

    private OrderbookContract.Presenter mPresenter;
    private RecyclerView mRvAskes, mRvBides, mRvTrades;
    private OrderbookAdapter mAskAdapter, mBidAdapter;
    private TradeAdapter mBithumbTradeAdapter;

    private CoinType mBithumbCoinType;


    public static OrderbookFragment newInstance(CoinType coinType) {
        OrderbookFragment bithumbOrderbookFragment = new OrderbookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_COIN_TYPE, coinType);
        bithumbOrderbookFragment.setArguments(bundle);
        return bithumbOrderbookFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.orderbook_fragment;
    }

    @Override
    protected void initView() {
        mRvAskes = mBinding.rvAskes;
        mRvBides = mBinding.rvBides;
        mRvTrades = mBinding.rvTrades;

        mAskAdapter = new OrderbookAdapter(getContext(), OrderbookAdapter.BookType.ASK);
        mBidAdapter = new OrderbookAdapter(getContext(), OrderbookAdapter.BookType.BID);
        mBithumbTradeAdapter = new TradeAdapter(getContext());

        mRvAskes.setAdapter(mAskAdapter);
        mRvBides.setAdapter(mBidAdapter);
        mRvTrades.setAdapter(mBithumbTradeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                RecyclerView.VERTICAL);
        mRvAskes.addItemDecoration(dividerItemDecoration);
        mRvBides.addItemDecoration(dividerItemDecoration);
        mRvTrades.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvAskes.getLayoutManager();
        layoutManager.setReverseLayout(true);
        layoutManager.setAutoMeasureEnabled(false);
        mRvAskes.setLayoutManager(layoutManager);

        mBithumbCoinType = (CoinType) getArguments().getSerializable(KEY_COIN_TYPE);
    }

    @Override
    protected void newPresenter() {
        new OrderbookPresenter(getContext(), this);
    }

    @Override
    protected void startPresenter() {
        mBinding.setPresenter(mPresenter);
        mPresenter.setCoinType(mBithumbCoinType);

        mPresenter.start();
    }

    @Override
    public void setPresenter(OrderbookContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.load();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.stop();
    }

    @Override
    public void showOrderbookList(BithumbOrderbook bithumbOrderbook) {
        if (bithumbOrderbook == null ||
                bithumbOrderbook.data.asks == null ||
                bithumbOrderbook.data.bids == null) return;

        ArrayList<CoinoneOrderbook.Book> askes, bides;
        askes = new ArrayList<>();
        for (BithumbOrderbook.Book book : bithumbOrderbook.data.asks) {
            askes.add(new CoinoneOrderbook.Book(book.price, book.quantity));
        }
        bides = new ArrayList<>();
        for (BithumbOrderbook.Book book : bithumbOrderbook.data.bids) {
            bides.add(new CoinoneOrderbook.Book(book.price, book.quantity));
        }

        if (mAskAdapter.getItemCount() == 0 &&
                mBidAdapter.getItemCount() == 0) {
            mAskAdapter.setBooks(askes);
            mBidAdapter.setBooks(bides);
            mRvAskes.scrollToPosition(0);
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
    public void showTradeList(BithumbTrade trade) {
        if (trade == null) return;
        if (trade.completeOrders == null) return;
        mBithumbTradeAdapter.setTrades(trade.completeOrders);

    }

    @Override
    public void showTicker(BithumbSoloTicker.Ticker ticker) {
        if (ticker == null) return;
        mBinding.setTicker(ticker);
    }

    @Override
    public void showCoinoneServerDownProgressDialog() {
        DialogUtil.showServerDownProgressDialog(getContext());
    }

    @Override
    public void hideCoinoneServerDownProgressDialog() {
        DialogUtil.hideProgressDialog();
    }

}
