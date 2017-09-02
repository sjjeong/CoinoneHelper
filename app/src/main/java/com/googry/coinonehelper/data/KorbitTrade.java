package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 6. 14..
 */

public class KorbitTrade {
    @SerializedName("price")
    public long price;
    @SerializedName("amount")
    public double amount;
}
