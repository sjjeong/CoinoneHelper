package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class BithumbTicker {
    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("BTC")
        public Ticker btc;
        @SerializedName("BCH")
        public Ticker bch;
        @SerializedName("ETH")
        public Ticker eth;
        @SerializedName("DASH")
        public Ticker dash;
        @SerializedName("LTC")
        public Ticker ltc;
        @SerializedName("ETC")
        public Ticker etc;
        @SerializedName("XRP")
        public Ticker xrp;
        @SerializedName("XMR")
        public Ticker xmr;
        @SerializedName("ZEC")
        public Ticker zec;
        @SerializedName("QTUM")
        public Ticker qtum;
    }

    public class Ticker {
        @SerializedName("closing_price")
        public long last;
    }
}
