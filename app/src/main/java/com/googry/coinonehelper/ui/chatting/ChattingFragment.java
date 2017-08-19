package com.googry.coinonehelper.ui.chatting;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseUser;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.ChatMessage;
import com.googry.coinonehelper.databinding.ChattingFragmentBinding;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingFragment extends BaseFragment<ChattingFragmentBinding> implements ChattingContract.View {
    private static final int RC_SIGN_IN = 9001;
    private ChattingContract.Presenter mPresenter;
    private ChattingAdapter mChattingAdapter;

    private GoogleApiClient.OnConnectionFailedListener mFailedListener =
            new GoogleApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                }
            };

    public static ChattingFragment newInstance() {
        ChattingFragment fragment = new ChattingFragment();
        return fragment;
    }

    @Override
    public void setPresenter(ChattingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chatting_fragment;
    }

    @Override
    protected void initView() {
        mBinding.setPresenter(mPresenter);
        mChattingAdapter = new ChattingAdapter();

        LinearLayoutManager layoutManager =
                (LinearLayoutManager) mBinding.rvChatting.getLayoutManager();
        layoutManager.setReverseLayout(true);
        layoutManager.setAutoMeasureEnabled(false);
        mBinding.rvChatting.setLayoutManager(layoutManager);
        mBinding.rvChatting.setAdapter(mChattingAdapter);
    }

    @Override
    protected void newPresenter() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.auth_google_web_client_id))
                .requestEmail()
                .build();
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity(), mFailedListener)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        new ChattingPresenter(this, googleApiClient);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public String getMessage() {
        return mBinding.etMessage.getText().toString();
    }

    @Override
    public void clearEditText() {
        mBinding.etMessage.setText("");
    }

    @Override
    public void showGoogleLogin(GoogleApiClient googleApiClient) {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void showFailedGoogleSignin() {
        Toast.makeText(getContext(), "구글 로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void addMessage(ChatMessage chatMessage) {
        mChattingAdapter.addData(chatMessage);
        mChattingAdapter.notifyDataSetChanged();
    }

    @Override
    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mChattingAdapter.setEmail(firebaseUser.getEmail());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            mPresenter.setGoogleSigninResult(data, getActivity());
        }
    }

}
