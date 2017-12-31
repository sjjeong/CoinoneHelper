package com.googry.coinonehelper.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by seokjunjeong on 2017. 12. 23..
 */

public class MarketAccount extends RealmObject{

    @PrimaryKey
    public String id;
    public String accessToken;
    public String secretKey;

    public MarketAccount() {
    }

    public MarketAccount(String id, String accessToken, String secretKey) {
        this.id = id;
        this.accessToken = accessToken;
        this.secretKey = secretKey;
    }
}
