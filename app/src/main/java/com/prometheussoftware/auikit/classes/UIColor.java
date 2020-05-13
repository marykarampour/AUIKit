package com.prometheussoftware.auikit.classes;

import com.prometheussoftware.auikit.R;
import com.prometheussoftware.auikit.common.MainApplication;

public class UIColor {

    private int id;

    public UIColor(int colorID, float alpha) {
        id = colorWithAlpha(colorID, alpha);
    }

    public static UIColor build(int colorID, float alpha) {
        return new UIColor(colorID, alpha);
    }

    public static UIColor build(int colorID) {
        return new UIColor(colorID, 1.0f);
    }

    /** Creates a hex color with transparency from colorID
     * @param colorID ID of color from Resources, i.e. color.xml
     * @param alpha Between 0.0-1.0 is transparency */
    private static int colorWithAlpha (int colorID, float alpha) {
        int color = MainApplication.getContext().getResources().getColor(colorID);
        return (color & 0x00ffffff) | ((int)(alpha*255) << 24);
    }

    public int get() {
        return id;
    }

    //region pure colors

    public static int black(float alpha) {
        return colorWithAlpha(R.color.black, alpha);
    }

    public static int white(float alpha) {
        return colorWithAlpha(R.color.white, alpha);
    }

    public static int green(float alpha) {
        return colorWithAlpha(R.color.green, alpha);
    }

    public static int red(float alpha) {
        return colorWithAlpha(R.color.red, alpha);
    }

    public static int blue(float alpha) {
        return colorWithAlpha(R.color.blue, alpha);
    }

    public static int yellow(float alpha) {
        return colorWithAlpha(R.color.yellow, alpha);
    }
    public static int pink(float alpha) {
        return colorWithAlpha(R.color.pink, alpha);
    }

    public static int cyan(float alpha) {
        return colorWithAlpha(R.color.cyan, alpha);
    }

    //endregion
}
