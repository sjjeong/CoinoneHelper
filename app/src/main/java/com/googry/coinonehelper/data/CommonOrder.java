package com.googry.coinonehelper.data;

import java.util.Comparator;

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

    public static class AscendingPrice implements Comparator<CommonOrder> {

        @Override
        public int compare(CommonOrder o1, CommonOrder o2) {
            if (o1.price < o2.price) {
                return -1;
            } else if (o1.price > o2.price) {
                return 1;
            }
            return 0;
        }
    }

    public static class DescendingPrice implements Comparator<CommonOrder> {

        @Override
        public int compare(CommonOrder o1, CommonOrder o2) {
            if (o1.price < o2.price) {
                return 1;
            } else if (o1.price > o2.price) {
                return -1;
            }
            return 0;
        }
    }
}
