package com.prometheussoftware.auikit.genericviews;

import android.util.Size;
import android.view.MotionEvent;

public interface UICheckboxProtocol {
    UIAccessoryView checkView();
    void setCheckViewSize(Size size);
    Size checkViewSize();
    void switchCheckbox(UIAccessoryView view, MotionEvent event);
    void handleSwitchCheckbox(boolean on);
    Size estimatedSize();
}
