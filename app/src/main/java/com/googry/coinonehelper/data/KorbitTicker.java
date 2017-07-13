package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 7. 13..
 */

public class KorbitTicker {
    public Ticker btc;
    public Ticker eth;
    public Ticker etc;
    public Ticker xrp;

    public class Ticker {
        @SerializedName("last")
        public long last;
    }
}
