package com.googry.coinonehelper.data;

/**
 * Created by seokjunjeong on 2017. 6. 17..
 */

public enum CoinType {
    BTC,
    ETH,
    ETC,
    XRP;

    public static String getValue(CoinType coinType) {
        String srtCoinType;
        switch (coinType) {
            case BTC:
                srtCoinType = "btc";
                break;
            case ETH:
                srtCoinType = "eth";
                break;
            case ETC:
                srtCoinType = "etc";
                break;
            case XRP:
                srtCoinType = "xrp";
                break;
            default:
                srtCoinType = "unknown";
                break;
        }
        return srtCoinType;
    }
}
