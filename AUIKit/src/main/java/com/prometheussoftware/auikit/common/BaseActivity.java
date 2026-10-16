package com.prometheussoftware.auikit.common;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.classes.UITargetManager;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseActivity extends AppCompatActivity implements UITargetManager.Delegate {

    public static final int DOCUMENT_PICKER_REQUEST_CODE = 1000;
    public static final int IMAGE_PICKER_REQUEST_CODE = 2000;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 3000;

    protected int defaultOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    private final UITargetManager targetManager = new UITargetManager(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initBase();
        setRequestedOrientation(defaultOrientation);
        super.onCreate(savedInstanceState);
        setActionBarProperties();
        setWindow();
    }

    protected void setActionBarProperties() { }

    /** Called first in onCreate to set fields and constants such as defaultOrientation */
    protected void initBase() { }

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

    @Override
    public boolean targetWillSetTouch(UITargetDelegate target, boolean down) {
        return true;
    }

    @Override
    public boolean targetWillSetKey(UITargetDelegate target, boolean down) {
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        targetManager.handleOnKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        targetManager.handleOnKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }


    //region targets

    public void setTarget(UITargetDelegate.TouchUp target) {
        targetManager.setTarget(target);
    }

    public void setKeyTarget(UITargetDelegate.KeyUp target) {
        targetManager.setKeyTarget(target);
    }

    /** @apiNote It does not distinguish between different instances
     * of the same lambda, user must be careful not to add the
     * same target multiple times, that would result in the
     * lambda being executed multiple times. Set
     *  multiTargetEnabled = false to guarantee unique action on each
     *  touch event */
    public void addTarget(Object ID, UITargetDelegate target) {
        targetManager.addTarget(ID, target);
    }

    public void addTouchDownTarget(Object ID, UITargetDelegate.TouchDown target) {
        addTarget(ID, target);
    }

    public void addTouchUpTarget(Object ID, UITargetDelegate.TouchUp target) {
        addTarget(ID, target);
    }

    public void addKeyDownTarget(Object ID, UITargetDelegate.KeyDown target) {
        addTarget(ID, target);
    }

    public void addKeyUpTarget(Object ID, UITargetDelegate.KeyUp target) {
        addTarget(ID, target);
    }

    public void removeTarget(UITargetDelegate target) {
        targetManager.removeTarget(target);
    }

    public HashMap<Object, ArrayList<UITargetDelegate>> getTargets() {
        return targetManager.getTargets();
    }

    public boolean isMultiTargetEnabled() {
        return targetManager.isMultiTargetEnabled();
    }

    public void setMultiTargetEnabled(boolean multiTargetEnabled) {
        targetManager.setMultiTargetEnabled(multiTargetEnabled);
    }

    //endregion
}
