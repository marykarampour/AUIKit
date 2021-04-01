package com.prometheussoftware.auikit.common;

import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;

import com.prometheussoftware.auikit.R;
import com.prometheussoftware.auikit.BuildConfig;
import com.prometheussoftware.auikit.common.protocols.ConstantsProtocol;

import java.util.HashMap;

public class Constants implements ConstantsProtocol {

    public static final int NOT_FOUND_ID = -1;

    public static String OS_Version_Device () {
        return Build.DEVICE + ", " + Build.MODEL + ", Android " + Build.VERSION.SDK_INT;
    }

    public static String App_Version () {
        return BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")";
    }

    public static String App_Version_Name () {
        return App_Name() + App_Version();
    }

    public static String App_Name () {
        return MainApplication.getContext().getString(R.string.app_name);
    }

    public static String Platform_Type () {
        return "GCM";
    }

    public static Size Screen_Size_PIXEL() {

        DisplayMetrics metrics = MainApplication.getContext().getResources().getDisplayMetrics();
        float density = MainApplication.getContext().getResources().getDisplayMetrics().density;
        int height = (int) (metrics.heightPixels / density);
        int width = (int) (metrics.widthPixels / density);

        return new Size(width, height);
    }

    public static Size Screen_Size() {
        DisplayMetrics metrics = MainApplication.getContext().getResources().getDisplayMetrics();
        return new Size(metrics.widthPixels, metrics.heightPixels);
    }

    //helpers

    protected static String StringResource(int source) {
        return MainApplication.getContext().getResources().getString(source);
    }

    //strings

    public static String OK_STR() {
        return StringResource(R.string.ok);
    }

    public static String Cancel_STR() {
        return StringResource(R.string.cancel);
    }

    public static String Yes_STR() {
        return StringResource(R.string.yes);
    }

    public static String No_STR() {
        return StringResource(R.string.no);
    }

    public static String OR_STR() {
        return StringResource(R.string.or);
    }

    public static String Day_s_STR() {
        return StringResource(R.string.day_s);
    }

    public static String Hour_s_STR() {
        return StringResource(R.string.hour_s);
    }

    public static String Minute_s_STR() {
        return StringResource(R.string.minute_s);
    }

    //regex

    public int Max_Regex_Chars() {
        return Integer.MAX_VALUE;
    }

    public String Regex_Password() {
        return "^(?=.*?([A-Z]|[\\W]))(?=.*?[a-z])(?=.*?[0-9]).{8,48}$";
    }

    public String Regex_Email() {
        return "^([A-Z0-9a-z\\._%+-@]){0,256}$";
    }

    public String Regex_Phone() {
        return "^([0-9]+-)*[0-9]+$";
    }

}
