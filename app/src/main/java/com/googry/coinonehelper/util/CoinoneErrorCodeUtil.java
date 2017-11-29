package com.googry.coinonehelper.util;

import android.widget.Toast;

import com.google.gson.Gson;
import com.googry.coinonehelper.data.CoinonePrivateError;

import java.io.IOException;

/**
 * Created by seokjunjeong on 2017. 11. 30..
 */

public final class CoinoneErrorCodeUtil {

    public static String getErrorMsgWithErrorCode(int errorCode) {
        switch (errorCode) {
            case 4:
                return "Blocked user access";
            case 11:
                return "Access token is missing";
            case 12:
                return "Invalid access token";
            case 40:
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

    public static String replaceBadQuotes(String badQuotes){
        return badQuotes.replaceAll("[\u201C\u201D\u201E\u201F\u2033\u2036]", "\"");
    }
}
