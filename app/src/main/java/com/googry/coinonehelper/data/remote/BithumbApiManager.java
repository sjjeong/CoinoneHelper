package com.googry.coinonehelper.data.remote;

import com.googry.coinonehelper.BuildConfig;
import com.googry.coinonehelper.data.BithumbSoloTicker;
import com.googry.coinonehelper.data.BithumbTicker;
import com.googry.coinonehelper.data.BithumbOrderbook;
import com.googry.coinonehelper.data.BithumbTrade;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class BithumbApiManager {
    private static final String BASE_URL = "https://api.bithumb.com/";
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

    public interface BithumbPublicApi {
        @GET("public/orderbook/{currency}")
        Call<BithumbOrderbook> orderbook(
                @Path("currency") String currency
        );

        @GET("public/recent_transactions/{currency}")
        Call<BithumbTrade> trades(
                @Path("currency") String currency
        );

        @GET("public/ticker/{currency}")
        Call<BithumbSoloTicker> ticker(
                @Path("currency") String currency
        );

        @GET("public/ticker/all")
        Call<BithumbTicker> allTicker();
    }

}
