package com.prometheussoftware.auikit.common;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.prometheussoftware.auikit.uiview.UIButton;

public class BaseActivity extends AppCompatActivity {

    public static final int DOCUMENT_PICKER_REQUEST_CODE = 1000;
    public static final int IMAGE_PICKER_REQUEST_CODE = 2000;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 3000;

    protected int defaultOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(defaultOrientation);
        setWindow();
        super.onCreate(savedInstanceState);
        setActionBarProperties();
    }

    protected void setActionBarProperties() { }

    protected void setWindow() {
        MainApplication.setWindow(this);
    }

    //region window helpers

    public void dismissKeyboard() {

        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
    }

    public void addPhoneCallAction(UIButton button, String phoneNumber) {
        button.addTarget(this, v -> {
            addPhoneCallAction(phoneNumber);
        });
    }

    public void addPhoneCallAction(String phoneNumber) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            this.startActivity(callIntent);
            return;
        }
    }

    public void copyTextToClipboard(String text) {

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    public boolean hasCameraPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void getCameraPermissions() {
        if (!hasCameraPermissions()) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSIONS_REQUEST_CODE);
        }
    }

    //endregion
}
