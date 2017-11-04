package com.googry.coinonehelper.ui.main.my_assets;

import android.content.Context;
import android.text.TextUtils;

import com.googry.coinonehelper.util.PrefUtil;

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
        checkRegisterAccount();
    }

    private void checkRegisterAccount() {
        String accessToken = PrefUtil.loadAccessToken(mContext);
        String secretKey = PrefUtil.loadSecretKey(mContext);
        if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(secretKey)) {
            mView.showSettingUi();
        }
    }
}
