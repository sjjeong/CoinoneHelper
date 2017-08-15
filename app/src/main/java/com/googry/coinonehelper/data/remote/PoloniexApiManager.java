package com.googry.coinonehelper.data.remote;

import com.googry.coinonehelper.data.PoloniexTicker;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class PoloniexApiManager {
    private static final String BASE_URL = "https://poloniex.com/";
    private static Retrofit mInstance;

    public static Retrofit getApiManager() {
        if (mInstance == null) {
            OkHttpClient client = new OkHttpClient.Builder()
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

    public interface PoloniexPublicApi {
        @GET("public?command=returnTicker")
        Call<PoloniexTicker> allTicker();
    }

}
