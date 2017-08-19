package com.googry.coinonehelper.ui.main.chatting;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseUser;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.ChatMessage;
import com.googry.coinonehelper.databinding.ChattingFragmentBinding;
import com.googry.coinonehelper.ui.setting.SettingActivity;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 8. 15..
 */

public class ChattingFragment extends BaseFragment<ChattingFragmentBinding> implements ChattingContract.View {
    private ChattingContract.Presenter mPresenter;
    private ChattingAdapter mChattingAdapter;
    private ArrayList<ChatMessage> mChatMessages;
    private ProgressDialog mChatLoding;


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

        mChatMessages = new ArrayList<>();
        mChatLoding = new ProgressDialog(getContext());
        mChatLoding.setMessage("채팅을 불러오는 중입니다.");
        mChatLoding.show();

        LinearLayoutManager layoutManager =
                (LinearLayoutManager) mBinding.rvChatting.getLayoutManager();
        layoutManager.setReverseLayout(true);
        layoutManager.setAutoMeasureEnabled(false);
        mBinding.rvChatting.setLayoutManager(layoutManager);
    }

    @Override
    protected void newPresenter() {
        new ChattingPresenter(this);
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
    public void addMessage(ChatMessage chatMessage) {
        if(mChatLoding != null && mChatLoding.isShowing()){
            mChatLoding.dismiss();
        }
        mChatMessages.add(0, chatMessage);
        mChattingAdapter.notifyDataSetChanged();
    }

    @Override
    public void showSettingUi() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setMessage("채팅을 하기 위해서는 로그인이 필요합니다.\n로그인 화면으로 이동하시겠습니까?")
                .setPositiveButton("이동", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), SettingActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void setFirebaseUser(FirebaseUser firebaseUser) {
        mChattingAdapter.setEmail(firebaseUser.getEmail());
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.setFragmentStart();
        mChattingAdapter = new ChattingAdapter(mChatMessages);
        mBinding.rvChatting.setAdapter(mChattingAdapter);
    }

    @Override
    public void onStop() {
        mChatMessages.clear();
        mChattingAdapter.notifyDataSetChanged();
        mPresenter.setFragmentStop();
        super.onStop();
    }
}
