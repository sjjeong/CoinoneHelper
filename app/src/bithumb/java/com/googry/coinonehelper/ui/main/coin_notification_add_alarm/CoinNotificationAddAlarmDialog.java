package com.googry.coinonehelper.ui.main.coin_notification_add_alarm;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.databinding.CoinNotificationAddAlarmDialogBinding;
import com.googry.coinonehelper.util.PrefUtil;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinNotificationAddAlarmDialog extends DialogFragment {
    private static final String EXTRA_COIN_NOTIFICATION = "EXTRA_COIN_NOTIFICATION";
    private CoinNotificationAddAlarmDialogBinding mBinding;

    private Realm mRealm;
    private CoinNotification mCoinNotification;
    private OnSaveButtonClick mOnSaveButtonClick;

    private long mNowPrice;
    private CoinType mCoinType;
    private long mDivider;

    public static CoinNotificationAddAlarmDialog newInstance(CoinNotification coinNotification) {
        CoinNotificationAddAlarmDialog dialog = new CoinNotificationAddAlarmDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_COIN_NOTIFICATION, coinNotification);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void setOnSaveButtonClick(OnSaveButtonClick onSaveButtonClick) {
        mOnSaveButtonClick = onSaveButtonClick;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.coin_notification_add_alarm_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setDialog(this);

        mRealm = Realm.getDefaultInstance();

        mCoinNotification = getArguments().getParcelable(EXTRA_COIN_NOTIFICATION);
        if (mCoinNotification == null) {
            mBinding.rbBtc.setChecked(true);
            mCoinType = CoinType.BTC;
            mNowPrice = new Gson().fromJson(PrefUtil.loadTicker(getContext(), CoinType.BTC), BithumbTicker.Ticker.class).last;
            mBinding.setPrice(mNowPrice);
        } else {
            switch (mCoinNotification.getCoinType()) {
                case BTC: {
                    mBinding.rbBtc.setChecked(true);
                    mCoinType = CoinType.BTC;
                }
                case BCH: {
                    mBinding.rbBch.setChecked(true);
                    mCoinType = CoinType.BCH;
                }
                break;
                case ETH: {
                    mBinding.rbEth.setChecked(true);
                    mCoinType = CoinType.ETH;
                }
                break;
                case ETC: {
                    mBinding.rbEtc.setChecked(true);
                    mCoinType = CoinType.ETC;
                }
                break;
                case XRP: {
                    mBinding.rbXrp.setChecked(true);
                    mCoinType = CoinType.XRP;
                }
                break;
                case DASH: {
                    mBinding.rbDash.setChecked(true);
                    mCoinType = CoinType.DASH;
                }
                break;
                case LTC: {
                    mBinding.rbLtc.setChecked(true);
                    mCoinType = CoinType.LTC;
                }
                break;
                case XMR: {
                    mBinding.rbXmr.setChecked(true);
                    mCoinType = CoinType.XRP;
                }
                break;
            }
            mBinding.setPrice(mCoinNotification.getTargetPrice());
        }
        mDivider = CoinType.getCoinDivider(mCoinType);
    }

    // databinding
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        RadioButton btn = (RadioButton) mBinding.getRoot().findViewById(checkedId);
        mCoinType = CoinType.getCoinTypeFromTitle(btn.getText().toString());
        mDivider = CoinType.getCoinDivider(mCoinType);
        mNowPrice = new Gson().fromJson(PrefUtil.loadTicker(getContext(), mCoinType), BithumbTicker.Ticker.class).last;
        mBinding.setPrice(mNowPrice);
    }

    // databinding
    public void onPlusMinusButtonClick(View v, boolean plus) {
        mBinding.setPrice(plus ? mBinding.getPrice() + mDivider : mBinding.getPrice() - mDivider);
    }

    // databinding
    public void onPositiveButtonClick(View v) {
        if (TextUtils.isEmpty(mBinding.etPrice.getText().toString())) {
            Toast.makeText(getContext(), "가격이 비어있습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                long targetPrice = Long.parseLong(mBinding.etPrice.getText().toString().replace(",", ""));
                if (targetPrice % mDivider != 0) {
                    Toast.makeText(getContext(), String.format("가격이 %d으로 나누어 떨어지게 적어주세요.", mDivider), Toast.LENGTH_LONG).show();
                    return;
                }

                CoinNotification.PriceDirection direction;

                if (targetPrice > mNowPrice) {
                    direction = CoinNotification.PriceDirection.Up;
                } else {
                    direction = CoinNotification.PriceDirection.Down;
                }


                CoinNotification coinNotification = mCoinNotification;
                if (coinNotification == null) {
                    coinNotification = new CoinNotification();
                    coinNotification.setCreatedTs(System.currentTimeMillis());
                }
                coinNotification.setCoinType(mCoinType);
                coinNotification.setTargetPrice(targetPrice);
                coinNotification.setPriceDirection(direction);
                coinNotification.setUpdatedTs(System.currentTimeMillis());
                realm.copyToRealmOrUpdate(coinNotification);
                mOnSaveButtonClick.onSaveButtonClick(coinNotification);
                dismiss();
            }
        });
    }

    // databinding
    public void onNegativeButtonClick(View v) {
        dismiss();
    }


    public interface OnSaveButtonClick {
        void onSaveButtonClick(CoinNotification coinNotification);
    }
}
