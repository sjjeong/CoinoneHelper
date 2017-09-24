package com.googry.coinonehelper.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by seokjunjeong on 2017. 9. 24..
 */

public class UnitAlarm extends RealmObject {
    @PrimaryKey
    public String coinType;
    public long divider;
    public long prevPrice;
    public boolean runFlag;

    public UnitAlarm() {
    }
}
