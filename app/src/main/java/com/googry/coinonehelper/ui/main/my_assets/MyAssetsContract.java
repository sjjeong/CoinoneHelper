package com.googry.coinonehelper.ui.main.my_assets;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;
import com.googry.coinonehelper.data.CoinoneBalance;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public interface MyAssetsContract {

    interface Presenter extends BasePresenter{
        void loadBalance();

        void checkRegisterAccount();
    }

    interface View extends BaseView<Presenter> {
        void showSettingUi();

        void showBalance(CoinoneBalance balance);

        void showLoadingDialog();

        void hideLoadingDialog();
    }

}
