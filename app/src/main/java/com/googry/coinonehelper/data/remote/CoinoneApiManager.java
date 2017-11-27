package com.googry.coinonehelper.data.remote;

import com.googry.coinonehelper.data.CoinoneBalance;
import com.googry.coinonehelper.data.CoinoneCompleteOrder;
import com.googry.coinonehelper.data.CoinoneLimitOrder;
import com.googry.coinonehelper.data.CoinoneOrderbook;
import com.googry.coinonehelper.data.CoinoneTicker;
import com.googry.coinonehelper.data.CoinoneTrade;
import com.googry.coinonehelper.data.CoinoneUserInfo;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class CoinoneApiManager {
    private static final String BASE_URL = "https://api.coinone.co.kr/";
    private static Retrofit mInstance;

    public static Retrofit getApiManager() {
        if (mInstance == null) {
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }

    public interface CoinonePublicApi {
        @GET("orderbook/")
        Call<CoinoneOrderbook> orderbook(
                @Query("currency") String currency
        );

        @GET("trades/")
        Call<CoinoneTrade> trades(
                @Query("currency") String currency,
                @Query("period") String period
        );

        @GET("ticker/?currency=all")
        Call<CoinoneTicker> allTicker();

        @GET("ticker")
        Call<CoinoneTicker.Ticker> ticker(
                @Query("currency") String currency
        );

    }

    public interface CoinonePrivateApi {
        @POST("v2/account/balance/")
        Call<CoinoneBalance> balance(
                @Header("X-COINONE-PAYLOAD") String payload,
                @Header("X-COINONE-SIGNATURE") String signature,
                @Body String body);

        @POST("v2/account/user_info/")
        Call<CoinoneUserInfo> userInfo(
                @Header("X-COINONE-PAYLOAD") String payload,
                @Header("X-COINONE-SIGNATURE") String signature,
                @Body String body);

        @POST("v2/order/limit_orders/")
        Call<CoinoneLimitOrder> limitOrders(
                @Header("X-COINONE-PAYLOAD") String payload,
                @Header("X-COINONE-SIGNATURE") String signature,
                @Body String body);

        @POST("v2/order/complete_orders/")
        Call<CoinoneCompleteOrder> completeOrders(
                @Header("X-COINONE-PAYLOAD") String payload,
                @Header("X-COINONE-SIGNATURE") String signature,
                @Body String body);
    }

}
