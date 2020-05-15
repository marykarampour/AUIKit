package com.prometheussoftware.auikit.common;

import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Size;

import com.prometheussoftware.auikit.R;
import com.prometheussoftware.auikit.BuildConfig;
import com.prometheussoftware.auikit.common.protocols.ConstantsProtocol;

import java.util.HashMap;

public abstract class Constants implements ConstantsProtocol {

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
        return AUIKitApplication.getContext().getString(R.string.app_name);
    }

    public static String Platform_Type () {
        return "GCM";
    }

    public static Size Screen_Size_PIXEL() {

        DisplayMetrics metrics = AUIKitApplication.getContext().getResources().getDisplayMetrics();
        float density = AUIKitApplication.getContext().getResources().getDisplayMetrics().density;
        int height = (int) (metrics.heightPixels / density);
        int width = (int) (metrics.widthPixels / density);

        return new Size(width, height);
    }

    public static Size Screen_Size() {
        DisplayMetrics metrics = AUIKitApplication.getContext().getResources().getDisplayMetrics();
        return new Size(metrics.widthPixels, metrics.heightPixels);
    }

    //helpers

    protected static int PixelDimension(int source) {
        return AUIKitApplication.getContext().getResources().getDimensionPixelSize(source);
    }

    protected static String StringResource(int source) {
        return AUIKitApplication.getContext().getResources().getString(source);
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


    //common dimensions

    public static Size size(int dimension) {
        return new Size(dimension, dimension);
    }

    public static int Dimen_1() {
        return PixelDimension(R.dimen.dimen_1);
    }

    public static int Dimen_2() {
        return PixelDimension(R.dimen.dimen_2);
    }

    public static int Dimen_4() {
        return PixelDimension(R.dimen.dimen_4);
    }

    public static int Dimen_8() {
        return PixelDimension(R.dimen.dimen_8);
    }

    public static int Dimen_16() {
        return PixelDimension(R.dimen.dimen_16);
    }

    public static int Dimen_32() {
        return PixelDimension(R.dimen.dimen_32);
    }

    public static int Dimen_64() { return PixelDimension(R.dimen.dimen_64); }

    public static int Dimen_128() { return PixelDimension(R.dimen.dimen_128); }

    public static int Dimen_256() { return PixelDimension(R.dimen.dimen_256); }

    public static int Dimen_512() { return PixelDimension(R.dimen.dimen_512); }

    public static int Dimen_1024() { return PixelDimension(R.dimen.dimen_1024); }


    public static int Dimen_6() {
        return PixelDimension(R.dimen.dimen_6);
    }

    public static int Dimen_10() {
        return PixelDimension(R.dimen.dimen_10);
    }

    public static int Dimen_12() {
        return PixelDimension(R.dimen.dimen_12);
    }

    public static int Dimen_14() {
        return PixelDimension(R.dimen.dimen_14);
    }

    public static int Dimen_18() {
        return PixelDimension(R.dimen.dimen_18);
    }

    public static int Dimen_20() {
        return PixelDimension(R.dimen.dimen_20);
    }

    public static int Dimen_22() {
        return PixelDimension(R.dimen.dimen_22);
    }

    public static int Dimen_24() {
        return PixelDimension(R.dimen.dimen_24);
    }

    public static int Dimen_28() {
        return PixelDimension(R.dimen.dimen_28);
    }

    public static int Dimen_36() {
        return PixelDimension(R.dimen.dimen_36);
    }

    public static int Dimen_38() {
        return PixelDimension(R.dimen.dimen_38);
    }

    public static int Dimen_40() {
        return PixelDimension(R.dimen.dimen_40);
    }

    public static int Dimen_44() {
        return PixelDimension(R.dimen.dimen_44);
    }

    public static int Dimen_46() {
        return PixelDimension(R.dimen.dimen_46);
    }

    public static int Dimen_48() {
        return PixelDimension(R.dimen.dimen_48);
    }

    public static int Dimen_52() {
        return PixelDimension(R.dimen.dimen_52);
    }

    public static int Dimen_56() {
        return PixelDimension(R.dimen.dimen_56);
    }

    public static int Dimen_60() {
        return PixelDimension(R.dimen.dimen_60);
    }

    public static int Dimen_72() { return PixelDimension(R.dimen.dimen_72); }

    public static int Dimen_76() { return PixelDimension(R.dimen.dimen_76); }

    public static int Dimen_80() { return PixelDimension(R.dimen.dimen_80); }

    public static int Dimen_88() { return PixelDimension(R.dimen.dimen_88); }

    public static int Dimen_92() { return PixelDimension(R.dimen.dimen_92); }

    public static int Dimen_96() { return PixelDimension(R.dimen.dimen_96); }

    public static int Dimen_100() { return PixelDimension(R.dimen.dimen_100); }

    public static int Dimen_120() { return PixelDimension(R.dimen.dimen_120); }

    public static int Dimen_144() { return PixelDimension(R.dimen.dimen_144); }

    public static int Dimen_152() { return PixelDimension(R.dimen.dimen_152); }

    public static int Dimen_160() { return PixelDimension(R.dimen.dimen_160); }

    public static int Dimen_176() { return PixelDimension(R.dimen.dimen_176); }

    public static int Dimen_192() { return PixelDimension(R.dimen.dimen_192); }

    public static int Dimen_200() { return PixelDimension(R.dimen.dimen_200); }

    public static int Dimen_320() { return PixelDimension(R.dimen.dimen_320); }

    public static int Dimen_400() { return PixelDimension(R.dimen.dimen_400); }

    public enum ALIGNMENT {
        TOP(0),
        CENTER_Y(1 << 0);

        private int value;
        private static HashMap<Integer, ALIGNMENT> map = new HashMap<>();

        static {
            for (ALIGNMENT al : values()) {
                map.put(al.value, al);
            }
        }

        ALIGNMENT(int i) { value = i; }

        public int intValue() { return value; }

        public static ALIGNMENT valueOf (int i) {
            return map.get(i);
        }

        public boolean isOption (int option) {
            return (option & value) == value;
        }

        public boolean isValue (int option) {
            return (option & value) == option;
        }

        public boolean isOption (ALIGNMENT option) {
            return (option.value & value) == value;
        }
    }
}
