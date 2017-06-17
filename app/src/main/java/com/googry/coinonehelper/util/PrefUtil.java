package com.googry.coinonehelper.util;

/**
 * Created by seokjunjeong on 2017. 6. 16..
 */

public class PrefUtil {
    private static final String PREF_TIME_STAMP = "pref_time_stamp";
    private static final String KEY_BTC_ASK = "key_btc_ask";
    private static final String KEY_ETH_ASK = "key_eth_ask";
    private static final String KEY_ETC_ASK = "key_etc_ask";
    private static final String KEY_XRP_ASK = "key_xrp_ask";
    private static final String KEY_BTC_BID = "key_btc_bid";
    private static final String KEY_ETH_BID = "key_eth_bid";
    private static final String KEY_ETC_BID = "key_etc_bid";
    private static final String KEY_XRP_BID = "key_xrp_bid";
    private static final String KEY_BTC_COMPLETE_ORDER = "key_btc_complete_order";
    private static final String KEY_ETH_COMPLETE_ORDER = "key_eth_complete_order";
    private static final String KEY_ETC_COMPLETE_ORDER = "key_etc_complete_order";
    private static final String KEY_XRP_COMPLETE_ORDER = "key_xrp_complete_order";
    private static final String KEY_BTC_TICKER = "key_btc_ticker";
    private static final String KEY_ETH_TICKER = "key_eth_ticker";
    private static final String KEY_ETC_TICKER = "key_etc_ticker";
    private static final String KEY_XRP_TICKER = "key_xrp_ticker";

//    public static void saveAsk()


//    public static boolean isFullScreen(Context context) {
//        return getSharedPrefs(context).getBoolean(PREF_FULLSCREEN, false);
//    }
//
//    public static boolean isDesignImageEnabled(Context context) {
//        return getSharedPrefs(context).getBoolean(PREF_DESIGN_IMAGE_ENABLED, true);
//    }
//
//    public static Uri getDesignImageUri(Context context) {
//        String uriString = getSharedPrefs(context).getString(PREF_DESIGN_IMAGE_URI, null);
//        if (uriString != null) {
//            return Uri.parse(uriString);
//        }
//        return null;
//    }
//
//    public static void setDesignImageUri(Context context, Uri uri) {
//        SharedPreferences.Editor editor = getEditor(context);
//        if (uri != null) {
//            editor.putString(PREF_DESIGN_IMAGE_URI, uri.toString());
//        } else {
//            editor.remove(PREF_DESIGN_IMAGE_URI);
//        }
//        apply(editor);
//    }
//
//    public static int getDesignImageAlpha(Context context) {
//        return getSharedPrefs(context).getInt(PREF_DESIGN_IMAGE_ALPHA, 100);
//    }
//
//    public static boolean isGridEnabled(Context context) {
//        return getSharedPrefs(context).getBoolean(PREF_GRID_ENABLED, true);
//    }
//
//    public static int getGridSize(Context context) {
//        return (int) DimenUtil.convertToPixelFromDip(context,
//                Float.parseFloat(getSharedPrefs(context).getString(PREF_GRID_SIZE, "4")));
//    }
//
//    public static boolean isAlignRight(Context context) {
//        return getSharedPrefs(context).getBoolean(PREF_ALIGN_RIGHT, false);
//    }
//
//    public static boolean isAlignBottom(Context context) {
//        return getSharedPrefs(context).getBoolean(PREF_ALIGN_BOTTOM, false);
//    }
//
//    public static int getGridColor(Context context) {
//        return getSharedPrefs(context).getInt(PREF_GRID_COLOR, 0x7732cd32);
//    }
//
//    public static void registerOnSharedPreferenceChangeListener(Context context,
//                                                                SharedPreferences.OnSharedPreferenceChangeListener listener) {
//        getSharedPrefs(context).registerOnSharedPreferenceChangeListener(listener);
//    }
//
//    public static void unregisterOnSharedPreferenceChangeListener(Context context,
//                                                                  SharedPreferences.OnSharedPreferenceChangeListener listener) {
//        getSharedPrefs(context).unregisterOnSharedPreferenceChangeListener(listener);
//    }
//
//    private static SharedPreferences getSharedPrefs(Context context) {
//        return PreferenceManager.getDefaultSharedPreferences(context);
//    }
//
//    private static SharedPreferences.Editor getEditor(Context context) {
//        return getSharedPrefs(context).edit();
//    }
//
//    // if you do not care about the result and calling from the main thread
//    private static void apply(SharedPreferences.Editor editor) {
//        editor.putLong(PREF_TIME_STAMP, getCurrentTime());
//        editor.apply();
//    }
//
//    private static void commit(SharedPreferences.Editor editor) {
//        editor.putLong(PREF_TIME_STAMP, getCurrentTime());
//        editor.commit();
//    }
//
//    private static long getCurrentTime() {
//        return System.currentTimeMillis();
//    }
}