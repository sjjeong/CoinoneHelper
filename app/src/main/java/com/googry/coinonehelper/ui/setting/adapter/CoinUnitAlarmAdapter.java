package com.googry.coinonehelper.ui.setting.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.R;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.CoinType;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.UnitAlarm;
import com.googry.coinonehelper.databinding.CoinPriceUnitAlarmItemBinding;
import com.googry.coinonehelper.util.PrefUtil;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 9. 17..
 */

public class CoinUnitAlarmAdapter extends RecyclerView.Adapter<CoinUnitAlarmAdapter.ViewHolder> {
    private static final int DEFAULT_UNIT = 10;
    private List<CoinType> mCoinTypes;
    private Realm mRealm;

    public CoinUnitAlarmAdapter() {
        mCoinTypes = Arrays.asList(CoinType.values());
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coin_price_unit_alarm_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mCoinTypes.get(position));
    }

    @Override
    public int getItemCount() {
        return mCoinTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CoinPriceUnitAlarmItemBinding mBinding;
        private CoinType mCoinType;
        private Context mContext;
        private UnitAlarm mUnitAlarm;

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.setViewholder(this);
            mContext = mBinding.getRoot().getContext();
        }

        public void bind(CoinType coinType) {
            mCoinType = coinType;
            mBinding.setType(coinType.name());
            mUnitAlarm = mRealm.where(UnitAlarm.class)
                    .equalTo("coinType", coinType.name())
                    .findFirst();
            if (mUnitAlarm == null) {
                mUnitAlarm = new UnitAlarm();
                mUnitAlarm.coinType = coinType.name();
                mUnitAlarm.runFlag = false;
                mUnitAlarm.divider = CoinType.getCoinDivider(coinType) * DEFAULT_UNIT;
            }
            mBinding.setFlag(mUnitAlarm.runFlag);
            mBinding.setUnit(mUnitAlarm.divider);
            mBinding.etCoinPriceUnit.setEnabled(!mUnitAlarm.runFlag);
        }

        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            String divider = mBinding.etCoinPriceUnit.getText().toString();
            if (TextUtils.isEmpty(divider)) {
                Toast.makeText(mContext, "단위를 입력해 주세요.", Toast.LENGTH_SHORT).show();
                mBinding.setUnit(CoinType.getCoinDivider(mCoinType) * DEFAULT_UNIT);
                mBinding.switchAlarm.setChecked(false);
                return;
            }
            divider = divider.replaceAll(",", "");
            if (divider.length() > 18) {
                Toast.makeText(mContext, "단위가 너무 큽니다.", Toast.LENGTH_SHORT).show();
                mBinding.setUnit(CoinType.getCoinDivider(mCoinType) * DEFAULT_UNIT);
                mBinding.switchAlarm.setChecked(false);
                return;
            }
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    long price;
                    if (BuildConfig.FLAVOR.equals("bithumb")) {
                        BithumbSoloTicker.Ticker ticker = new Gson().fromJson(PrefUtil.loadTicker(mContext, mCoinType), BithumbSoloTicker.Ticker.class);
                        price = ticker.last;
                    } else if (BuildConfig.FLAVOR.equals("korbit")) {
                        KorbitTicker.Ticker ticker = new Gson().fromJson(PrefUtil.loadTicker(mContext, mCoinType), KorbitTicker.Ticker.class);
                        price = ticker.last;
                    } else {
                        CoinoneTicker.Ticker ticker = new Gson().fromJson(PrefUtil.loadTicker(mContext, mCoinType), CoinoneTicker.Ticker.class);
                        price = ticker.last;
                    }
                    mUnitAlarm.divider = Long.parseLong(mBinding.etCoinPriceUnit.getText().toString().replaceAll(",", ""));
                    mUnitAlarm.runFlag = isChecked;
                    mUnitAlarm.prevPrice = price;
                    realm.copyToRealmOrUpdate(mUnitAlarm);
                    mBinding.etCoinPriceUnit.setEnabled(!mUnitAlarm.runFlag);
                }
            });
        }
    }
}
