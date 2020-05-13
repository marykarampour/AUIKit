package com.prometheussoftware.auikit.common;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.prometheussoftware.auikit.callback.CompletionCallback;
import com.prometheussoftware.auikit.uiview.UIButton;
import com.prometheussoftware.auikit.uiview.UITransitioningContainerView;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiviewcontroller.Navigation;
import com.prometheussoftware.auikit.uiviewcontroller.UINavigationController;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.utility.ViewUtility;

import java.util.HashMap;
import java.util.Map;

public class BaseWindow <V extends UIViewController> extends FragmentActivity {

    public static final int DOCUMENT_PICKER_REQUEST_CODE = 1000;
    public static final int IMAGE_PICKER_REQUEST_CODE = 2000;
    public static final int CAMERA_PERMISSIONS_REQUEST_CODE = 3000;

    protected int defaultOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    public DataDelegate dataDelegate;
    public ResultDelegate resultDelegate;

    private V rootViewController;
    private UIViewController visibleViewController;

    /** Root view, all view controller views are added to this view */
    private UITransitioningContainerView view;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {

        setRequestedOrientation(defaultOrientation);

        AUIKitApplication.setWindow(this);
        super.onCreate(savedInstanceState);

        view = new UITransitioningContainerView();
        ViewUtility.setViewID(view);

        setRootViewController(createRootViewController());
        setContentView(view);

        addOverlay(AppSpinner.spinner().view());

        dispatchDataDelegateWithIntent(getIntent());
        setWindowProperties();
    }

    protected void setWindowProperties() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_FULLSCREEN);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        getWindow().setAttributes(params);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override protected void onStart() {
        super.onStart();
        addOverlay(AppSpinner.spinner().view());
        AppSpinner.hide();
        setRequestedOrientation(defaultOrientation);
    }

    @Override protected void onRestart() {
        super.onRestart();
        setRequestedOrientation(defaultOrientation);
    }

    @Override protected void onResume() {
        super.onResume();
        setRequestedOrientation(defaultOrientation);
    }

    @Override protected void onPause() {
        super.onPause();
    }

    @Override protected void onStop() {
        super.onStop();
        dismissKeyboard();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        removeOverlay(AppSpinner.spinner().view());
    }

    @Override public void onBackPressed() { }

    //region root

    public V getRootViewController() {
        return rootViewController;
    }

    /** Subclass must implement to create custom rootViewController
     * @implNote Only initialize the root when implementing this
     * view of root will be null at this point, this is not a proper
     * place for view setup */
    protected V createRootViewController() {
        return (V)new UIViewController<>();
    }

    private void setRootViewController(V rootViewController) {
        this.rootViewController = rootViewController;
        presentVisibleViewController(rootViewController, Navigation.TRANSITION_ANIMATION.NONE, null);
    }

    //endregion

    //region view

    private void setVisibleViewController(
            boolean isDismiss,
            UIViewController viewController,
            Navigation.TRANSITION_ANIMATION animation,
            CompletionCallback completion) {

        if (visibleViewController != null) {
            boolean animated = animation != Navigation.TRANSITION_ANIMATION.NONE;
            visibleViewController.setAnimated(animated);
        }
        view.setCurrentContentView(viewController.view(), () -> {
            setVisibleViewController(viewController);
            rotate(viewController.preferredInterfaceOrientationForPresentation());
            view.applyTransitionAnimation(isDismiss, animation, completion);
        });
    }

    private void setVisibleViewController(UIViewController viewController) {
        this.visibleViewController = viewController;
    }

    public void presentVisibleViewController(
            UIViewController viewController,
            Navigation.TRANSITION_ANIMATION animation,
            CompletionCallback completion) {

        setVisibleViewController(false, viewController, animation, completion);
    }

    public void dismissVisibleViewController(
            UIViewController viewController,
            Navigation.TRANSITION_ANIMATION animation,
            CompletionCallback completion) {

        setVisibleViewController(true, viewController, animation, completion);
    }

    public void popToRootViewController() {
        popToViewController(rootViewController.getClass());
    }

    public void popToViewController(Class VCClass) {
        UIViewController visibleViewController = getVisibleViewController();
        popToViewController(visibleViewController, VCClass);
    }

    public void popToViewController(UIViewController vc, Class root) {

        if (vc.getClass() == root) return;

        if (UINavigationController.class.isInstance(vc)) {
            UINavigationController nav = (UINavigationController)vc;
            for (Navigation.Node<UIViewController> view : nav.getNavigationStack().getNodes()) {
                if (view.getClass() == root) {
                    nav.popToViewControllerAtIndex(0, false);
                    return;
                }
            }
        }

        UINavigationController nav = vc.getNavigationController();
        UIViewController presenting;
        if (nav != null) {
            presenting = nav.getPresentingViewController();
        }
        else {
            presenting = vc.getPresentingViewController();
        }
        vc.dismissViewController(Navigation.TRANSITION_ANIMATION.NONE, () -> popToViewController(presenting, root) );
    }

    //endregion


    //region orientation

    public void rotate(ORIENTATION orientation) {

        int deviceOrientation;
        switch (orientation) {
            case PORTRAIT: {
                deviceOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            }
            break;
            default:
                deviceOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        }
        if (defaultOrientation != deviceOrientation) {
            defaultOrientation = deviceOrientation;
            setRequestedOrientation(deviceOrientation);
        }
    }

    @Override public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(defaultOrientation);
    }

    public enum ORIENTATION {

        PORTRAIT(0),
        LANDSCAPE(1);

        ORIENTATION(int value) {
            this.value = value;
        }

        private int value;

        public int intValue() {
            return this.value;
        }

        private static Map map = new HashMap<>();

        static {
            for (ORIENTATION flavor : ORIENTATION.values()) {
                map.put(flavor.value, flavor);
            }
        }

        public static ORIENTATION valueOf(int flavor) {
            return (ORIENTATION) map.get(flavor);
        }
    }

    public UIViewController getVisibleViewController() {
        return visibleViewController;
    }

    //endregion

    //region overlay

    public void addOverlay (UIView overlay) {

        view.addSubView(overlay);
        view.constraintSidesForView(overlay);
        view.applyConstraints();
        view.bringChildToFront(overlay);
    }

    public void removeOverlay (UIView overlay) {
        view.removeView(overlay);
    }

    public void removeOverlaysOfType (Class OClass) {
        for (int i=0; i < view.getChildCount(); i++) {
            View vi = view.getChildAt(i);
            if (OClass.isInstance(vi)) {
                view.removeView(vi);
            }
        }
    }

    //endregion

    //region intent

    @Override protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dispatchDataDelegateWithIntent(intent);
    }

    protected void dispatchDataDelegateWithIntent(Intent intent) {
        if (dataDelegate != null && intent.getData() != null)
            dataDelegate.applicationDidOpenURI(this, intent.getData());
    }

    public interface DataDelegate {
        default void applicationDidOpenURI(BaseWindow window, Uri uri) { }
    }

    //endregion

    //region activity result

    @Override protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case DOCUMENT_PICKER_REQUEST_CODE:
            case IMAGE_PICKER_REQUEST_CODE: {
                if (resultCode == RESULT_OK) {
                    dispatchResultDelegateWithIntent(data);
                }
                else if (resultCode == RESULT_CANCELED) {
                    dispatchResultDelegateDidCancel();
                }
            }
            break;
            default:
                break;
        }
    }

    protected void dispatchResultDelegateWithIntent(Intent intent) {
        if (resultDelegate != null) {
            if (intent != null && intent.getData() != null) {
                resultDelegate.documentPickerDidReturnWithData(this, intent);
            }
            else {
                resultDelegate.documentPickerDidReturn(this);
            }
        }
    }

    protected void dispatchResultDelegateDidCancel() {
        if (resultDelegate != null)
            resultDelegate.documentPickerDidCancel(this);
    }

    public interface ResultDelegate {
        default void documentPickerDidReturnWithData(BaseWindow window, Intent data) { }
        default void documentPickerDidCancel(BaseWindow window) { }
        default void documentPickerDidReturn(BaseWindow window) { }
    }

    //endregion

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
