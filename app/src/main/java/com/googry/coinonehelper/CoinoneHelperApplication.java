package com.googry.coinonehelper;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.firebase.FirebaseApp;
import com.securepreferences.SecurePreferences;

import java.io.FileNotFoundException;
import java.security.SecureRandom;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by seokjunjeong on 2017. 8. 6..
 */

public class CoinoneHelperApplication extends Application {
    /**
     * 초기 버전
     */
    private static final long REALM_SCHEMA_INIT = 0;
    /**
     * unit alarm의 스키마 추가로 버전 1
     */
    private static final long REALM_SCHEMA_UNIT_ALARM = 1;
    /**
     *
     */
    private static final long REALM_SCHEMA_MARKET_ACCOUNT = 2;
    private static CoinoneHelperApplication sInstance;
//    private SecurePreferences mSecurePrefs;

    public CoinoneHelperApplication() {
        super();
        sInstance = this;
    }

    public static synchronized CoinoneHelperApplication getInstance() {
        return sInstance;
    }

//    public SharedPreferences getSecurePreferences() {
//        if (mSecurePrefs == null) {
//            mSecurePrefs = new SecurePreferences(this, "C@in@n2H2lp2r", "prefs.xml");
//        }
//
//        return mSecurePrefs;
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        byte[] key = new byte[64];
        new SecureRandom().nextBytes(key);

        Realm.init(this);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
//                .encryptionKey(key)
                .schemaVersion(REALM_SCHEMA_MARKET_ACCOUNT)
                .migration(new CoinRealmMigration())
                .build();
        Realm.deleteRealm(realmConfiguration);
        try {
            Realm.migrateRealm(realmConfiguration);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Realm.setDefaultConfiguration(realmConfiguration);

        FirebaseApp.initializeApp(this);
    }


    public class CoinRealmMigration implements RealmMigration {

        @Override
        public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
            RealmSchema schema = realm.getSchema();

            /**
             * {@link com.googry.coinonehelper.data.UnitAlarm} 추가
             * @PrimaryKey
             * public String coinType;
             * public long divider;
             * public long prevPrice;
             * public boolean runFlag;
             */
            if (oldVersion == REALM_SCHEMA_INIT) {
                schema.create("UnitAlarm")
                        .addField("coinType", String.class, FieldAttribute.PRIMARY_KEY)
                        .addField("divider", long.class)
                        .addField("prevPrice", long.class)
                        .addField("runFlag", boolean.class);


                oldVersion++;
            }

            if (oldVersion == REALM_SCHEMA_UNIT_ALARM) {
                schema.create("MarketAccount")
                        .addField("id", String.class, FieldAttribute.PRIMARY_KEY)
                        .addField("accessToken", String.class)
                        .addField("secretKey", String.class);

                oldVersion++;
            }
        }
    }
}
