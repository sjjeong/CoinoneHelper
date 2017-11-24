package com.googry.coinonehelper.ui.main.my_assets.trade.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.googry.coinonehelper.data.CommonOrderbook;
import com.googry.coinonehelper.ui.main.my_assets.trade.adapter.OrderbookAdapter;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public class OrderbookListBindings {

    @BindingAdapter("app:orderbooks")
    public static void setOrderbooks(RecyclerView recyclerView, List<CommonOrderbook> orderbooks) {
        OrderbookAdapter orderbookAdapter = (OrderbookAdapter) recyclerView.getAdapter();
        if (orderbookAdapter != null) {
            orderbookAdapter.replaceOrderbooks(orderbooks);
        }
    }
}
