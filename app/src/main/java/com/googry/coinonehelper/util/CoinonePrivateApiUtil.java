package com.googry.coinonehelper.util;

import android.content.Context;
import android.util.Base64;

import com.googry.coinonehelper.data.CoinoneBalance;
import com.googry.coinonehelper.data.CoinoneCompleteOrder;
import com.googry.coinonehelper.data.CoinoneLimitOrder;
import com.googry.coinonehelper.data.CoinonePrivateError;
import com.googry.coinonehelper.data.CoinoneUserInfo;
import com.googry.coinonehelper.data.CommonOrder;
import com.googry.coinonehelper.data.remote.CoinoneApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;


/**
 * Created by seokjunjeong on 2017. 8. 28..
 */

public class CoinonePrivateApiUtil {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String getJsonAccount(String accessToken, long nonce) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("access_token", accessToken);
            jsonObject.put("nonce", nonce);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static String getJsonLimitOrders(String accessToken,
                                             String currency,
                                             long nonce) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("access_token", accessToken);
            jsonObject.put("currency", currency);
            jsonObject.put("nonce", nonce);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getJsonCancelOrder(String accessToken,
                                            String orderId,
                                            long price,
                                            double qty,
                                            int isAsk,
                                            String currency,
                                            long nonce) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("access_token", accessToken);
            jsonObject.put("order_id", orderId);
            jsonObject.put("price", price);
            jsonObject.put("qty", qty);
            jsonObject.put("is_ask", isAsk);
            jsonObject.put("currency", currency);
            jsonObject.put("nonce", nonce);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    private static String getEncyptPayload(String payload) {

        String base64Payload = Base64.encodeToString(payload.getBytes(), 0);
        base64Payload = base64Payload.replace("\n", "");
        return base64Payload;
    }

    private static String getSignature(String secretKey, String encrypPayload) {
        try {
            secretKey = secretKey.toUpperCase();
            byte[] byteKey = secretKey.getBytes("UTF-8");
            final String HMAC_SHA256 = "HmacSHA512";
            Mac sha512_HMAC = Mac.getInstance(HMAC_SHA256);
            sha512_HMAC.init(new SecretKeySpec(byteKey, HMAC_SHA256));
            return bytesToHex(sha512_HMAC.doFinal(encrypPayload.getBytes("UTF-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    private static String[] getEncryptPayloadAndSignature(Context context) {
        String accessToken = PrefUtil.loadAccessToken();
        String secretKey = PrefUtil.loadSecretKey();

        String limitOrdersPayload = getJsonAccount(
                accessToken, System.currentTimeMillis());
        String encryptlimitOrdersPayload = getEncyptPayload(limitOrdersPayload);
        String limitOrdersSignature = getSignature(secretKey, encryptlimitOrdersPayload);
        return new String[]{encryptlimitOrdersPayload, limitOrdersSignature};
    }

    public static Call<CoinoneBalance> getBalance(Context context) {
        String[] encryptPayloadAndStignature = getEncryptPayloadAndSignature(context);

        CoinoneApiManager.CoinonePrivateApi coinonePrivateApi =
                CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePrivateApi.class);

        return coinonePrivateApi.balance(
                encryptPayloadAndStignature[0],
                encryptPayloadAndStignature[1],
                encryptPayloadAndStignature[0]);
    }

    public static Call<CoinoneUserInfo> getUserInfo(Context context) {
        String[] encryptPayloadAndStignature = getEncryptPayloadAndSignature(context);

        CoinoneApiManager.CoinonePrivateApi coinonePrivateApi =
                CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePrivateApi.class);

        return coinonePrivateApi.userInfo(
                encryptPayloadAndStignature[0],
                encryptPayloadAndStignature[1],
                encryptPayloadAndStignature[0]);
    }

    public static Call<CoinoneLimitOrder> getLimitOrder(Context context, String coinName) {
        String accessToken = PrefUtil.loadAccessToken();
        String secretKey = PrefUtil.loadSecretKey();

        String limitOrdersPayload = getJsonLimitOrders(
                accessToken, coinName, System.currentTimeMillis());
        String encryptlimitOrdersPayload = getEncyptPayload(limitOrdersPayload);
        String limitOrdersSignature = getSignature(secretKey, encryptlimitOrdersPayload);

        CoinoneApiManager.CoinonePrivateApi coinonePrivateApi =
                CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePrivateApi.class);

        return coinonePrivateApi.limitOrders(
                encryptlimitOrdersPayload,
                limitOrdersSignature,
                encryptlimitOrdersPayload);
    }

    public static Call<CoinoneCompleteOrder> getCompleteOrder(Context context, String coinName) {
        String accessToken = PrefUtil.loadAccessToken();
        String secretKey = PrefUtil.loadSecretKey();

        String limitOrdersPayload = getJsonLimitOrders(
                accessToken, coinName, System.currentTimeMillis());
        String encryptlimitOrdersPayload = getEncyptPayload(limitOrdersPayload);
        String limitOrdersSignature = getSignature(secretKey, encryptlimitOrdersPayload);

        CoinoneApiManager.CoinonePrivateApi coinonePrivateApi =
                CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePrivateApi.class);

        return coinonePrivateApi.completeOrders(
                encryptlimitOrdersPayload,
                limitOrdersSignature,
                encryptlimitOrdersPayload);
    }

    public static Call<CoinonePrivateError> getCancelOrder(Context context, CommonOrder order, String coinName) {
        String accessToken = PrefUtil.loadAccessToken();
        String secretKey = PrefUtil.loadSecretKey();

        String limitOrdersPayload = getJsonCancelOrder(
                accessToken,
                order.orderId,
                order.price,
                order.qty,
                order.isAsk ? 1 : 0,
                coinName,
                System.currentTimeMillis());
        String encryptlimitOrdersPayload = getEncyptPayload(limitOrdersPayload);
        String limitOrdersSignature = getSignature(secretKey, encryptlimitOrdersPayload);

        CoinoneApiManager.CoinonePrivateApi coinonePrivateApi =
                CoinoneApiManager.getApiManager().create(CoinoneApiManager.CoinonePrivateApi.class);

        return coinonePrivateApi.cancelOrder(
                encryptlimitOrdersPayload,
                limitOrdersSignature,
                encryptlimitOrdersPayload);
    }
}
