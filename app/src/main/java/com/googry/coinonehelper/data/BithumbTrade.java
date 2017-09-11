package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 6. 14..
 */

public class BithumbTrade {
    @SerializedName("data")
    public ArrayList<CompleteOrder> completeOrders;

    public class CompleteOrder {
        @SerializedName("type")
        public String type;
        @SerializedName("price")
        public long price;
        @SerializedName("units_traded")
        public double units_traded;
    }
}
