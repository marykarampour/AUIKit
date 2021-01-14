package com.prometheussoftware.auikit.utility;

public class ObjectUtility {

    public static String logTag (Object obj) {
        return obj.getClass().getSimpleName();
    }

    public static <T> T castTo (Object obj) {
        try {
            return (T)obj;
        }
        catch (Exception e) {
            return null;
        }
    }
}
