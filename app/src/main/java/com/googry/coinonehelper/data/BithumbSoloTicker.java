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
        public long closingPrice;
        @SerializedName("opening_price")
        public long openingPrice;
        @SerializedName("min_price")
        public long minPrice;
        @SerializedName("max_price")
        public long maxPrice;
        @SerializedName("units_traded")
        public double unitsTraded;

    }
}
