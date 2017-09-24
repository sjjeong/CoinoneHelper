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
import com.googry.coinonehelper.databinding.CoinPriceUnitAlarmItemBinding;
import com.googry.coinonehelper.util.LogUtil;
import com.googry.coinonehelper.util.PrefUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 9. 17..
 */

public class CoinUnitAlarmAdapter extends RecyclerView.Adapter<CoinUnitAlarmAdapter.ViewHolder> {
    private List<CoinType> mCoinTypes;

    public CoinUnitAlarmAdapter() {
        mCoinTypes = Arrays.asList(CoinType.values());
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
        private long[] mUnits = {1, 10, 20, 50, 100};

        public ViewHolder(View itemView) {
            super(itemView);
            mBinding = DataBindingUtil.bind(itemView);
            mBinding.setViewholder(this);
            mContext = mBinding.getRoot().getContext();
        }

        public void bind(CoinType coinType) {
            mCoinType = coinType;
            mBinding.setType(coinType.name());
            mBinding.setFlag(PrefUtil.loadCoinUnitAlarmFlag(mContext, coinType));
            mBinding.setUnit(PrefUtil.loadCoinUnitAlarm(mContext, coinType));

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
                        public void onClick(DialogInterface dialog, int which) {
                            PrefUtil.saveCoinUnitAlarm(mContext, mCoinType, Long.valueOf(strUnits[which]));
                            mBinding.setUnit(Long.valueOf(strUnits[which]));
                        }
                    })
                    .setTitle("알람 단위를 선택해주세요.")
                    .show();
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            PrefUtil.saveCoinUnitAlarmFlag(mContext, mCoinType, isChecked);
            long price = 0;
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
            PrefUtil.saveCoinUnitAlarmPrice(mContext, mCoinType, price);
        }
    }
}
