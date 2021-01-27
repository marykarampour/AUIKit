package com.prometheussoftware.auikit.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.uiview.UIView;

public abstract class MainApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context context;
    private static BaseActivity window;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(this);
    }

    public static Context getContext() {
        return context;
    }

    public static BaseActivity getWindow() {
        return window;
    }

    public static void setWindow(BaseActivity window) {
        MainApplication.window = window;
        UIView.setActivity(window);
    }

    public static void setActivity(Activity activity) {
        if (activity instanceof BaseActivity)
            setWindow((BaseActivity) activity);
    }

    //region activity life cycle

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        setActivity(activity);
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    //endregion
}
