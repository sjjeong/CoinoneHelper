package com.googry.coinonehelper.ui.main.coin_volume;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinMarketCap;
import com.googry.coinonehelper.databinding.CoinVolumeItemBinding;
import com.googry.coinonehelper.ui.OnItemClickListener;

import java.util.ArrayList;


/**
 * Created by seokjunjeong on 2017. 10. 14..
 */

public class CoinVolumeAdapter extends RecyclerView.Adapter<CoinVolumeAdapter.CoinVolumeViewHolder> {

    private ArrayList<CoinMarketCap> mCoinMarketCaps;

    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setCoinMarketCaps(ArrayList<CoinMarketCap> coinMarketCaps) {
        mCoinMarketCaps = coinMarketCaps;
    }

    @Override
    public CoinVolumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.coin_volume_item, parent, false);
        return new CoinVolumeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CoinVolumeViewHolder holder, int position) {
        holder.getBinding().setCoinMarketCap(mCoinMarketCaps.get(position));
    }

    @Override
    public int getItemCount() {
        return mCoinMarketCaps != null ? mCoinMarketCaps.size() : 0;
    }

    class CoinVolumeViewHolder extends RecyclerView.ViewHolder {
        private CoinVolumeItemBinding mBinding;

        public CoinVolumeViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

        public CoinVolumeItemBinding getBinding() {
            return mBinding;
        }
    }

}

