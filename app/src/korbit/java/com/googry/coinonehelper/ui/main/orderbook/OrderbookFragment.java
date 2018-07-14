package com.googry.coinonehelper.ui.main.orderbook;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.KorbitOrderbook;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.KorbitTrade;
import com.googry.coinonehelper.databinding.OrderbookFragmentBinding;
import com.googry.coinonehelper.util.DialogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 5. 28..
 */

public class OrderbookFragment extends BaseFragment<OrderbookFragmentBinding>
        implements OrderbookContract.View {
    private static final String KEY_COIN_TYPE = "coinType";

    private OrderbookContract.Presenter mPresenter;
    private RecyclerView mRvAskes, mRvBides, mRvTrades;
    private OrderbookAdapter mAskAdapter, mBidAdapter;
    private TradeAdapter mKorbitTradeAdapter;

    private CoinType mKorbitCoinType;


    public static OrderbookFragment newInstance(CoinType coinType) {
        OrderbookFragment korbitOrderbookFragment = new OrderbookFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_COIN_TYPE, coinType);
        korbitOrderbookFragment.setArguments(bundle);
        return korbitOrderbookFragment;
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
        mKorbitTradeAdapter = new TradeAdapter(getContext());

        mRvAskes.setAdapter(mAskAdapter);
        mRvBides.setAdapter(mBidAdapter);
        mRvTrades.setAdapter(mKorbitTradeAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                RecyclerView.VERTICAL);
        mRvAskes.addItemDecoration(dividerItemDecoration);
        mRvBides.addItemDecoration(dividerItemDecoration);
        mRvTrades.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager layoutManager = (LinearLayoutManager) mRvAskes.getLayoutManager();
        layoutManager.setReverseLayout(true);
        layoutManager.setAutoMeasureEnabled(false);
        mRvAskes.setLayoutManager(layoutManager);

        mKorbitCoinType = (CoinType) getArguments().getSerializable(KEY_COIN_TYPE);
    }

    @Override
    protected void newPresenter() {
        new OrderbookPresenter(getContext(), this);
    }

    @Override
    protected void startPresenter() {
        mBinding.setPresenter(mPresenter);
        mPresenter.setCoinType(mKorbitCoinType);

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
    public void showOrderbookList(KorbitOrderbook korbitOrderbook) {
        if (korbitOrderbook == null ||
                korbitOrderbook.asks == null ||
                korbitOrderbook.bids == null) return;

        ArrayList<CoinoneOrderbook.Book> askes, bides;

        askes = new ArrayList<>();

        for (List<String> strings : korbitOrderbook.asks) {
            askes.add(new CoinoneOrderbook.Book(Long.valueOf(strings.get(0)), Double.valueOf(strings.get(1))));
        }
        bides = new ArrayList<>();
        for (List<String> strings : korbitOrderbook.bids) {
            bides.add(new CoinoneOrderbook.Book(Long.valueOf(strings.get(0)), Double.valueOf(strings.get(1))));
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
    public void showTradeList(List<KorbitTrade> trade) {
        if (trade == null) return;
        mKorbitTradeAdapter.setTrades(trade);

    }

    @Override
    public void showTicker(KorbitTicker.TickerDetailed ticker) {
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
