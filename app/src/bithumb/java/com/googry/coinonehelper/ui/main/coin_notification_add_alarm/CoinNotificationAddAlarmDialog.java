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
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.CoinNotification;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.databinding.CoinNotificationAddAlarmDialogBinding;
import com.googry.coinonehelper.util.PrefUtil;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinNotificationAddAlarmDialog extends DialogFragment {
    private static final String EXTRA_COIN_NOTIFICATION = "EXTRA_COIN_NOTIFICATION";
    private static final long DIVIDER_BTC = 500;
    private static final long DIVIDER_BCH = 100;
    private static final long DIVIDER_ETH = 50;
    private static final long DIVIDER_ETC = 10;
    private static final long DIVIDER_XRP = 1;
    private static final long DIVIDER_QTUM = 10;
    private CoinNotificationAddAlarmDialogBinding mBinding;

    private Realm mRealm;
    private CoinNotification mCoinNotification;
    private OnSaveButtonClick mOnSaveButtonClick;

    private long mNowPrice;
    private CoinType mCoinType;

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
            mNowPrice = new Gson().fromJson(PrefUtil.loadTicker(getContext(), CoinType.BTC), CoinoneTicker.Ticker.class).last;
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
            }
            mBinding.setPrice(mCoinNotification.getTargetPrice());
        }
    }

    // databinding
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.rb_btc: {
                mCoinType = CoinType.BTC;
            }
            break;
            case R.id.rb_bch: {
                mCoinType = CoinType.BCH;
            }
            break;
            case R.id.rb_eth: {
                mCoinType = CoinType.ETH;
            }
            break;
            case R.id.rb_etc: {
                mCoinType = CoinType.ETC;
            }
            break;
            case R.id.rb_xrp: {
                mCoinType = CoinType.XRP;
            }
            break;
        }
        mNowPrice = new Gson().fromJson(PrefUtil.loadTicker(getContext(), mCoinType), CoinoneTicker.Ticker.class).last;
        mBinding.setPrice(mNowPrice);
    }

    // databinding
    public void onPlusMinusButtonClick(View v, boolean plus) {
        long price = mBinding.getPrice();
        switch (mCoinType) {
            case BTC: {
                price += plus ? DIVIDER_BTC : -DIVIDER_BTC;
            }
            break;
            case BCH: {
                price += plus ? DIVIDER_BCH : -DIVIDER_BCH;
            }
            break;
            case ETH: {
                price += plus ? DIVIDER_ETH : -DIVIDER_ETH;
            }
            break;
            case ETC: {
                price += plus ? DIVIDER_ETC : -DIVIDER_ETC;
            }
            break;
            case XRP: {
                price += plus ? DIVIDER_XRP : -DIVIDER_XRP;
            }
            break;
        }
        mBinding.setPrice(price);
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

                CoinType coinType = CoinType.BTC;
                switch (mBinding.rgCoinGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_btc: {
                        coinType = CoinType.BTC;
                    }
                    break;
                    case R.id.rb_bch: {
                        coinType = CoinType.BCH;
                    }
                    break;
                    case R.id.rb_eth: {
                        coinType = CoinType.ETH;
                    }
                    break;
                    case R.id.rb_etc: {
                        coinType = CoinType.ETC;
                    }
                    break;
                    case R.id.rb_xrp: {
                        coinType = CoinType.XRP;
                    }
                    break;
                }

                long targetPrice = Long.parseLong(mBinding.etPrice.getText().toString().replace(",", ""));
                long divider = 1;
                switch (coinType) {
                    case BTC: {
                        divider = DIVIDER_BTC;
                    }
                    break;
                    case BCH: {
                        divider = DIVIDER_BCH;
                    }
                    break;
                    case ETH: {
                        divider = DIVIDER_ETH;
                    }
                    break;
                    case ETC: {
                        divider = DIVIDER_ETC;
                    }
                    break;
                    case XRP: {
                        divider = DIVIDER_XRP;
                    }
                    break;
                }
                if (targetPrice % divider != 0) {
                    Toast.makeText(getContext(), String.format("가격이 %d으로 나누어 떨어지게 적어주세요.", divider), Toast.LENGTH_LONG).show();
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
                coinNotification.setCoinType(coinType);
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
