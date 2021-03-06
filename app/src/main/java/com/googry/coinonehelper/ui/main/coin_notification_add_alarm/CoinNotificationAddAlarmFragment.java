package com.googry.coinonehelper.ui.main.coin_notification_add_alarm;

import android.view.View;
import android.widget.Toast;

import com.googry.coinonehelper.R;
import com.googry.coinonehelper.base.ui.BaseFragment;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.databinding.CoinNotificationAddAlarmFragmentBinding;
import com.googry.coinonehelper.ui.OnItemClickListener;
import com.googry.coinonehelper.util.LogUtil;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by seokjunjeong on 2017. 7. 15..
 */

public class CoinNotificationAddAlarmFragment
        extends BaseFragment<CoinNotificationAddAlarmFragmentBinding>
        implements CoinNotificationAddAlarmContract.View, View.OnClickListener {
    private CoinNotificationAddAlarmContract.Presenter mPresenter;
    private CoinNotificationAddAlarmAdapter mAdapter;
    private Realm mRealm;
    private OnItemClickListener mDeleteListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    mAdapter.getData().get(position).deleteFromRealm();
                    Toast.makeText(getContext(), "알람 삭제", Toast.LENGTH_LONG).show();
                    mAdapter.notifyDataSetChanged();

                    RealmResults<CoinNotification> realmResults =
                            realm.where(CoinNotification.class)
                                    .findAll();
                    mBinding.tvDontHaveAlarm.setVisibility(realmResults.size() == 0 ?
                            View.VISIBLE : View.GONE);
                }
            });

        }
    };
    private CoinNotificationAddAlarmDialog.OnSaveButtonClick mOnSaveButtonClick =
            new CoinNotificationAddAlarmDialog.OnSaveButtonClick() {
                @Override
                public void onSaveButtonClick(final CoinNotification coinNotification) {
                    Toast.makeText(getContext(), "저장에 성공했습니다.", Toast.LENGTH_LONG).show();
                    mAdapter.notifyDataSetChanged();
                    mBinding.tvDontHaveAlarm.setVisibility(View.GONE);
                }
            };
    private OnItemClickListener mModifyListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {
            showAddAlarmPopup(mAdapter.getData().get(position));
        }
    };

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
        mRealm = Realm.getDefaultInstance();
        mBinding.setPresenter(mPresenter);

        mAdapter = new CoinNotificationAddAlarmAdapter(mDeleteListener, mModifyListener);
        mBinding.rvCoinNotificationList.setAdapter(mAdapter);
        mBinding.fabAddAlarm.setOnClickListener(this);
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
    public void showAddAlarmPopup(CoinNotification coinNotification) {
        LogUtil.e("show");
        CoinNotificationAddAlarmDialog addAlarmDialog =
                CoinNotificationAddAlarmDialog.newInstance(coinNotification);

        addAlarmDialog.setCancelable(false);
        addAlarmDialog.setOnSaveButtonClick(mOnSaveButtonClick);
        addAlarmDialog.show(getChildFragmentManager(), addAlarmDialog.getTag());
    }

    @Override
    public void showCoinNotificationList(RealmResults<CoinNotification> realmResults) {
        mAdapter.setData(realmResults);
        mAdapter.notifyDataSetChanged();
        mBinding.tvDontHaveAlarm.setVisibility(realmResults.size() == 0 ?
                View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        showAddAlarmPopup(null);
    }
}
