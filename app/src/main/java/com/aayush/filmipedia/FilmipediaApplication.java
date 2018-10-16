package com.aayush.filmipedia;

import android.app.Application;
import android.content.Context;

import com.aayush.filmipedia.R;
import com.aayush.filmipedia.util.Utility;
import com.aayush.filmipedia.util.api.RestApi;
import com.aayush.filmipedia.util.api.RestApiFactory;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FilmipediaApplication extends Application {
    private String countryCode;

    private RestApi restApi;

    @Override
    public void onCreate() {
        super.onCreate();

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            return;
//        }
//        LeakCanary.install(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        countryCode = Utility.getDeviceCountryCode(this);
    }

    private static FilmipediaApplication get(Context context) {
        return (FilmipediaApplication) context.getApplicationContext();
    }

    public static FilmipediaApplication create(Context context) {
        return FilmipediaApplication.get(context);
    }

    public RestApi getRestApi() {
        if (restApi == null) {
            restApi = RestApiFactory.create(getApplicationContext());
        }
        return restApi;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
