package com.googry.coinonehelper.ui.main.my_assets;

import com.googry.coinonehelper.base.BasePresenter;
import com.googry.coinonehelper.base.BaseView;

/**
 * Created by seokjunjeong on 2017. 10. 28..
 */

public interface MyAssetsContract {

    interface Presenter extends BasePresenter{

    }

    interface View extends BaseView<Presenter> {
        void showSettingUi();
    }

}
