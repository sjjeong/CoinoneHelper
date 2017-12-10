package com.googry.coinonehelper.util;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import com.googry.coinonehelper.R;

/**
 * Created by seokjunjeong on 2017. 11. 30..
 */

public final class CoinoneErrorCodeUtil {

    public static void handleErrorCode(Context context, int errorCode) {
        switch (errorCode) {
            case 4:
            case 40:
            case 103:
            case 113:
            case 131:
                handleCustomErrorMsg(context, errorCode);
                break;
            default:
                Toast.makeText(context, getErrorMsgWithErrorCode(errorCode), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private static void handleCustomErrorMsg(Context context, int errorCode) {
        AlertDialog alertDialog;
        switch (errorCode) {
            case 4:
                alertDialog = new AlertDialog.Builder(context)
                        .setMessage("서버 오류로 인해 해당 기능을 1~10분 동안 사용할 수 없습니다.")
                        .setPositiveButton("확인", null)
                        .setCancelable(false)
                        .create();
                alertDialog.show();
                break;
            case 40:
                alertDialog = new AlertDialog.Builder(context)
                        .setMessage("발급하신 개인용 API Key에는 거래소 주문을 위한 권한이 없습니다.")
                        .setPositiveButton("확인", null)
                        .setCancelable(false)
                        .create();
                alertDialog.show();
                break;
            case 103:
                // 돈이 부족해요
                Toast.makeText(context, R.string.lack_of_money, Toast.LENGTH_SHORT).show();
                break;
            case 113:
                // 최소수량
                Toast.makeText(context, R.string.lack_of_amount, Toast.LENGTH_SHORT).show();
                break;
            case 131:
                // 재요청
                Toast.makeText(context, R.string.server_error_retry, Toast.LENGTH_LONG).show();
                break;
        }
    }

    private static String getErrorMsgWithErrorCode(int errorCode) {
        switch (errorCode) {
            case 4:
                // 계정 임시 차단
                return "Blocked user access";
            case 11:
                return "Access token is missing";
            case 12:
                return "Invalid access token";
            case 40:
                // 사용 권한이 없음
                return "Invalid API permission";
            case 50:
                return "Authenticate error";
            case 51:
                return "Invalid API";
            case 52:
                return "Deprecated API";
            case 53:
                return "Two Factor Auth Fail";
            case 100:
                return "Session expired";
            case 101:
                return "Invalid format";
            case 102:
                return "ID is not exist";
            case 103:
                // 돈 부족
                return "Lack of Balance";
            case 104:
                return "Order id is not exist";
            case 105:
                return "Price is not correct";
            case 106:
                return "Locking error";
            case 107:
                return "Parameter error";
            case 111:
                return "Order id is not exist";
            case 112:
                return "Cancel failed";
            case 113:
                // 최소 수량 미달
                return "Quantity is too low(ETH, ETC > 0.01)";
            case 120:
                return "V2 API payload is missing";
            case 121:
                return "V2 API signature is missing";
            case 122:
                return "V2 API nonce is missing";
            case 123:
                return "V2 API signature is not correct";
            case 130:
                return "V2 API Nonce value must be a positive integer";
            case 131:
                // 재요청
                return "V2 API Nonce is must be bigger then last nonce";
            case 132:
                return "V2 API body is corrupted";
            case 141:
                return "Too many limit orders";
            case 150:
                return "It's V1 API. V2 Access token is not acceptable";
            case 151:
                return "It's V2 API. V1 Access token is not acceptable";
            case 200:
                return "Wallet Error";
            case 202:
                return "Limitation error";
            case 210:
                return "Limitation error";
            case 220:
                return "Limitation error";
            case 221:
                return "Limitation error";
            case 310:
                return "Mobile auth error";
            case 311:
                return "Need mobile auth";
            case 312:
                return "Name is not correct";
            case 330:
                return "Phone number error";
            case 404:
                return "Page not found error";
            case 405:
                return "Server error";
            case 444:
                return "Locking error";
            case 500:
                return "Email error";
            case 501:
                return "Email error";
            case 777:
                return "Mobile auth error";
            case 778:
                return "Phone number error";
            case 1202:
                return "App not found";
            case 1203:
                return "Already registered";
            case 1204:
                return "Invalid access";
            case 1205:
                return "API Key error";
            case 1206:
                return "User not found";
            case 1207:
                return "User not found";
            case 1208:
                return "User not found";
            case 1209:
                return "User not found";
            default:
                return "ERROR";
        }
    }

    public static String replaceBadQuotes(String badQuotes) {
        return badQuotes.replaceAll("[\u201C\u201D\u201E\u201F\u2033\u2036]", "\"");
    }
}
