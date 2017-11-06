package com.googry.coinonehelper.ui.main.my_assets;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinoneBalance;
import com.googry.coinonehelper.util.CoinonePrivateApiUtil;
import com.googry.coinonehelper.util.PrefUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public class MyAssetsPresenter implements MyAssetsContract.Presenter {
    private MyAssetsContract.View mView;

    private Context mContext;

    public MyAssetsPresenter(MyAssetsContract.View view,
                             Context context) {
        mView = view;
        mView.setPresenter(this);
        mContext = context;
    }

    @Override
    public void start() {

    }

    @Override
    public void loadBalance() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Call<CoinoneBalance> call = CoinonePrivateApiUtil.getBalance(mContext);
                call.enqueue(new Callback<CoinoneBalance>() {
                    @Override
                    public void onResponse(Call<CoinoneBalance> call, Response<CoinoneBalance> response) {
                        CoinoneBalance balance = response.body();
                        if (balance == null) {
                            return;
                        }
                        if (balance.errorCode.equals("0")) {
                            mView.hideLoadingDialog();
                            mView.showBalance(balance);
                            return;
                        }
                        mView.hideLoadingDialog();
                        Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<CoinoneBalance> call, Throwable t) {
                        mView.hideLoadingDialog();
                        Toast.makeText(mContext, R.string.server_error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    public void checkRegisterAccount() {
        if (PrefUtil.loadRegisterAccount(mContext)) {
            mView.showLoadingDialog();
            loadBalance();
        } else {
            mView.showSettingUi();
        }
    }
}
