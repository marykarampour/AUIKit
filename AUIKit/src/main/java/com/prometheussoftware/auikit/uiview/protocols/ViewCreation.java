package com.prometheussoftware.auikit.uiview.protocols;

import com.prometheussoftware.auikit.uiview.UIView;

public interface ViewCreation <V extends UIView> {
    default V view() { return (V)new UIView(); };
    default V view(int index) { return view(); };
}
