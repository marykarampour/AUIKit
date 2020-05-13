package com.prometheussoftware.auikit.common;

import android.graphics.Color;
import android.util.Size;

import com.prometheussoftware.auikit.uiview.UISingleLayerView;
import com.prometheussoftware.auikit.uiview.UISpinner;
import com.prometheussoftware.auikit.uiview.UIView;

public class AppSpinner {

    private UIView backView;
    private UISingleLayerView<UISpinner> spinnerLayer;
    private UISpinner hud;
    private Size hudSize = hudSize();

    public static AppSpinner spinner() {
        return spinnerInitializer.spinner;
    }

    private AppSpinner() {

        hud = new UISpinner();

        spinnerLayer = new UISingleLayerView<>();
        spinnerLayer.setViewBackgroundColor(App.theme().spinner_Frame_Color());
        spinnerLayer.setViewRadius(App.constants().Spinner_Corner_Radius());
        spinnerLayer.setView(hud);

        backView = new UIView();
        backView.setBackgroundColor(Color.TRANSPARENT);
        backView.addSubView(spinnerLayer);
        backView.constraintSizeForView(spinnerLayer, hudSize);
        backView.constraintCenterXForView(spinnerLayer);
        backView.constraintCenterYForView(spinnerLayer);
        backView.applyConstraints();
    }

    private static class spinnerInitializer {
        private static final AppSpinner spinner = new AppSpinner();
    }

    public static void show() {
        spinner().hud.show();
        UIView.runOnUiThread(() -> {
            spinner().backView.setGone(false);
            spinner().backView.bringToFront();
        });
    }

    public static void hide() {
        spinner().hud.hide();
        UIView.runOnUiThread(() -> {
            spinner().backView.setGone(true);
        });
    }
    public UIView view() {
        return backView;
    }

    public Size hudSize() {
        return new Size(Constants.Dimen_100(), Constants.Dimen_100());
    }

    public void setHudSize(Size hudSize) {
        this.hudSize = hudSize;

        if (backView != null && backView.isLoaded()) {
            backView.constraintSizeForView(spinnerLayer, hudSize);
            backView.applyConstraints();
        }
    }
}
