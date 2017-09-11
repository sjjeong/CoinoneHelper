package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class BithumbOrderbook {

    @SerializedName("data")
    public Data data;

    public class Data {
        @SerializedName("asks")
        public ArrayList<Book> asks;
        @SerializedName("bids")
        public ArrayList<Book> bids;
    }

    public class Book {
        @SerializedName("price")
        public long price;
        @SerializedName("quantity")
        public double qty;

    }
}
