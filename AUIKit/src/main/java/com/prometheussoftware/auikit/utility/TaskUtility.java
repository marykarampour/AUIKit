package com.prometheussoftware.auikit.utility;

public class TaskUtility {

    public static void runInBackground (Runnable runnable) {
        new Thread(runnable).start();
    }
}
