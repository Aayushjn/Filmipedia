package com.aayush.filmipedia.util

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.telephony.TelephonyManager

class Utility {
    companion object {
        init {
            System.loadLibrary("keys")
        }

        @JvmStatic external fun getNativeKey(): String
    }
}
// Mapping countries that actually use CDMA
private val cdmaCountryIso: String?
    @SuppressLint("PrivateApi")
    get() {
        try {
            val systemProperties = Class.forName("android.os.SystemProperties")
            val get = systemProperties.getMethod("get", String::class.java)

            val homeOperator = get.invoke(systemProperties,
                    "ro.cdma.home.operator.numeric") as String

            when (Integer.parseInt(homeOperator.substring(0, 3))) {
                330 -> return "pr"
                310, 311, 312, 316 -> return "us"
                283 -> return "am"
                460 -> return "cn"
                455 -> return "mo"
                414 -> return "mm"
                619 -> return "sl"
                450 -> return "kr"
                634 -> return "sd"
                434 -> return "uz"
                232 -> return "at"
                204 -> return "nl"
                262 -> return "de"
                247 -> return "lv"
                255 -> return "ua"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

private fun isAppInstalled(context: Context): Boolean {
    val packageManager = context.packageManager

    return try {
        packageManager.getPackageInfo("com.google.android.youtube", PackageManager.GET_ACTIVITIES)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

fun isNetworkNotAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork == null || !activeNetwork.isConnected
}

fun getDeviceCountryCode(context: Context): String {
    var countryCode: String?

    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager?

    if (telephonyManager != null) {
        countryCode = telephonyManager.simCountryIso
        if (countryCode != null && countryCode.length == 2) {
            return countryCode.toLowerCase()
        }

        countryCode = if (telephonyManager.phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
            cdmaCountryIso
        } else {
            telephonyManager.networkCountryIso
        }

        if (countryCode != null && countryCode.length == 2) {
            return countryCode.toLowerCase()
        }
    }

    countryCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        context.resources.configuration.locales.get(0).country
    } else {
        context.resources.configuration.locale.country
    }

    return if (countryCode != null && countryCode.length == 2) {
        countryCode.toLowerCase()
    } else "us"
}

fun watchYoutubeVideo(id: String, context: Context) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:$id"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (isAppInstalled(context)) {
            intent.setClassName("com.google.android.youtube",
                    "com.google.android.youtube.WatchActivity")
        }
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        val intent = Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=$id"))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}