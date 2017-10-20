package com.googry.coinonehelper.ui.main.coin_volume.coin_volume_detail;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinMarket;
import com.googry.coinonehelper.databinding.CoinVolumeDetailItemBinding;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 10. 20..
 */

public class CoinVolumeDetailAdapter extends RecyclerView.Adapter<CoinVolumeDetailAdapter.ViewHolder> {
    private ArrayList<CoinMarket> mCoinMarkets;

    public void setCoinMarkets(ArrayList<CoinMarket> coinMarkets) {
        mCoinMarkets = coinMarkets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_volume_detail_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getBinding().setCoinMarket(mCoinMarkets.get(position));
    }

    @Override
    public int getItemCount() {
        return mCoinMarkets != null ? mCoinMarkets.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CoinVolumeDetailItemBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public CoinVolumeDetailItemBinding getBinding() {
            return mBinding;
        }
    }

}
