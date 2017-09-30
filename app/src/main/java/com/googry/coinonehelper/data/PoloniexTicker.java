package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 7. 14..
 */

public class PoloniexTicker {
    @SerializedName("USDT_BTC")
    public Ticker usdtBtc;
    @SerializedName("USDT_BCH")
    public Ticker usdtBch;
    @SerializedName("USDT_ETH")
    public Ticker usdtEth;
    @SerializedName("USDT_ETC")
    public Ticker usdtEtc;
    @SerializedName("USDT_XRP")
    public Ticker usdtXrp;
    @SerializedName("USDT_LTC")
    public Ticker usdtLtc;
    @SerializedName("USDT_DASH")
    public Ticker usdtDash;
    @SerializedName("USDT_XMR")
    public Ticker usdtXmr;
    @SerializedName("USDT_ZEC")
    public Ticker usdtZec;
    @SerializedName("BTC_BCH")
    public Ticker btcBch;
    @SerializedName("BTC_ETH")
    public Ticker btcEth;
    @SerializedName("BTC_ETC")
    public Ticker btcEtc;
    @SerializedName("BTC_XRP")
    public Ticker btcXrp;
    @SerializedName("BTC_LTC")
    public Ticker btcLtc;
    @SerializedName("BTC_DASH")
    public Ticker btcDash;
    @SerializedName("BTC_XMR")
    public Ticker btcXmr;
    @SerializedName("BTC_ZEC")
    public Ticker btcZec;

    public class Ticker {
        @SerializedName("last")
        public double last;
    }
}
