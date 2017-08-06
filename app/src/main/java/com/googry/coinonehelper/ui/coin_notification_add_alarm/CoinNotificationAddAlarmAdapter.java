package com.googry.coinonehelper.ui.coin_notification_add_alarm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.databinding.CoinNotificationAddAlarmListItemBinding;

import io.realm.RealmResults;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinNotificationAddAlarmAdapter extends RecyclerView.Adapter<CoinNotificationAddAlarmAdapter.ViewHolder> {
    private RealmResults<CoinNotification> mData;

    public void setData(RealmResults<CoinNotification> data) {
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_notification_add_alarm_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CoinNotificationAddAlarmListItemBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(CoinNotification coinNotification) {
            mBinding.setCointype(coinNotification.getCoinType().name());
            mBinding.setPrice(coinNotification.getTargetPrice());
        }

    }
}
