package com.googry.coinonehelper;

import io.realm.Realm;

/**
 * Created by seokjunjeong on 2017. 12. 23..
 */

public class Injection {

    public static Realm getSecureRealm() {
        return Realm.getDefaultInstance();
    }
}
