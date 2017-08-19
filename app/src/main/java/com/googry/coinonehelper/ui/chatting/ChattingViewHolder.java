package com.googry.coinonehelper.ui.chatting;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.ChatMessage;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingViewHolder extends RecyclerView.ViewHolder {
    private String mEmail;
    private TextView mTvUserName, mTvYouMessage, mTvMeMessage;
    private CardView mCvYouMessage, mCvMeMessage;

    public ChattingViewHolder(View itemView, String email) {
        super(itemView);
        mEmail = email;
        mTvUserName = (TextView) itemView.findViewById(R.id.tv_user_name);
        mTvYouMessage = (TextView) itemView.findViewById(R.id.tv_you_message);
        mTvMeMessage = (TextView) itemView.findViewById(R.id.tv_me_message);
        mCvYouMessage = (CardView) itemView.findViewById(R.id.cv_you_message);
        mCvMeMessage = (CardView) itemView.findViewById(R.id.cv_me_message);
    }

    public void bind(ChatMessage chatMessage) {
        if (mEmail.equals(chatMessage.email)) {
            mCvYouMessage.setVisibility(View.GONE);
            mCvMeMessage.setVisibility(View.VISIBLE);
            mTvMeMessage.setText(chatMessage.message);
        } else {
            mCvYouMessage.setVisibility(View.VISIBLE);
            mCvMeMessage.setVisibility(View.GONE);
            mTvUserName.setText(chatMessage.name);
            mTvYouMessage.setText(chatMessage.message);
        }
    }
}
