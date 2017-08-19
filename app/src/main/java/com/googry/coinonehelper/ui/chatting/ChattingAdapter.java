package com.googry.coinonehelper.ui.chatting;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.ChatMessage;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingAdapter extends RecyclerView.Adapter<ChattingViewHolder> {

    private String mEmail;
    @NonNull
    private ArrayList<ChatMessage> mData;

    public void addData(ChatMessage chatMessage) {
        mData.add(0,chatMessage);
    }

    public ChattingAdapter() {

        mData = new ArrayList<>();
    }

    public ChattingAdapter(String email) {
        mEmail = email;
        mData = new ArrayList<>();
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
}
