package com.prometheussoftware.auikit.utility;

import android.util.Size;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;

public class ConstraintUtility {

    public static void constraintSidesForView (ConstraintSet set, View view) {
        set.connect(view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END);
        set.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
        set.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
    }

    public static void constraintSidesForView (ConstraintSet set, View view, UIEdgeInsets insets) {
        set.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, insets.left);
        set.connect(view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, insets.right);
        set.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, insets.top);
        set.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, insets.bottom);
    }

    public static void constraintForView (ConstraintSet set, int constraint, View view) {
        set.connect(view.getId(), constraint, ConstraintSet.PARENT_ID, constraint, 0);
    }

    public static void constraintForView (ConstraintSet set, int constraint, View view, int padding) {
        set.connect(view.getId(), constraint, ConstraintSet.PARENT_ID, constraint, padding);
    }

    public static void constraintSizeForView (ConstraintSet set, View view, Size size) {
        set.constrainHeight(view.getId(), size.getHeight());
        set.constrainWidth(view.getId(), size.getWidth());
    }

    public static void constraintSameForViews (ConstraintSet set, int constraint, View view1, View view2, int padding) {
        set.connect(view1.getId(), constraint, view2.getId(), constraint, padding);
    }

    public static void constraintSameForViews (ConstraintSet set, int constraint, View view1, View view2) {
        set.connect(view1.getId(), constraint, view2.getId(), constraint);
    }

    public static void constraintSameForViews (ConstraintSet set, View view1, View view2) {
        constraintSameForViews(set, ConstraintSet.TOP, view1, view2);
        constraintSameForViews(set, ConstraintSet.BOTTOM, view1, view2);
        constraintSameForViews(set, ConstraintSet.START, view1, view2);
        constraintSameForViews(set, ConstraintSet.END, view1, view2);
    }

    public static void removeAllCosntraints (ConstraintSet set, ConstraintLayout view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View vi = view.getChildAt(i);
            set.clear(vi.getId());
        }
        set.applyTo(view);
    }
}
