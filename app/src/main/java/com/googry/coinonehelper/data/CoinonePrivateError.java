package com.googry.coinonehelper.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by seokjunjeong on 2017. 11. 30..
 */

public class CoinonePrivateError {
    @SerializedName("result")
    public String result;
    @SerializedName("errorCode")
    public int errorCode;
    @SerializedName("errorMsg")
    public String errorMsg;
}
