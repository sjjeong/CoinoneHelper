package com.googry.coinonehelper.ui.main.my_assets.adapter;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.databinding.MyAssetsItemBinding;
import com.googry.coinonehelper.ui.main.my_assets.trade.TradeActivity;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 11. 5..
 */

public class MyAssetsAdapter extends RecyclerView.Adapter<MyAssetsAdapter.MyAssetsViewHolder> {
    private ArrayList<MyAssetsItem> mMyAssetsItems;
    private ArrayList<Integer> mProgressBarColors = new ArrayList<>();
    private boolean isAccessible;

    public MyAssetsAdapter() {
        mProgressBarColors.add(R.drawable.drawableBtc);
        mProgressBarColors.add(R.drawable.drawableBch);
        mProgressBarColors.add(R.drawable.drawableEth);
        mProgressBarColors.add(R.drawable.drawableEtc);
        mProgressBarColors.add(R.drawable.drawableXrp);
        mProgressBarColors.add(R.drawable.drawableQtum);
        mProgressBarColors.add(R.drawable.drawableLtc);
    }

    public void setAccessible(boolean accessible) {
        isAccessible = accessible;
    }

    public void setMyAssetsItems(ArrayList<MyAssetsItem> myAssetsItems) {
        mMyAssetsItems = myAssetsItems;
    }

    @Override
    public MyAssetsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyAssetsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_assets_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyAssetsViewHolder holder, int position) {
        holder.bind(mMyAssetsItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mMyAssetsItems == null ? 0 : mMyAssetsItems.size();
    }

    public static class MyAssetsItem {
        public String assetsName;
        public double avail;
        public double balance;
        public long money;
        public int color;

        public MyAssetsItem(String assetsName, double avail, double balance, long money, int color) {
            this.assetsName = assetsName;
            this.avail = avail;
            this.balance = balance;
            this.money = money;
            this.color = color;
        }
    }

    public class MyAssetsViewHolder extends RecyclerView.ViewHolder {
        public MyAssetsItemBinding mBinding;


        public MyAssetsViewHolder(final View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBinding.tvAssetsName.getText().toString().toUpperCase().equals("KRW")
                            || !isAccessible) {
                        return;
                    }
                    itemView.getContext().startActivity(new Intent(itemView.getContext(),
                            TradeActivity.class)
                            .putExtra(TradeActivity.EXTRA_COIN_TYPE, mBinding.tvAssetsName.getText().toString()));
                }
            });
        }

        public void bind(MyAssetsItem myAssetsItem) {
            mBinding.tvAssetsName.setText(myAssetsItem.assetsName);
            mBinding.tvAssetsName.setTextColor(myAssetsItem.color);
            mBinding.tvAvail.setTextColor(myAssetsItem.color);
            mBinding.numberProgressBar.setReachedBarColor(myAssetsItem.color);
            mBinding.tvMoney.setText(String.format("%,d", myAssetsItem.money));

            if (myAssetsItem.assetsName.equals("KRW")) {
                mBinding.tvAvail.setText(String.format("%,d", (int) myAssetsItem.avail));
                mBinding.tvTrading.setText(String.format("%,d", (int) (myAssetsItem.balance - myAssetsItem.avail)));
                mBinding.tvBalance.setVisibility(View.INVISIBLE);
                mBinding.numberProgressBar.setMax((int) myAssetsItem.balance);
                mBinding.numberProgressBar.setProgress((int) myAssetsItem.avail);
            } else {
                mBinding.tvAvail.setText(String.format("%,.4f", myAssetsItem.avail));
                mBinding.tvTrading.setText(String.format("%,.4f", (myAssetsItem.balance - myAssetsItem.avail)));
                mBinding.tvBalance.setText(String.format("%,.4f", myAssetsItem.balance));
                mBinding.tvBalance.setVisibility(View.VISIBLE);
                int max = 1000000;
                int progress = (int) (myAssetsItem.avail * max / myAssetsItem.balance);
                mBinding.numberProgressBar.setMax(max);
                mBinding.numberProgressBar.setProgress(progress);
            }
        }
    }

}

