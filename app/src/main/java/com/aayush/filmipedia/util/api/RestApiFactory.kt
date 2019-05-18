package com.aayush.filmipedia.util.api

import android.content.Context
import com.aayush.filmipedia.util.BASE_URL
import com.aayush.filmipedia.util.CACHE_SIZE
import com.aayush.filmipedia.util.isNetworkNotAvailable
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RestApiFactory {
    fun create(context: Context): RestApi {
        val gson = GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()

        val httpClient = OkHttpClient.Builder()
            .cache(Cache(context.cacheDir, CACHE_SIZE.toLong()))
            .addInterceptor { chain ->
                var request = chain.request()
                if (isNetworkNotAvailable(context)) {
                    val maxStale = 60 * 60 * 24 * 28
                    request = request.newBuilder()
                        .header(
                            "Cache-Control",
                            "public, only-if-cached, max-stale=$maxStale"
                        )
                        .build()
                }
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build()

        return retrofit.create(RestApi::class.java)
    }
}