package com.prometheussoftware.auikit.utility;

public class ObjectUtility {

    public static String logTag (Object obj) {
        return obj.getClass().getSimpleName();
    }
}
