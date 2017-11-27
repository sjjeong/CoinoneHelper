package com.googry.coinonehelper.data;

/**
 * Created by seokjunjeong on 2017. 11. 23..
 */

public class CommonOrderbook {
    public long price;
    public double qty;
    public boolean isAsk;

    public CommonOrderbook(long price, double qty, boolean isAsk) {
        this.price = price;
        this.qty = qty;
        this.isAsk = isAsk;
    }
}
