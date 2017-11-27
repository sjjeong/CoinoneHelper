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
    public ArrayList<Book> asks;
    @SerializedName("bid")
    public ArrayList<Book> bids;


    public static class Book {
        @SerializedName("price")
        public long price;
        @SerializedName("qty")
        public double qty;

        public Book(long price, double qty) {
            this.price = price;
            this.qty = qty;
        }

        public Book() {
        }
    }
}
