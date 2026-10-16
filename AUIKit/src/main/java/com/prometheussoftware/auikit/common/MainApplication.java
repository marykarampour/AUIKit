package com.prometheussoftware.auikit.common;

import static com.prometheussoftware.auikit.common.BaseActivity.CAMERA_PERMISSIONS_REQUEST_CODE;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.prometheussoftware.auikit.uiview.UIButton;
import com.prometheussoftware.auikit.uiview.UIView;

public abstract class MainApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context context;
    private static BaseActivity window;
    private static STATE state;

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
        state = STATE.FOREGROUND;
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
        state = STATE.BACKGROUND;
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }

    public static STATE getState() {
        return state;
    }

    public enum STATE {
        UNKNOWN,
        FOREGROUND,
        BACKGROUND
    }

    //endregion


    public static void addPhoneCallAction(UIButton button, String phoneNumber) {
        button.addTouchUpTarget(getWindow(), v -> {
            callPhone(phoneNumber);
        });
    }

    public static void callPhone(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(getWindow(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            getWindow().startActivity(callIntent);
        }
    }

    public static void copyTextToClipboard(String text) {

        ClipboardManager clipboard = (ClipboardManager) getWindow().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public static boolean hasCameraPermissions() {
        return ActivityCompat.checkSelfPermission(getWindow(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public static void getCameraPermissions() {
        if (!hasCameraPermissions()) {
            ActivityCompat.requestPermissions(getWindow(), new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIONS_REQUEST_CODE);
        }
    }

    public static boolean isAsleep() {
        PowerManager manager = (PowerManager) getWindow().getSystemService(POWER_SERVICE);
        return manager.isInteractive();
    }
}
