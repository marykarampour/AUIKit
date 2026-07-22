package com.prometheussoftware.auikit.uiview.protocols;

import com.prometheussoftware.auikit.classes.UITargetDelegate;

public interface UIEditingAccessoryProtocol {
    default void addEditingTarget(Object ID, UITargetDelegate target) {}
    default void addAccessoryTarget(Object ID, UITargetDelegate target) {}
}
