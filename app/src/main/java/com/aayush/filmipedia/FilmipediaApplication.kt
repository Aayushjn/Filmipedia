package com.aayush.filmipedia

import android.app.Application
import android.content.Context
import com.aayush.filmipedia.util.api.RestApi
import com.aayush.filmipedia.util.api.RestApiFactory
import com.aayush.filmipedia.util.getDeviceCountryCode
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump


class FilmipediaApplication: Application() {
    lateinit var countryCode: String
    private var restApi: RestApi? = null

    override fun onCreate() {
        super.onCreate()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        countryCode = getDeviceCountryCode(this)
    }

    fun getRestApi(): RestApi {
        return restApi ?: synchronized(this) {
            RestApiFactory.create(this).also { restApi = it }
        }
    }

    companion object {
        fun getApplication(context: Context) = context.applicationContext as FilmipediaApplication
    }
}