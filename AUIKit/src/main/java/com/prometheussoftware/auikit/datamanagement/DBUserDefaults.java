package com.prometheussoftware.auikit.datamanagement;

import android.content.SharedPreferences;

import com.prometheussoftware.auikit.classes.UserDefaults;

public class DBUserDefaults extends UserDefaults {

    private static final String SAVED_SQLITE_DB_VERSION_KEY = "SAVED_SQLITE_DB_VERSION_KEY";
    private static final String LAST_SQLITE_DB_SYNC_TIME = "LAST_SQLITE_DB_SYNC_TIME";

    public static String savedSQLiteDBVersion () {
        SharedPreferences preferences = preferences();
        return preferences.getString(SAVED_SQLITE_DB_VERSION_KEY, null);
    }

    public static void saveSQLiteDBVersion (String version) {
        SharedPreferences preferences = preferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SAVED_SQLITE_DB_VERSION_KEY, version);
        editor.apply();
    }

    public static int lastSQLiteSyncTime () {
        SharedPreferences preferences = preferences();
        return preferences.getInt(LAST_SQLITE_DB_SYNC_TIME, 0);
    }

    public static void updateLastSQLiteSyncTime (int timestamp) {
        SharedPreferences preferences = preferences();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAST_SQLITE_DB_SYNC_TIME, timestamp);
        editor.apply();
    }
}
