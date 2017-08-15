package com.googry.coinonehelper.ui.chatting;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
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

        void setGoogleSigninResult(Intent data, Activity activity);

    }

    interface View extends BaseView<Presenter> {
        String getMessage();

        void clearEditText();

        void showGoogleLogin(GoogleApiClient googleApiClient);

        void showFailedGoogleSignin();

        void addMessage(ChatMessage chatMessage);

        void setFirebaseUser(FirebaseUser firebaseUser);
    }
}
