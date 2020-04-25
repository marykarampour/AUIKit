package com.prometheussoftware.auikit.utility;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import com.prometheussoftware.auikit.common.Constants;

public class ViewUtility {

    /** Very important: set ids then add subviews! Obviously a flaw in SDK */
    public static void setViewID (View view) {
        view.setId(View.generateViewId());
    }

    public static void addViewWithID (View view, ViewGroup parent) {
        if (view == null) return;
        if (view.getParent() != null) {
            if (view.getParent() != parent) {
                ((ViewGroup)view.getParent()).removeView(view);
            }
            else {
                return;
            }
        }
        view.setId(View.generateViewId());
        parent.addView(view);
    }

    public static void addViewWithID (View view, ViewGroup parent, int index) {
        view.setId(View.generateViewId());
        parent.addView(view, index);
    }

    public static boolean isChildView (ViewGroup parent, View child) {
        if (parent instanceof ViewGroup) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (parent.getChildAt(i) == child) return true;
            }
        }
        return false;
    }

    public static Bitmap imageFromView (View view) {

        int width = view.getWidth() > 0 ? view.getWidth() : Constants.Screen_Size().getWidth();
        int height = view.getHeight() > 0 ? view.getHeight() : Constants.Screen_Size().getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
}
