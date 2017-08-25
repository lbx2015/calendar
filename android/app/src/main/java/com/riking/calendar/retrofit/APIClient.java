package com.riking.calendar.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.riking.calendar.util.CONST;
import com.riking.calendar.util.GsonStringConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zzw on 05/01/17.
 */

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit != null)
            return retrofit;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://www.baidu.com")
//                .baseUrl("https://reqres.in")
                .baseUrl(CONST.URL_BASE)
//                .baseUrl("http://172.16.64.96:8281/")
//                .baseUrl("http://172.16.64.85:8281/")
                .addConverterFactory(new GsonStringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .client(client)
                .build();
        return retrofit;
    }

}
