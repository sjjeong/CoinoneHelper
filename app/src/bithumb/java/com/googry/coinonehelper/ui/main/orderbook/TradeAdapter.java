package com.googry.coinonehelper.ui.main.orderbook;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.BithumbTrade;
import com.googry.coinonehelper.databinding.TradeItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 6. 14..
 */

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.ViewHolder> {
    private List<BithumbTrade.CompleteOrder> mTrades;
    private Context mContext;

    public TradeAdapter(Context context) {
        mContext = context;
        mTrades = new ArrayList<>();
    }

    public void setTrades(List<BithumbTrade.CompleteOrder> trades) {
        mTrades = trades;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trade_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mTrades.get(position));
    }

    @Override
    public int getItemCount() {
        return mTrades.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TradeItemBinding binding;

        public ViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void bind(BithumbTrade.CompleteOrder trade) {
            binding.setPrice(String.format("%,d", trade.price));
            binding.setQty(String.format("%,.4f", trade.units_traded));
            binding.setBuy(true);
        }

    }
}
