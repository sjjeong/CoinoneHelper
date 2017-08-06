package com.googry.coinonehelper.data;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinNotification extends RealmObject {
    private long createdTs;
    private int priceDirection;
    private long targetPrice;
    private int coinType;

    public PriceDirection getPriceDirection() {
        return PriceDirection.values()[priceDirection];
    }

    public void setPriceDirection(PriceDirection priceDirection) {
        this.priceDirection = priceDirection.ordinal();
    }

    public long getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(long targetPrice) {
        this.targetPrice = targetPrice;
    }

    public CoinType getCoinType() {
        return CoinType.values()[coinType];
    }

    public void setCoinType(CoinType coinType) {
        this.coinType = coinType.ordinal();
    }

    public long getCreatedTs() {
        return createdTs;
    }

    public void setCreatedTs(long createdTs) {
        this.createdTs = createdTs;
    }

    public enum PriceDirection {
        Up, Down
    }
}
