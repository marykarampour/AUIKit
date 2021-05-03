package com.prometheussoftware.auikit.utility;

import android.util.Log;

import com.google.gson.Gson;
import com.prometheussoftware.auikit.BuildConfig;

public class DEBUGLOG {

    private final static boolean isDebug = BuildConfig.DEBUG;

    public DEBUGLOG () { }

    public static void s (String str) {
        if (!isDebug) return;
        Log.d("static -> ", str);
    }

    public static void s (String tag, String str) {
        if (!isDebug) return;
        Log.d(tag, str);
    }

    public static void s (Class T, String str) {
        if (!isDebug) return;
        Log.d(T.getSimpleName(), str);
    }

    public static void s (Object obj, String str) {
        if (!isDebug) return;
        Log.d(ObjectUtility.logTag(obj), str);
    }

    public static void s (String str, Object obj) {
        DEBUGLOG.s(str, new Gson().toJson(obj));
    }

    public static void s (String str, Exception e) {
        DEBUGLOG.s(str, e.getStackTrace());
    }

    public static void s (Exception e) {
        if (!isDebug) return;
        Log.d("Exception -> ", e.getStackTrace().toString());
    }

    public static void s (Exception e, Object obj) {
        if (!isDebug) return;
        Log.d("Exception -> " + e.getStackTrace().toString(), " object -> " + new Gson().toJson(obj));
    }
}
