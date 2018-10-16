package com.aayush.filmipedia.util.api;

import android.content.Context;

import com.aayush.filmipedia.util.Utility;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiFactory {
    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static RestApi create(Context context) {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), CACHE_SIZE))
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    if (Utility.isNetworkNotAvailable(context)) {
                        int maxStale = 60 * 60 * 24 * 28;
                        request = request.newBuilder()
                                .header("Cache-Control",
                                        "public, only-if-cached, max-stale=" + maxStale)
                                .build();
                    }
                    return chain.proceed(request);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .build();

        return retrofit.create(RestApi.class);
    }
}
