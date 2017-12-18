package com.googry.coinonehelper.ui.main.chatting;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.googry.coinonehelper.data.ChatMessage;
import com.googry.coinonehelper.util.LogUtil;

import java.util.Date;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingPresenter implements ChattingContract.Presenter {
    public static final String MESSAGES_CHILD = "messages";
    private ChattingContract.View mView;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private Query mQuery;
    private FirebaseAuth.AuthStateListener mAuthStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            mFirebaseUser = firebaseAuth.getCurrentUser();
            if (mFirebaseUser != null) {
                // User is signed in
                mView.setFirebaseUser(mFirebaseUser);

            } else {
                // User is signed out
                mView.showSettingUi();
            }
        }
    };
    private ChildEventListener mChlidEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            ChatMessage message = dataSnapshot.getValue(ChatMessage.class);
            mView.addMessage(message);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChattingPresenter(ChattingContract.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase
                .getInstance()
                .getReference();
        mQuery = mDatabaseReference.child(MESSAGES_CHILD)
                .limitToLast(50);
    }

    private boolean checkUserSignin() {
        return mFirebaseUser != null;
    }

    @Override
    public void sendMessage() {
        if (!checkUserSignin()) {
            mView.showSettingUi();
            return;
        }
        if (TextUtils.isEmpty(mView.getMessage())) {
            return;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.email = mFirebaseUser.getEmail();
        chatMessage.name = mFirebaseUser.getDisplayName();
        chatMessage.message = mView.getMessage();
        chatMessage.time = new Date(System.currentTimeMillis()).toString();

        mDatabaseReference.child(MESSAGES_CHILD)
                .push().setValue(chatMessage);
        mView.clearEditText();

    }

    @Override
    public void setFragmentStart() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
        mQuery.addChildEventListener(mChlidEventListener);

    }

    @Override
    public void setFragmentStop() {
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
        mQuery.removeEventListener(mChlidEventListener);
    }
}
