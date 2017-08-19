package com.googry.coinonehelper.ui.main.chatting;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.googry.coinonehelper.data.ChatMessage;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingPresenter implements ChattingContract.Presenter {
    public static final String MESSAGES_CHILD = "messages";
    private ChattingContract.View mView;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;

    public ChattingPresenter(ChattingContract.View view,
                             GoogleApiClient googleApiClient) {
        mView = view;
        mView.setPresenter(this);
        mGoogleApiClient = googleApiClient;
    }

    @Override
    public void start() {
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (!checkUserSignin()) {
            return;
        }
        mView.setFirebaseUser(mFirebaseUser);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabaseReference.child(MESSAGES_CHILD).limitToFirst(100);
        query.addChildEventListener(new ChildEventListener() {
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
        });
    }

    private boolean checkUserSignin() {
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            mView.showGoogleLogin(mGoogleApiClient);
            return false;
        }
        return true;
    }

    @Override
    public void sendMessage() {
        if (!checkUserSignin()) {
            return;
        }
        if (TextUtils.isEmpty(mView.getMessage())) {
            return;
        }
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.email = mFirebaseUser.getEmail();
        chatMessage.name = mFirebaseUser.getEmail();
        chatMessage.message = mView.getMessage();
        chatMessage.date = System.currentTimeMillis();

        mDatabaseReference.child(MESSAGES_CHILD)
                .push().setValue(chatMessage);
        mView.clearEditText();

    }

    @Override
    public void setGoogleSigninResult(Intent data, Activity activity) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            // Google Sign-In was successful, authenticate with Firebase
            GoogleSignInAccount account = result.getSignInAccount();
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            mFirebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                mView.showFailedGoogleSignin();
                            } else {
                                start();
                            }
                        }
                    });
        } else {
            // Google Sign-In failed
            mView.showFailedGoogleSignin();
        }
    }
}
