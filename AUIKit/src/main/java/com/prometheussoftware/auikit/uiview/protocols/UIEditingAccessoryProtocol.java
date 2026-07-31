package com.prometheussoftware.auikit.uiview.protocols;

import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.tableview.UITableViewCell;

public interface UIEditingAccessoryProtocol {
    void setEditingStyle(UITableViewCell.EDITING_STYLE editingStyle);
    default void addEditingTarget(Object ID, UITargetDelegate target) {}
    default void addAccessoryTarget(Object ID, UITargetDelegate target) {}
}
