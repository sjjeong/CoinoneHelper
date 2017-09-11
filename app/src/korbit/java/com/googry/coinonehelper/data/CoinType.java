package com.googry.coinonehelper.data;

import android.support.annotation.DrawableRes;
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
}
