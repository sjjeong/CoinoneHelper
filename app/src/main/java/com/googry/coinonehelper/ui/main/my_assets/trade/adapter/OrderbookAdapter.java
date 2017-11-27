package com.googry.coinonehelper.ui.main.my_assets.trade.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CommonOrderbook;
import com.googry.coinonehelper.databinding.OrderbookSingleItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public class OrderbookAdapter extends RecyclerView.Adapter<OrderbookAdapter.ViewHolder> {
    private List<CommonOrderbook> mOrderbooks = new ArrayList<>();

    public OrderbookAdapter() {

    }

    public void replaceOrderbooks(List<CommonOrderbook> orderbooks) {
        mOrderbooks.clear();
        mOrderbooks.addAll(orderbooks);
        notifyDataSetChanged();
    }

    public void replaceOrderbook(int position, CommonOrderbook orderbook) {
        mOrderbooks.set(position, orderbook);
        notifyItemChanged(position);
    }

    public CommonOrderbook getOrderbook(int position) {
        return mOrderbooks.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderbook_single_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.binding.setOrderbook(mOrderbooks.get(position));
    }

    @Override
    public int getItemCount() {
        return mOrderbooks != null ? mOrderbooks.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public OrderbookSingleItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
