package com.googry.coinonehelper.ui.chatting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.ChatMessage;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingViewHolder extends RecyclerView.ViewHolder {
    private String mUid;
    private TextView mTvUserName, mTvYouMessage, mTvMeMessage;

    public ChattingViewHolder(View itemView, String uid) {
        super(itemView);
        mUid = uid;
        mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
        mTvYouMessage = (TextView) itemView.findViewById(R.id.tv_you_message);
        mTvMeMessage = (TextView) itemView.findViewById(R.id.tv_me_message);
    }

    public void bind(ChatMessage chatMessage) {
        if (mUid.equals(chatMessage.uid)) {
            mTvUserName.setVisibility(View.GONE);
            mTvYouMessage.setVisibility(View.GONE);
            mTvMeMessage.setVisibility(View.VISIBLE);
            mTvMeMessage.setText(chatMessage.message);
        } else {
            mTvUserName.setVisibility(View.VISIBLE);
            mTvYouMessage.setVisibility(View.VISIBLE);
            mTvMeMessage.setVisibility(View.GONE);
            mTvUserName.setText(chatMessage.userName);
            mTvYouMessage.setText(chatMessage.message);
        }
    }
}
