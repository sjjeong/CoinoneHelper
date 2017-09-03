package com.googry.coinonehelper.data;

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
    XRP,
    QTUM;

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
            case QTUM:
                return R.string.qtum;
            default:
                return R.string.btc;
        }
    }

}
