package com.googry.coinonehelper.ui.main.my_assets.trade.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.CommonOrderbook;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.AskBidAdapter;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.ConclusionHistoryAdapter;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.OrderbookAdapter;
import com.googry.coinonehelper.util.LogUtil;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public class TradeListBindings {

    @BindingAdapter("bind:conclusionHistories")
    public static void setConclusionHistories(RecyclerView recyclerView, List<CommonOrder> conclusionHistories) {
        ConclusionHistoryAdapter conclusionHistoryAdapter = (ConclusionHistoryAdapter) recyclerView.getAdapter();
        if (conclusionHistoryAdapter != null) {
            conclusionHistoryAdapter.replace(conclusionHistories);
            LogUtil.e(String.valueOf(conclusionHistoryAdapter.getItemCount()));
        }
    }

    @BindingAdapter("bind:limitOrders")
    public static void setLimitOrders(RecyclerView recyclerView, List<CommonOrder> limitOrders) {
        AskBidAdapter askBidAdapter = (AskBidAdapter) recyclerView.getAdapter();
        if (askBidAdapter != null) {
            askBidAdapter.replace(limitOrders);
            LogUtil.e(String.valueOf(askBidAdapter.getItemCount()));
        }
    }

    @BindingAdapter("bind:orderbooks")
    public static void setOrderbooks(RecyclerView recyclerView, List<CommonOrderbook> orderbooks) {
        OrderbookAdapter orderbookAdapter = (OrderbookAdapter) recyclerView.getAdapter();
        if (orderbookAdapter != null) {
            if (orderbookAdapter.getItemCount() == 0) {
                orderbookAdapter.replaceOrderbooks(orderbooks);
                recyclerView.scrollToPosition(0);
                return;
            }
            int size = orderbooks.size();
            for (int i = 0; i < size; i++) {
                CommonOrderbook prevBook = orderbookAdapter.getOrderbook(i);
                CommonOrderbook book = orderbooks.get(i);
                if (prevBook.price == book.price &&
                        prevBook.qty == book.qty) {
                    continue;
                }
                orderbookAdapter.replaceOrderbook(i, book);
            }
        }
    }

    @BindingAdapter("bind:reverse")
    public static void setReverse(RecyclerView recyclerView, boolean reverse) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.setReverseLayout(reverse);
            layoutManager.setStackFromEnd(reverse);
            recyclerView.setLayoutManager(layoutManager);
        }
    }
}
