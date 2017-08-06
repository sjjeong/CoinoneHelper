package com.googry.coinonehelper.ui.coin_notification_add_alarm;

import android.databinding.DataBindingUtil;
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
    private CoinNotificationAddAlarmDialogBinding mBinding;

    private Realm mRealm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.coin_notification_add_alarm_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(view);
        mBinding.setDialog(this);
        mBinding.rbBtc.setChecked(true);

        mRealm = Realm.getDefaultInstance();
    }

    public void onPositiveButtonClick(View v) {
        if (TextUtils.isEmpty(mBinding.etPrice.getText().toString())) {
            Toast.makeText(getContext(), "가격이 비어있습니다.", Toast.LENGTH_LONG).show();
            return;
        }
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                CoinNotification coinNotification = realm.createObject(CoinNotification.class);
                CoinType coinType = CoinType.BTC;
                switch (mBinding.rgCoinGroup.getCheckedRadioButtonId()) {
                    case R.id.rb_btc: {
                        coinType = CoinType.BTC;
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

                long targetPrice = Long.parseLong(mBinding.etPrice.getText().toString());
                long divider = 1;
                switch (coinType){
                    case BTC:{
                        divider = 500;
                    }
                    break;
                    case ETH:{
                        divider = 50;
                    }
                    break;
                    case ETC:{
                        divider = 10;
                    }
                    break;
                    case XRP:{
                        divider = 1;
                    }
                    break;
                }
                if(targetPrice % divider != 0){
                    Toast.makeText(getContext(), String.format("가격이 %d으로 나누어 떨어지게 적어주세요.",divider),Toast.LENGTH_LONG).show();
                    return;
                }

                CoinNotification.PriceDirection direction;

                long nowPrice = new Gson().fromJson(PrefUtil.loadTicker(getContext(), coinType), CoinoneTicker.Ticker.class).last;
                if(targetPrice > nowPrice){
                    direction = CoinNotification.PriceDirection.Up;
                } else {
                    direction = CoinNotification.PriceDirection.Down;
                }

                coinNotification.setCoinType(coinType);
                coinNotification.setTargetPrice(targetPrice);
                coinNotification.setPriceDirection(direction);
                coinNotification.setCreatedTs(System.currentTimeMillis());
                Toast.makeText(getContext(), "저장에 성공했습니다.", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }

    public void onNegativeButtonClick(View v) {
        dismiss();
    }
}
