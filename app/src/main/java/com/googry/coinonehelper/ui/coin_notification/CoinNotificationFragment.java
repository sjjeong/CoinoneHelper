package com.googry.coinonehelper.ui.coin_notification;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.databinding.CoinNotificationFragmentBinding;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public class CoinNotificationFragment extends BaseFragment<CoinNotificationFragmentBinding> implements CoinNotificationContract.View {
    private CoinNotificationContract.Presenter mPresenter;

    public static CoinNotificationFragment newInstance(){
        CoinNotificationFragment coinNotificationFragment
                = new CoinNotificationFragment();
        return coinNotificationFragment;
    }

    @Override
    public void setPresenter(CoinNotificationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.coin_notification_fragment;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void newPresenter() {
        new CoinNotificationPresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }
}
