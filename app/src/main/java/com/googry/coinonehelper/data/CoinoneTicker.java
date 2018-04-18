package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 6. 17..
 */

public class CoinoneTicker {
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("btc")
    public Ticker btc;
    @SerializedName("bch")
    public Ticker bch;
    @SerializedName("eth")
    public Ticker eth;
    @SerializedName("etc")
    public Ticker etc;
    @SerializedName("xrp")
    public Ticker xrp;
    @SerializedName("qtum")
    public Ticker qtum;
    @SerializedName("ltc")
    public Ticker ltc;
    @SerializedName("iota")
    public Ticker iota;
    @SerializedName("btg")
    public Ticker btg;
    @SerializedName("omg")
    public Ticker omg;


    public class Ticker {
        @SerializedName("volume")
        public double volume;
        @SerializedName("last")
        public long last;
        @SerializedName("first")
        public long first;
        @SerializedName("high")
        public long high;
        @SerializedName("low")
        public long low;
        @SerializedName("currency")
        public String currency;
    }

}
