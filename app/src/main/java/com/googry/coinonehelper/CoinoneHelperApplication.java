package com.googry.coinonehelper;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import java.io.FileNotFoundException;

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

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(REALM_SCHEMA_UNIT_ALARM)
                .migration(new CoinRealmMigration())
                .build();
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
            if (oldVersion == 0) {
                schema.create("UnitAlarm")
                        .addField("coinType", String.class, FieldAttribute.PRIMARY_KEY)
                        .addField("divider", long.class)
                        .addField("prevPrice", long.class)
                        .addField("runFlag", boolean.class);


                oldVersion++;
            }
        }
    }
}
