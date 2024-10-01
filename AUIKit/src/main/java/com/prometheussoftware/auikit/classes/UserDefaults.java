package com.prometheussoftware.auikit.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.prometheussoftware.auikit.common.MainApplication;

public class UserDefaults {

    private static final String SHARED_USER_DEFAULTS_KEY = "SHARED_USER_DEFAULTS_KEY";

    protected static SharedPreferences preferences() {
        return MainApplication.getContext().getSharedPreferences(SHARED_USER_DEFAULTS_KEY, Context.MODE_PRIVATE);
    }
}
