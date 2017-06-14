package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 6. 14..
 */

public class CoinoneTrades {
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("currency")
    public String currency;
    @SerializedName("completeOrders")
    public ArrayList<CompleteOrder> completeOrders;

    public class CompleteOrder{
        @SerializedName("timestamp")
        public long timestamp;
        @SerializedName("price")
        public long price;
        @SerializedName("qty")
        public double qty;
    }
}
