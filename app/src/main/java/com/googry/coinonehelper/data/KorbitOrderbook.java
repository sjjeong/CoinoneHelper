package com.googry.coinonehelper.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class KorbitOrderbook {
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("currency")
    public String currency;
    @SerializedName("bids")
    @Expose
    public List<List<String>> bids = null;
    @SerializedName("asks")
    @Expose
    public List<List<String>> asks = null;


    public class Book {
        @SerializedName("price")
        public long price;
        @SerializedName("qty")
        public double qty;

    }
}
