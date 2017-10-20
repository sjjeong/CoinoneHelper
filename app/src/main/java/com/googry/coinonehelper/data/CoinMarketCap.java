package com.googry.coinonehelper.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by seokjunjeong on 2017. 10. 14..
 */

public class CoinMarketCap implements Parcelable{
    public String name;
    public String marketCap;
    public String price;
    public String circulatingSupply;
    public String volume24;
    public String changePercent;
    public String priceGraph7hUrl;
    public String marketsUrl;

    public CoinMarketCap() {
    }

    protected CoinMarketCap(Parcel in) {
        name = in.readString();
        marketCap = in.readString();
        price = in.readString();
        circulatingSupply = in.readString();
        volume24 = in.readString();
        changePercent = in.readString();
        priceGraph7hUrl = in.readString();
        marketsUrl = in.readString();
    }

    public static final Creator<CoinMarketCap> CREATOR = new Creator<CoinMarketCap>() {
        @Override
        public CoinMarketCap createFromParcel(Parcel in) {
            return new CoinMarketCap(in);
        }

        @Override
        public CoinMarketCap[] newArray(int size) {
            return new CoinMarketCap[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(marketCap);
        dest.writeString(price);
        dest.writeString(circulatingSupply);
        dest.writeString(volume24);
        dest.writeString(changePercent);
        dest.writeString(priceGraph7hUrl);
        dest.writeString(marketsUrl);
    }
}
