package com.googry.coinonehelper.data;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import com.googry.coinonehelper.R;


/**
 * Created by seokjunjeong on 2017. 6. 17..
 */

public enum CoinType {
    BTC,
    BCH,
    ETH,
    ETC,
    XRP;

    @StringRes
    public static int getCoinTitleRes(CoinType coinType) {
        switch (coinType) {
            case BTC:
                return R.string.btc;
            case BCH:
                return R.string.bch;
            case ETH:
                return R.string.eth;
            case ETC:
                return R.string.etc;
            case XRP:
                return R.string.xrp;
            default:
                return R.string.empty;
        }
    }

    @Nullable
    public static CoinType getCoinTypeFromTitle(String title){
        switch (title) {
            case "BTC":
                return BTC;
            case "BCH":
                return BCH;
            case "ETH":
                return ETH;
            case "ETC":
                return ETC;
            case "XRP":
                return XRP;
            default:
                return null;
        }
    }

    @DrawableRes
    public static int getCoinIconRes(CoinType coinType) {
        switch (coinType) {
            case BTC:
                return R.drawable.bitcoin;
            case BCH:
                return R.drawable.bitcoincash;
            case ETH:
                return R.drawable.ethereum;
            case ETC:
                return R.drawable.ethereumclassic;
            case XRP:
                return R.drawable.ripple;
            default:
                return R.drawable.bitcoin;
        }
    }
    public static long getCoinDivider(CoinType coinType){
        switch (coinType) {
            case BTC:
                return 500;
            case BCH:
                return 500;
            case ETH:
                return 50;
            case ETC:
                return 10;
            case XRP:
                return 1;
            default:
                return 1;
        }
    }
}
