package com.googry.coinonehelper.ui.main.coin_notification_add_alarm;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.databinding.CoinNotificationAddAlarmListItemBinding;
import com.googry.coinonehelper.ui.OnItemClickListener;

import io.realm.RealmResults;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinNotificationAddAlarmAdapter extends RecyclerView.Adapter<CoinNotificationAddAlarmAdapter.ViewHolder> {
    private RealmResults<CoinNotification> mData;
    private OnItemClickListener mDeleteListener, mModifyListener;

    public CoinNotificationAddAlarmAdapter(OnItemClickListener deleteListener, OnItemClickListener modifyListener) {
        mDeleteListener = deleteListener;
        mModifyListener = modifyListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_notification_add_alarm_list_item, parent, false);
        view.findViewById(R.id.btn_modify).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);
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

    public RealmResults<CoinNotification> getData() {
        return mData;
    }

    public void setData(RealmResults<CoinNotification> data) {
        mData = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CoinNotificationAddAlarmListItemBinding mBinding;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.setViewholder(this);
            mBinding.setChecked(false);
        }

        public void bind(CoinNotification coinNotification) {
            mBinding.setCointype(coinNotification.getCoinType().name());
            mBinding.setPrice(coinNotification.getTargetPrice());
        }

        public void onViewClick(View v) {
            mBinding.setChecked(!mBinding.getChecked());
        }

        public void onDeleteClick(View v) {
            mBinding.setChecked(false);
            mDeleteListener.onItemClick(getAdapterPosition());
        }

        public void onModifyClick(View v) {
            mModifyListener.onItemClick(getAdapterPosition());
        }
    }

}
