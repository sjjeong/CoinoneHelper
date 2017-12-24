package com.googry.coinonehelper.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.googry.coinonehelper.CoinoneHelperApplication;
import com.googry.coinonehelper.data.CoinType;

/**
 * Created by seokjunjeong on 2017. 6. 16..
 */

public class PrefUtil {
    private static final String EMPTY = "";
    private static final String PREF_TIME_STAMP = "pref_time_stamp";
    private static final String KEY_PREFIX = "key_";
    private static final String KEY_SUFFIX_TICKER = "_ticker";
//    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
//    private static final String KEY_SECRET_KEY = "KEY_SECRET_KEY";
//    private static final String KEY_USER_INFO = "KEY_USER_INFO";
//    private static final String KEY_REGISTER_ACCOUNT = "KEY_REGISTER_ACCOUNT";

    public static void saveTicker(Context context, CoinType coinType, String value) {
        SharedPreferences.Editor editor = getEditor(context);
        editor.putString(getKey(coinType, KEY_SUFFIX_TICKER), value);
        commit(editor);
    }


    public static String loadTicker(Context context, CoinType coinType) {
        return getSharedPrefs(context).getString(getKey(coinType, KEY_SUFFIX_TICKER), EMPTY);
    }

//    public static void saveRegisterAccount(Context context, boolean isRegister) {
//        SharedPreferences.Editor editor = getEditor(context);
//        editor.putBoolean(KEY_REGISTER_ACCOUNT, isRegister);
//        apply(editor);
//    }
//
//    public static boolean loadRegisterAccount(Context context) {
//        return getSharedPrefs(context).getBoolean(KEY_REGISTER_ACCOUNT, false);
//    }

    private static SharedPreferences getSharedPrefs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPrefs(context).edit();
    }

//    public static void saveAccessToken(String accessToken) {
//        SharedPreferences.Editor editor = getSecureEditor();
//        editor.putString(KEY_ACCESS_TOKEN, accessToken);
//        apply(editor);
//    }
//
//    public static String loadAccessToken() {
//        return getSecureSharedPrefs().getString(KEY_ACCESS_TOKEN, null);
//    }
//
//    public static void saveSecretKey(String secretKey) {
//        SharedPreferences.Editor editor = getSecureEditor();
//        editor.putString(KEY_SECRET_KEY, secretKey);
//        apply(editor);
//    }
//
//    public static String loadSecretKey() {
//        return getSecureSharedPrefs().getString(KEY_SECRET_KEY, null);
//    }
//
//    public static void saveUserInfo(String userInfo) {
//        SharedPreferences.Editor editor = getSecureEditor();
//        editor.putString(KEY_USER_INFO, userInfo);
//        apply(editor);
//    }
//
//    public static String loadUserInfo() {
//        return getSecureSharedPrefs().getString(KEY_USER_INFO, null);
//    }

//    private static SharedPreferences getSecureSharedPrefs() {
//        return CoinoneHelperApplication.getInstance().getSecurePreferences();
//    }

//    private static SharedPreferences.Editor getSecureEditor() {
//        return getSecureSharedPrefs().edit();
//    }

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