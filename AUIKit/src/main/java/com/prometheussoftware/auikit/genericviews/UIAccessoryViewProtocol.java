package com.prometheussoftware.auikit.genericviews;

import android.util.Size;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIFont;

public interface UIAccessoryViewProtocol {
    void updateViews();
    Size size();
    UIFont font();
    default int numberOfLines() { return 1; }
    default int textPadding() { return 0; }
    default UIEdgeInsets imageInsets() {
        return new UIEdgeInsets();
    }

    default UIEdgeInsets labelInsets() {
        return new UIEdgeInsets();
    }
}
