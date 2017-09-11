package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 7. 1..
 */

public class BithumbSoloTicker {
    @SerializedName("data")
    public Ticker ticker;

    public class Ticker {
        @SerializedName("closing_price")
        public long last;
        @SerializedName("opening_price")
        public long first;
        @SerializedName("min_price")
        public long low;
        @SerializedName("max_price")
        public long high;
        @SerializedName("units_traded")
        public double volume;

    }
}
