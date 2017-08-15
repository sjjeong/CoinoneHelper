package com.googry.coinonehelper.data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinNotification extends RealmObject implements Parcelable {
    public static final Creator<CoinNotification> CREATOR = new Creator<CoinNotification>() {
        @Override
        public CoinNotification createFromParcel(Parcel in) {
            return new CoinNotification(in);
        }

        @Override
        public CoinNotification[] newArray(int size) {
            return new CoinNotification[size];
        }
    };

    @PrimaryKey
    private long createdTs;
    private long updatedTs;
    private int priceDirection;
    private long targetPrice;
    private int coinType;

    public CoinNotification() {
    }

    protected CoinNotification(Parcel in) {
        createdTs = in.readLong();
        updatedTs = in.readLong();
        priceDirection = in.readInt();
        targetPrice = in.readLong();
        coinType = in.readInt();
    }

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

    public long getUpdatedTs() {
        return updatedTs;
    }

    public void setUpdatedTs(long updatedTs) {
        this.updatedTs = updatedTs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(createdTs);
        dest.writeLong(updatedTs);
        dest.writeInt(priceDirection);
        dest.writeLong(targetPrice);
        dest.writeInt(coinType);
    }

    public enum PriceDirection {
        Up, Down
    }
}
