package com.googry.coinonehelper.ui.main.my_assets.trade.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.databinding.ConclusionHistoryItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 11. 26..
 */

public class ConclusionHistoryAdapter extends RecyclerView.Adapter<ConclusionHistoryAdapter.ViewHolder> {
    private ArrayList<CommonOrder> mCompleteOrders = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conclusion_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setCompleteOrder(mCompleteOrders.get(position));
    }

    @Override
    public int getItemCount() {
        return mCompleteOrders.size();
    }

    public void replace(List<CommonOrder> conclusionHistories) {
        mCompleteOrders.clear();
        mCompleteOrders.addAll(conclusionHistories);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ConclusionHistoryItemBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }
    }
}
