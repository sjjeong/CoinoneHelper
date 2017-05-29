package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class CoinoneOrderbook {
    @SerializedName("timestamp")
    public long timestamp;
    @SerializedName("currency")
    public String currency;
    @SerializedName("ask")
    public ArrayList<Book> askes;
    @SerializedName("bid")
    public ArrayList<Book> bides;


    public class Book{
        @SerializedName("price")
        public long price;
        @SerializedName("qty")
        public double qty;

    }
}
