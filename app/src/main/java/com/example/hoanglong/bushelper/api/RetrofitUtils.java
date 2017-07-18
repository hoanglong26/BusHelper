package com.example.hoanglong.bushelper.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hoanglong on 10/08/2016.
 */

public class RetrofitUtils {
    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20,TimeUnit.SECONDS).build();
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(Constant.WEB_URL).client(client).addConverterFactory(GsonConverterFactory.create());

    public static Retrofit get() {
        return builder.build();
    }


}
