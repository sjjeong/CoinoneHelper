package com.googry.coinonehelper.ui.main.chatting;

import com.google.firebase.auth.FirebaseUser;
import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.ChatMessage;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public interface ChattingContract {
    interface Presenter extends BasePresenter {
        void sendMessage();

        void setFragmentStart();

        void setFragmentStop();
    }

    interface View extends BaseView<Presenter> {
        String getMessage();

        void clearEditText();

        void addMessage(ChatMessage chatMessage);

        void showSettingUi();

        void setFirebaseUser(FirebaseUser firebaseUser);
    }
}
