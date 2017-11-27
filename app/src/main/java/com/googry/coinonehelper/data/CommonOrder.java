package com.googry.coinonehelper.data;

/**
 * Created by seokjunjeong on 2017. 11. 27..
 */

public class CommonOrder {
    public int index;
    public long price;
    public double qty;
    public boolean isAsk;

    public CommonOrder(int index, long price, double qty, boolean isAsk) {
        this.index = index;
        this.price = price;
        this.qty = qty;
        this.isAsk = isAsk;
    }
}
