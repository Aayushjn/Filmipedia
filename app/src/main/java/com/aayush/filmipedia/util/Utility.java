package com.aayush.filmipedia.util;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Utility {
    private Utility() {}

    private static boolean isAppInstalled(Context context) {
        PackageManager packageManager = context.getPackageManager();
        boolean installed;

        try {
            packageManager.getPackageInfo("com.google.android.youtube",
                    PackageManager.GET_ACTIVITIES);
            installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }

        return installed;
    }

    public static boolean isNetworkNotAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager != null ?
                connectivityManager.getActiveNetworkInfo() : null;
        return activeNetwork == null || !activeNetwork.isConnected();
    }

    @SuppressLint("PrivateApi")
    private static String getCdmaCountryIso() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);

            String homeOperator = ((String) get.invoke(systemProperties,
                    "ro.cdma.home.operator.numeric"));

            int mcc = Integer.parseInt(homeOperator.substring(0, 3));

            // Mapping countries that actually use CDMA
            switch (mcc) {
                case 330:
                    return "pr";
                case 310:
                case 311:
                case 312:
                case 316:
                    return "us";
                case 283:
                    return "am";
                case 460:
                    return "cn";
                case 455:
                    return "mo";
                case 414:
                    return "mm";
                case 619:
                    return "sl";
                case 450:
                    return "kr";
                case 634:
                    return "sd";
                case 434:
                    return "uz";
                case 232:
                    return "at";
                case 204:
                    return "nl";
                case 262:
                    return "de";
                case 247:
                    return "lv";
                case 255:
                    return "ua";
            }
        } catch (ClassNotFoundException
                | NoSuchMethodException
                | IllegalAccessException
                | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDeviceCountryCode(Context context) {
        String countryCode;

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            countryCode = telephonyManager.getSimCountryIso();
            if (countryCode != null && countryCode.length() == 2) {
                return countryCode.toLowerCase();
            }

            if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                countryCode = getCdmaCountryIso();
            }
            else {
                countryCode = telephonyManager.getNetworkCountryIso();
            }

            if (countryCode != null && countryCode.length() == 2) {
                return countryCode.toLowerCase();
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
        }
        else {
            countryCode = context.getResources().getConfiguration().locale.getCountry();
        }

        if (countryCode != null && countryCode.length() == 2) {
            return countryCode.toLowerCase();
        }

        return "us";
    }

    public static void watchYoutubeVideo(String id, Context context) {
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(isAppInstalled(context)){
                intent.setClassName("com.google.android.youtube",
                        "com.google.android.youtube.WatchActivity");
            }
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e){
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + id))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
