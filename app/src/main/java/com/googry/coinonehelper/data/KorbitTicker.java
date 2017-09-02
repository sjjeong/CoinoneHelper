package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 7. 13..
 */

public class KorbitTicker {
    public Ticker btc;
    public Ticker bch;
    public Ticker eth;
    public Ticker etc;
    public Ticker xrp;

    public class Ticker {
        @SerializedName("last")
        public long last;
    }

    public class TickerDetailed {
        @SerializedName("volume")
        public double volume;
        @SerializedName("last")
        public long last;
        @SerializedName("high")
        public long high;
        @SerializedName("low")
        public long low;
        @SerializedName("change")
        public long change;
        @SerializedName("changePercent")
        public double changePercent;
    }
}
