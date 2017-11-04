package com.googry.coinonehelper.ui.main.my_assets;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.MyAssetsFragmentBinding;
import com.googry.coinonehelper.ui.setting.SettingActivity;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public class MyAssetsFragment extends BaseFragment<MyAssetsFragmentBinding> implements MyAssetsContract.View {
    private MyAssetsContract.Presenter mPresenter;

    public static MyAssetsFragment newInstance() {

        Bundle args = new Bundle();

        MyAssetsFragment fragment = new MyAssetsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setPresenter(MyAssetsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.my_assets_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void newPresenter() {
        new MyAssetsPresenter(this, getContext());
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void showSettingUi() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.ask_register_account)
                .setPositiveButton(R.string.move, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), SettingActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}
