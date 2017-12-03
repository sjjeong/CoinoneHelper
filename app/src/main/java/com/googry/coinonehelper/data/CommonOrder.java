package com.googry.coinonehelper.data;

/**
 * Created by seokjunjeong on 2017. 11. 27..
 */

public class CommonOrder {
    public int index;
    public long timestamp;
    public long price;
    public double qty;
    public String orderId;
    public boolean isAsk;

    public CommonOrder(int index, long timestamp, long price, double qty, String orderId, boolean isAsk) {
        this.index = index;
        this.timestamp = timestamp;
        this.price = price;
        this.qty = qty;
        this.orderId = orderId;
        this.isAsk = isAsk;
    }
}
