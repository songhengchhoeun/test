package kh.com.mysabay.sdk.utils;

import android.util.Log;

import kh.com.mysabay.sdk.BuildConfig;

/**
 * Created by Tan Phirum on 5/25/15.
 */
public class LogUtil {

    public static void debug(String message) {
        if (BuildConfig.DEBUG)
            Log.d("phirum", message);
    }

    public static void debug(String tag, String message) {
        if (BuildConfig.DEBUG)
            Log.d(tag, message);
    }

    public static void info(String tag, String message) {
        Log.i(tag, message);
    }

    public static void error(String tag, String message) {
        Log.e(tag, message);
    }
}
