package com.googry.coinonehelper.ui.main.chatting;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.ChatMessage;
import com.googry.coinonehelper.databinding.ChattingMessageItemBinding;
import com.googry.coinonehelper.util.UIUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChattingViewHolder> {
    private String mEmail = "";
    @NonNull
    private ArrayList<ChatMessage> mData;

    public ChattingAdapter(@NonNull ArrayList<ChatMessage> data) {
        mData = data;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    @Override
    public ChattingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatting_message_item, parent, false);
        return new ChattingViewHolder(view, mEmail);
    }

    @Override
    public void onBindViewHolder(ChattingViewHolder holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ChattingViewHolder extends RecyclerView.ViewHolder {
        private String mEmail;
        private ChattingMessageItemBinding mBinding;

        ChattingViewHolder(View itemView, String email) {
            super(itemView);
            mEmail = email;
            mBinding = DataBindingUtil.bind(itemView);
        }

        public void bind(ChatMessage chatMessage) {
            if (mEmail.equals(chatMessage.email)) {
                mBinding.cvYourMessage.setVisibility(View.GONE);
                mBinding.tvYourTime.setVisibility(View.GONE);
                mBinding.cvMyMessage.setVisibility(View.VISIBLE);
                mBinding.tvMyTime.setVisibility(View.VISIBLE);
                mBinding.tvMyMessage.setText(chatMessage.message);
                mBinding.tvMyTime.setText(UIUtil.formatTime(new Date(chatMessage.time)));
            } else {
                mBinding.cvYourMessage.setVisibility(View.VISIBLE);
                mBinding.tvYourTime.setVisibility(View.VISIBLE);
                mBinding.cvMyMessage.setVisibility(View.GONE);
                mBinding.tvMyTime.setVisibility(View.GONE);
                mBinding.tvUserName.setText(chatMessage.name);
                mBinding.tvYourMessage.setText(chatMessage.message);
                mBinding.tvYourTime.setText(UIUtil.formatTime(new Date(chatMessage.time)));
            }
        }
    }

}
