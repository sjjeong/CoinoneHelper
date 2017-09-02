package com.googry.coinonehelper.data.remote;

import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.data.KorbitOrderbook;
import com.googry.coinonehelper.data.KorbitTicker;
import com.googry.coinonehelper.data.KorbitTrade;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by seokjunjeong on 2017. 7. 13..
 */

public class KorbitApiManager {
    private static final String BASE_URL = "https://api.korbit.co.kr/v1/";
    private static Retrofit mInstance;

    public static Retrofit getApiManager() {
        if (mInstance == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ?
                    HttpLoggingInterceptor.Level.BODY
                    : HttpLoggingInterceptor.Level.NONE);

            OkHttpClient client = new OkHttpClient.Builder()
//                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // 코빗은 User-Agent를 달아주지 않으면 403에러를 응답함.
                            Request request = original.newBuilder()
                                    .header("User-Agent", "CoinoneHelper")
                                    .method(original.method(), original.body())
                                    .build();

                            return chain.proceed(request);
                        }
                    })
                    .build();
            mInstance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mInstance;
    }

    public interface KorbitPublicApi {
        @GET("orderbook")
        Call<KorbitOrderbook> orderbook(
                @Query("currency_pair") String currency
        );

        @GET("transactions")
        Call<List<KorbitTrade>> trades(
                @Query("currency_pair") String currency,
                @Query("time") String period
        );

        @GET("ticker/detailed")
        Call<KorbitTicker.TickerDetailed> ticker(
                @Query("currency_pair") String currency
        );

        @GET("ticker?currency_pair=btc_krw")
        Call<KorbitTicker.Ticker> btcTicker();

        @GET("ticker?currency_pair=bch_krw")
        Call<KorbitTicker.Ticker> bchTicker();

        @GET("ticker?currency_pair=eth_krw")
        Call<KorbitTicker.Ticker> ethTicker();

        @GET("ticker?currency_pair=etc_krw")
        Call<KorbitTicker.Ticker> etcTicker();

        @GET("ticker?currency_pair=xrp_krw")
        Call<KorbitTicker.Ticker> xrpTicker();
    }
}
