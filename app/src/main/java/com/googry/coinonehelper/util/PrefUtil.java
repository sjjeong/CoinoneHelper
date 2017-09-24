package com.googry.coinonehelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.googry.coinonehelper.data.CoinType;

/**
 * Created by seokjunjeong on 2017. 6. 16..
 */

public class PrefUtil {
    private static final String EMPTY = "";
    private static final String PREF_TIME_STAMP = "pref_time_stamp";
    private static final String KEY_PREFIX = "key_";
    private static final String KEY_SUFFIX_ORDERBOOK = "_orderbook";
    private static final String KEY_SUFFIX_COMPLETE_ORDER = "_complete_order";
    private static final String KEY_SUFFIX_TICKER = "_ticker";
    private static final String KEY_SUFFIX_UNIT_ALARM = "_unit_alarm";
    private static final String KEY_SUFFIX_UNIT_ALARM_PRICE = "_unit_alarm_price";
    private static final String KEY_SUFFIX_UNIT_ALARM_FLAG = "_unit_alarm_flag";

    public static void saveOrderbook(Context context, CoinType coinType, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(getKey(coinType, KEY_SUFFIX_ORDERBOOK), value);
        commit(editor);
    }


    public static String loadOrderbook(Context context, CoinType coinType) {
        return getSharedPrefs(context).getString(getKey(coinType, KEY_SUFFIX_ORDERBOOK), EMPTY);
    }

    public static void saveCompleteOrder(Context context, CoinType coinType, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(getKey(coinType, KEY_SUFFIX_COMPLETE_ORDER), value);
        commit(editor);
    }


    public static String loadCompleteOrder(Context context, CoinType coinType) {
        return getSharedPrefs(context).getString(getKey(coinType, KEY_SUFFIX_COMPLETE_ORDER), EMPTY);
    }

    public static void saveTicker(Context context, CoinType coinType, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(getKey(coinType, KEY_SUFFIX_TICKER), value);
        commit(editor);
    }


    public static String loadTicker(Context context, CoinType coinType) {
        return getSharedPrefs(context).getString(getKey(coinType, KEY_SUFFIX_TICKER), EMPTY);
    }

    public static void saveCoinUnitAlarm(Context context, CoinType coinType, long divideUnit){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(getKey(coinType, KEY_SUFFIX_UNIT_ALARM), divideUnit);
        editor.commit();
    }

    public static long loadCoinUnitAlarm(Context context, CoinType coinType) {
        return getSharedPrefs(context).getLong(getKey(coinType, KEY_SUFFIX_UNIT_ALARM), CoinType.getCoinDivider(coinType));
    }

    public static void saveCoinUnitAlarmPrice(Context context, CoinType coinType, long price){
        long divider = loadCoinUnitAlarm(context, coinType);
        long standardPrice = price - (price % divider);
        SharedPreferences.Editor editor = getEditor(context);
        editor.putLong(getKey(coinType, KEY_SUFFIX_UNIT_ALARM_PRICE), standardPrice);
        editor.commit();
    }

    public static long loadCoinUnitAlarmPrice(Context context, CoinType coinType) {
        return getSharedPrefs(context).getLong(getKey(coinType, KEY_SUFFIX_UNIT_ALARM_PRICE), 0);
    }

    public static void saveCoinUnitAlarmFlag(Context context, CoinType coinType, boolean flag){
        SharedPreferences.Editor editor = getEditor(context);
        editor.putBoolean(getKey(coinType, KEY_SUFFIX_UNIT_ALARM_FLAG), flag);
        editor.commit();
    }

    public static boolean loadCoinUnitAlarmFlag(Context context, CoinType coinType) {
        return getSharedPrefs(context).getBoolean(getKey(coinType, KEY_SUFFIX_UNIT_ALARM_FLAG), false);
    }

    private static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPrefs(context).edit();
    }

    // if you do not care about the result and calling from the main thread
    private static void apply(SharedPreferences.Editor editor) {
        editor.putLong(PREF_TIME_STAMP, getCurrentTime());
        editor.apply();
    }

    private static void commit(SharedPreferences.Editor editor) {
        editor.putLong(PREF_TIME_STAMP, getCurrentTime());
        editor.commit();
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    private static String getKey(CoinType coinType, String suffix) {
        return KEY_PREFIX + coinType.name() + suffix;

    }
}