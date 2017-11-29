package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by seokjunjeong on 2017. 9. 4..
 */

public class CoinoneCompleteOrder {
    @SerializedName("completeOrders")
    public List<Order> completeOrders;

    public class Order {
        @SerializedName("index")
        public int index;
        @SerializedName("timestamp")
        public long timestamp;
        @SerializedName("price")
        public long price;
        @SerializedName("qty")
        public double qty;
        @SerializedName("orderId")
        public String orderId;
        @SerializedName("type")
        public String type;
        @SerializedName("feeRate")
        public float feeRate;
    }


}
