package com.sklk.ticket.network;


import com.sklk.ticket.Constant;
import com.sklk.ticket.base.MyApplication;
import com.sklk.ticket.utils.SPUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * author：${JHuiQ} on 2018/4/9 13:24
 */
public class OkHttpClientManger {

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(OkHttpClientManger.getHeaderInterceptor())
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(OkHttpClientManger.getCookiesInterceptor())
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        if (null != cookies && cookies.size() > 0) {
                            String urlStr = url.url().toString();
                            if (urlStr.contains("parent/common/login") || urlStr.contains("parent/account/wechat_token") || urlStr.contains("parent/account/wechat_bind_parent")) {
                                Cookie cookie = cookies.get(0);
                                SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_DOMAIN);
                                SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_EXPIRESAT);
                                SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_NAME);
                                SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_PATH);
                                SPUtil.remove(MyApplication.getApplication(), SPUtil.COOKIE_VALUE);
                                SPUtil.putString(MyApplication.getApplication(), SPUtil.COOKIE_DOMAIN, cookie.domain());
                                SPUtil.putLong(MyApplication.getApplication(), SPUtil.COOKIE_EXPIRESAT, cookie.expiresAt());
                                SPUtil.putString(MyApplication.getApplication(), SPUtil.COOKIE_NAME, cookie.name());
                                SPUtil.putString(MyApplication.getApplication(), SPUtil.COOKIE_PATH, cookie.path());
                                SPUtil.putString(MyApplication.getApplication(), SPUtil.COOKIE_VALUE, cookie.value());
                            }
                        }
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> list = new ArrayList<>();
                        String domain = SPUtil.getString(MyApplication.getApplication(), SPUtil.COOKIE_DOMAIN);
                        long expiresAt = SPUtil.getLong(MyApplication.getApplication(), SPUtil.COOKIE_EXPIRESAT, -1);
                        String name = SPUtil.getString(MyApplication.getApplication(), SPUtil.COOKIE_NAME);
                        String path = SPUtil.getString(MyApplication.getApplication(), SPUtil.COOKIE_PATH);
                        String value = SPUtil.getString(MyApplication.getApplication(), SPUtil.COOKIE_VALUE);
                        if (null != domain && expiresAt != -1 && null != name && null != path && null != value) {
                            Cookie.Builder builder = new Cookie.Builder();
                            builder.domain(domain);
                            builder.expiresAt(expiresAt);
                            builder.name(name);
                            builder.path(path);
                            builder.value(value);
                            Cookie build = builder.build();
                            list.add(build);
                        }
                        return list;
                    }
                })
                //失败重连
                .retryOnConnectionFailure(true)
                .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    private static Interceptor getHeaderInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
//                String accessToken = SPUtil.getString(HospitalApplication.getApplication(), SPUtil.STRING_ACCESSTOKEN);
//                if (!StringUtil.isEmptyWithString(accessToken)) {
//                    //添加Parameter
//                    HttpUrl httpUrl = request.url().newBuilder()
//                            .addQueryParameter("client_id", AppConfig.CLIENT_ID)
//                            .addQueryParameter("installation_id", SPUtil.getString(HospitalApplication.getApplication(), SPUtil.STRING_UUID))
//                            .addQueryParameter("widget_id", AppConfig.WIDGET_ID)
//                            .build();
//                    //添加header
//                    Request builder = request.newBuilder()
//                            .addHeader("AccessToken", accessToken)
//                            .url(httpUrl)
//                            .build();
//
//                    return chain.proceed(builder);
//                }

                return chain.proceed(request);
            }
        };
        return interceptor;
    }

    private static Interceptor getCookiesInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.headers("Set-Cookie").isEmpty()) {
                    HttpUrl url = originalResponse.request().url();
                    String urlStr = url.url().toString();
                    if (urlStr.contains("parent/common/login") || urlStr.contains("parent/account/wechat_token") || urlStr.contains("parent/account/wechat_bind_parent")) {
                        List<String> headers = originalResponse.headers("Set-Cookie");
                        String[] cookies = new String[headers.size()];
                        for (int i = 0; i < headers.size(); i++) {
                            cookies[i] = headers.get(i);
                        }
                        SPUtil.putString(MyApplication.getApplication(), SPUtil.STRING_COOKIES, Arrays.toString(cookies));
                    }
                }
                return originalResponse;
            }
        };
        return interceptor;
    }
//    app_token=043c85ce-5ef5-4ee9-8b9a-561174e8727b; Path=/
}
