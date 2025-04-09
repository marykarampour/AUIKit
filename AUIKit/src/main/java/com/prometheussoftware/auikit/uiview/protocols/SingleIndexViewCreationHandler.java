package com.prometheussoftware.auikit.uiview.protocols;

import com.prometheussoftware.auikit.uiview.UIView;

public interface SingleIndexViewCreationHandler <T extends UIView> {
    T createView(int index);
}
