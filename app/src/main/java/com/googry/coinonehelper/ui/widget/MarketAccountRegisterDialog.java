package com.googry.coinonehelper.ui.widget;

import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinoneUserInfo;
import com.googry.coinonehelper.databinding.MarketAccountRegisterDialogBinding;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 11. 4..
 */

public class MarketAccountRegisterDialog extends DialogFragment {
    public ObservableField<String> accessToken = new ObservableField<>();
    public ObservableField<String> secretKey = new ObservableField<>();
    private MarketAccountRegisterDialogBinding mBinding;

    private OnRequestResultListener mOnRequestResultListener;

    public void setOnRequestResultListener(OnRequestResultListener onRequestResultListener) {
        mOnRequestResultListener = onRequestResultListener;
    }

    public static MarketAccountRegisterDialog newInstance() {

        Bundle args = new Bundle();

        MarketAccountRegisterDialog fragment = new MarketAccountRegisterDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.market_account_register_dialog, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setDialog(this);
        setCancelable(false);
    }

    // databinding
    public void onCancelClick() {
        dismiss();
    }

    // databinding
    public void onRegisterClick() {
        if (TextUtils.isEmpty(accessToken.get())) {
            Toast.makeText(getContext(), R.string.empty_access_token, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(secretKey.get())) {
            Toast.makeText(getContext(), R.string.empty_secret_key, Toast.LENGTH_SHORT).show();
            return;

        }

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.checking_account));
        dialog.show();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                PrefUtil.saveAccessToken(accessToken.get());
                PrefUtil.saveSecretKey(secretKey.get());

                Call<CoinoneUserInfo> call = CoinonePrivateApiUtil.getUserInfo(getContext());
                call.enqueue(new Callback<CoinoneUserInfo>() {
                    @Override
                    public void onResponse(Call<CoinoneUserInfo> call, Response<CoinoneUserInfo> response) {
                        dialog.dismiss();
                        CoinoneUserInfo userInfo = response.body();
                        if (userInfo == null) {
                            Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (userInfo.errorCode.equals("0")) {
                            Toast.makeText(getContext(), R.string.description_register_account, Toast.LENGTH_SHORT).show();
                            MarketAccountRegisterDialog.this.dismiss();
                            if (mOnRequestResultListener != null) {
                                mOnRequestResultListener.onRequestResultListener();
                            }
                            PrefUtil.saveUserInfo(new Gson().toJson(userInfo));
                        } else {
                            Toast.makeText(getContext(), R.string.cant_check_account, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CoinoneUserInfo> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // databinding
    public void onShowMakeTokenKeyClick() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://medium.com/@sjjeong1225/%EC%BD%94%EC%9D%B8%EC%9B%90-%EA%B1%B0%EB%9E%98%EC%86%8C-private-api-key-%EB%A7%8C%EB%93%9C%EB%8A%94-%EB%B0%A9%EB%B2%95-faf8e2f5b158"));
        startActivity(browserIntent);
    }

    public interface OnRequestResultListener{
        void onRequestResultListener();
    }
}
