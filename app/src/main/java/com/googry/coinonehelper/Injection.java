package com.googry.coinonehelper;

import java.security.SecureRandom;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by seokjunjeong on 2017. 12. 23..
 */

public class Injection {

    public static Realm getSecureRealm() {
//        byte[] key = new byte[64];
//        new SecureRandom().nextBytes(key);
//
//        RealmConfiguration config = new RealmConfiguration.Builder()
//                .encryptionKey(key)
//                .build();
//        Realm.deleteRealm(config);
//        return Realm.getInstance(config);
        return Realm.getDefaultInstance();
    }
}
