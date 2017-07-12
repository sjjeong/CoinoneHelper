package com.googry.coinonehelper.data.remote;

import com.googry.coinonehelper.data.BithumbTicker;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by seokjunjeong on 2017. 5. 27..
 */

public class BithumbApiManager {
    private static final String BASE_URL = "https://api.bithumb.com/";
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

    public interface BithumbPublicApi {
        @GET("public/ticker/all")
        Call<BithumbTicker> allTicker();
    }

}
