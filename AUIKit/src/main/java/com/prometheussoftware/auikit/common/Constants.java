package com.prometheussoftware.auikit.common;

import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;

import com.prometheussoftware.auikit.R;
import com.prometheussoftware.auikit.common.protocols.ConstantsProtocol;
import com.prometheussoftware.auikit.datamanagement.SQLConstantsProtocol;

public abstract class Constants implements ConstantsProtocol, SQLConstantsProtocol {

    public static final int NOT_FOUND_ID = -1;
    public static final boolean TARGET_ANDROID_EMULATOR = Build.FINGERPRINT.contains("sdk");

    public static String OS_Version_Device () {
        return Build.DEVICE + ", " + Build.MODEL + ", " + Android_STR() + Build.VERSION.SDK_INT;
    }

    /** Apps must add these values to their build.gradle
     * @<code>
     *      *  productFlavors {
     *      *      flower {
     *      *          minSdkVersion 26
     *      *          targetSdkVersion 29
     *      *          versionCode 5
     *      *          versionName '1.0.0'
     *      *          dimension 'default'
     *      *          buildConfigField("long", "VERSION_CODE", "${flower.versionCode}")
     *      *          buildConfigField("String","VERSION_NAME","\"${flower.versionName}\"")
     *      *      }
     *      *  }
     * </code>

     * */
    public String App_Version () {
        return Version_Name_STR() + " (" + Version_Code_STR() + ")";
    }

    public String App_Version_Name () {
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

    public static String Next_STR() {
        return StringResource(R.string.next);
    }

    public static String Back_STR() {
        return StringResource(R.string.back);
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

    public static String Username_STR() {
        return StringResource(R.string.username);
    }

    public static String Password_STR() {
        return StringResource(R.string.password);
    }

    public static String Fullname_STR() {
        return StringResource(R.string.fullname);
    }

    public static String Version_STR() {
        return StringResource(R.string.version);
    }

    public static String Android_STR() {
        return StringResource(R.string.android);
    }

    public static String Sign_In_STR() {
        return StringResource(R.string.sign_in);
    }

    public static String Logout_STR() {
        return StringResource(R.string.logout);
    }

    public static String Accept_STR() {
        return StringResource(R.string.accept);
    }

    public static String Save_STR() {
        return StringResource(R.string.save);
    }

    public static String Error_STR() {
        return StringResource(R.string.error);
    }

    @Override
    public String Generic_Success_Message() {
        return null;
    }

    @Override
    public String Generic_Failure_Message() {
        return null;
    }

    //regex

    public int Max_Regex_Chars() {
        return 10000;
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
