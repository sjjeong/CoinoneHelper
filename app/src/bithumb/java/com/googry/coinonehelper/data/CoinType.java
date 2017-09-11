package com.googry.coinonehelper.data;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.googry.coinonehelper.R;

/**
 * Created by seokjunjeong on 2017. 6. 17..
 */

public enum CoinType {
    BTC,
    ETH,
    DASH,
    LTC,
    ETC,
    XRP,
    BCH,
    XMR;

    @StringRes
    public static int getCoinTitleRes(CoinType coinType) {
        switch (coinType) {
            case BTC:
                return R.string.btc;
            case ETH:
                return R.string.eth;
            case DASH:
                return R.string.dash;
            case LTC:
                return R.string.ltc;
            case ETC:
                return R.string.etc;
            case XRP:
                return R.string.xrp;
            case BCH:
                return R.string.bch;
            case XMR:
                return R.string.xmr;
            default:
                return R.string.empty;
        }
    }


    @DrawableRes
    public static int getCoinIconRes(CoinType coinType) {
        switch (coinType) {
            case BTC:
                return R.drawable.bitcoin;
            case ETH:
                return R.drawable.ethereum;
            case DASH:
                return R.drawable.dash;
            case LTC:
                return R.drawable.litecoin;
            case ETC:
                return R.drawable.ethereumclassic;
            case XRP:
                return R.drawable.ripple;
            case BCH:
                return R.drawable.bitcoincash;
            case XMR:
                return R.drawable.monero;
            default:
                return R.drawable.bitcoin;
        }
    }

}
