package com.googry.coinonehelper.ui.setting.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 9. 17..
 */

public class CoinUnitAlarmAdapter extends RecyclerView.Adapter<CoinUnitAlarmAdapter.ViewHolder> {
    private List<CoinType> mCoinTypes;
    private Realm mRealm;
    private long[] mUnits = {1, 10, 20, 50, 100};

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
                mUnitAlarm.divider = CoinType.getCoinDivider(coinType);
            }
            mBinding.setFlag(mUnitAlarm.runFlag);
            mBinding.setUnit(mUnitAlarm.divider);

        }

        public void onUnitClick(View v){
            long divider = CoinType.getCoinDivider(mCoinType);
            final String[] strUnits = new String[mUnits.length];
            for (int i = 0; i < strUnits.length; i++) {
                strUnits[i] = String.valueOf(mUnits[i] * divider);
            }

            new AlertDialog.Builder(mContext)
                    .setItems(strUnits, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            mRealm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    mUnitAlarm.divider = Long.valueOf(strUnits[which]);
                                    realm.copyToRealmOrUpdate(mUnitAlarm);
                                }
                            });
                            mBinding.setUnit(Long.valueOf(strUnits[which]));
                        }
                    })
                    .setTitle("알람 단위를 선택해주세요.")
                    .show();
        }

        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
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
                    mUnitAlarm.runFlag = isChecked;
                    mUnitAlarm.prevPrice = price;
                    realm.copyToRealmOrUpdate(mUnitAlarm);
                }
            });
        }
    }
}
