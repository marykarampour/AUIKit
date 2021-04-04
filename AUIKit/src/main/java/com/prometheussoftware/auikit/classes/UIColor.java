package com.prometheussoftware.auikit.classes;

import android.graphics.Color;

import com.prometheussoftware.auikit.R;
import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.model.BaseModel;

public class UIColor extends BaseModel {

    private int hex;

    public UIColor() {
    }

    public UIColor(int colorID, float alpha) {
        hex = colorWithAlpha(colorID, alpha);
    }

    public static UIColor build(int colorID, float alpha) {
        return new UIColor(colorID, alpha);
    }

    public static UIColor build(int colorID) {
        return new UIColor(colorID, 1.0f);
    }

    /** Creates a hex color with transparency from color components
     * @param red Red color component 0-255
     * @param green Green color component 0-255
     * @param blue Blue color component 0-255
     * @param alpha Between 0.0-1.0 is transparency */
    public static UIColor build(int red, int green, int blue, float alpha) {
        int color = Color.argb((int) (alpha*255), red, green, blue);
        return new UIColor(color, 1.0f);
    }

    /** Creates a hex color with transparency from color components
     * @param red Red color component 0.0-1.0
     * @param green Green color component 0.0-1.0
     * @param blue Blue color component 0.0-1.0
     * @param alpha Between 0.0-1.0 is transparency */
    public static UIColor build(float red, float green, float blue, float alpha) {
        int color = Color.argb((int) (alpha*255), (int) (red*255), (int) (green*255), (int) (blue*255));
        return new UIColor(color, 1.0f);
    }

    /** Creates a hex color with transparency from colorID
     * @param colorID ID of color from Resources, i.e. color.xml
     * @param alpha Between 0.0-1.0 is transparency */
    private static int colorWithAlpha (int colorID, float alpha) {
        int color = colorID;
        try {
            color = MainApplication.getContext().getResources().getColor(colorID, null);
        } catch (Exception e) {}
        return (color & 0x00FFFFFF) | ((int)(alpha*255) << 24);
    }

    /** Creates a color from a hex string */
    public static UIColor build(String hex) {
        int color = Color.parseColor(hex);
        return new UIColor(color, 1.0f);
    }

    /** Creates a color with transparency from a hex string */
    public static UIColor build(String hex, float alpha) {
        int color = Color.parseColor(hex);
        return new UIColor(color, alpha);
    }

    public int get() {
        return hex;
    }

    public int red() {
        return (hex >> 16) & 0xFF;
    }

    public int green() {
        return (hex >> 8) & 0xFF;
    }

    public int blue() {
        return hex & 0xFF;
    }

    public static boolean hasNoColor (UIColor color) {
        return color == null || color.get() == 0;
    }

    //region pure colors

    public static UIColor black(float alpha) {
        return UIColor.build(Color.BLACK, alpha);
    }

    public static UIColor white(float alpha) {
        return UIColor.build(Color.WHITE, alpha);
    }

    public static UIColor green(float alpha) {
        return UIColor.build(Color.GREEN, alpha);
    }

    public static UIColor gray(float alpha) {
        return UIColor.build(Color.GRAY, alpha);
    }

    public static UIColor red(float alpha) {
        return UIColor.build(Color.RED, alpha);
    }

    public static UIColor blue(float alpha) {
        return UIColor.build(Color.BLUE, alpha);
    }

    public static UIColor yellow(float alpha) {
        return UIColor.build(Color.YELLOW, alpha);
    }

    public static UIColor magenta(float alpha) {
        return UIColor.build(Color.MAGENTA, alpha);
    }

    public static UIColor cyan(float alpha) {
        return UIColor.build(Color.CYAN, alpha);
    }

    public static UIColor clear() {
        return UIColor.build(Color.TRANSPARENT, 0.0f);
    }

    //endregion
}
