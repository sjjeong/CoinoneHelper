package com.googry.coinonehelper.ui.main;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.databinding.SlideMenuCoinTypeItemBinding;
import com.googry.coinonehelper.util.PrefUtil;

/**
 * Created by seokjunjeong on 2017. 9. 11..
 */

public class SlideMenuCoinTypeAdapter extends RecyclerView.Adapter<SlideMenuCoinTypeAdapter.ViewHolder> {
    private CoinType[] mCoinTypes = CoinType.values();
    private OnCoinTypeItemClickListener mOnCoinTypeItemClickListener;

    public SlideMenuCoinTypeAdapter(OnCoinTypeItemClickListener onCoinTypeItemClickListener) {
        mOnCoinTypeItemClickListener = onCoinTypeItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide_menu_coin_type_item, parent, false);
        return new ViewHolder(view, mOnCoinTypeItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mCoinTypes[position]);
    }

    @Override
    public int getItemCount() {
        return mCoinTypes.length;
    }

    public interface OnCoinTypeItemClickListener {
        void onCoinTypeItemClickListener(CoinType coinType);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private SlideMenuCoinTypeItemBinding mBinding;
        private Context mContext;
        private CoinType mCoinType;

        public ViewHolder(View itemView, final OnCoinTypeItemClickListener onCoinTypeItemClickListener) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mContext = itemView.getContext();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCoinTypeItemClickListener.onCoinTypeItemClickListener(mCoinType);
                }
            });
        }

        public void bind(CoinType coinType) {
            mCoinType = coinType;
            mBinding.ivCoinIcon.setImageResource(CoinType.getCoinIconRes(coinType));
            mBinding.ivCoinTitle.setText(CoinType.getCoinTitleRes(coinType));
            if (BuildConfig.FLAVOR.equals("bithumb")) {
                BithumbSoloTicker.Ticker ticker = new Gson().fromJson(PrefUtil.loadTicker(mContext, coinType), BithumbSoloTicker.Ticker.class);
                mBinding.tvCoinPrice.setText(ticker != null ? String.format("%,d", ticker.last) : "");
            } else if (BuildConfig.FLAVOR.equals("korbit")) {
                KorbitTicker.Ticker ticker = new Gson().fromJson(PrefUtil.loadTicker(mContext, coinType), KorbitTicker.Ticker.class);
                mBinding.tvCoinPrice.setText(ticker != null ? String.format("%,d", ticker.last) : "");
            } else {
                CoinoneTicker.Ticker ticker = new Gson().fromJson(PrefUtil.loadTicker(mContext, coinType), CoinoneTicker.Ticker.class);
                mBinding.tvCoinPrice.setText(ticker != null ? String.format("%,d", ticker.last) : "");
            }
        }
    }
}
