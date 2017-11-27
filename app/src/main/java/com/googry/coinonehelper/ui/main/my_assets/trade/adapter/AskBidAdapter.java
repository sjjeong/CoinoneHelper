package com.googry.coinonehelper.ui.main.my_assets.trade.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.databinding.AskBidItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class AskBidAdapter extends RecyclerView.Adapter<AskBidAdapter.ViewHolder> {
    private ArrayList<CommonOrder> mLimitOrders = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.ask_bid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setLimitOrder(mLimitOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return mLimitOrders.size();
    }

    public void replace(List<CommonOrder> limitOrders) {
        mLimitOrders.clear();
        mLimitOrders.addAll(limitOrders);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public AskBidItemBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
