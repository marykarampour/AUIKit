package com.prometheussoftware.auikit.genericviews;

import android.util.Size;
import android.view.MotionEvent;

public interface UICheckboxProtocol {
    UIAccessoryView checkView();
    void switchCheckbox(UIAccessoryView view, MotionEvent event);
    void handleSwitchCheckbox(boolean on);
    Size estimatedSize();
}
