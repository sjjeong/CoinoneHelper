package com.googry.coinonehelper.ui.coin_notification_add_alarm;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.databinding.CoinNotificationAddAlarmFragmentBinding;
import com.googry.coinonehelper.util.LogUtil;

import io.realm.RealmResults;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public class CoinNotificationAddAlarmFragment extends BaseFragment<CoinNotificationAddAlarmFragmentBinding> implements CoinNotificationAddAlarmContract.View {
    private CoinNotificationAddAlarmContract.Presenter mPresenter;
    private CoinNotificationAddAlarmAdapter mAdapter;

    public static CoinNotificationAddAlarmFragment newInstance() {
        CoinNotificationAddAlarmFragment coinNotificationAddAlarmFragment
                = new CoinNotificationAddAlarmFragment();
        return coinNotificationAddAlarmFragment;
    }

    @Override
    public void setPresenter(CoinNotificationAddAlarmContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.coin_notification_add_alarm_fragment;
    }

    @Override
    protected void initView() {
        mBinding.setPresenter(mPresenter);

        mAdapter = new CoinNotificationAddAlarmAdapter();
        mBinding.rvCoinNotificationList.setAdapter(mAdapter);
    }

    @Override
    protected void newPresenter() {
        new CoinNotificationAddAlarmPresenter(this);
    }

    @Override
    protected void startPresenter() {
        mPresenter.start();
    }

    @Override
    public void showAddAlarmPopup() {
        CoinNotificationAddAlarmDialog addAlarmDialog
                = new CoinNotificationAddAlarmDialog();
        addAlarmDialog.setCancelable(false);
        addAlarmDialog.show(getChildFragmentManager(), addAlarmDialog.getTag());
    }

    @Override
    public void showCoinNotificationList(RealmResults<CoinNotification> realmResults) {
        mAdapter.setData(realmResults);
        mAdapter.notifyDataSetChanged();
        LogUtil.i("showCoinNotificationList");
    }
}
