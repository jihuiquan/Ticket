package com.sklk.ticket.network;


import com.sklk.ticket.config.AppConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author：${JHuiQ} on 2018/4/9 13:14
 */
public class RetroftServiceManager {


    public static <T> T getService(Class<T> cls) {
        Retrofit build = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_IP+"/api/v1.0/")
                .client(OkHttpClientManger.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return build.create(cls);
    }
}
