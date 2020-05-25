package com.prometheussoftware.auikit.classes;

import android.graphics.Typeface;

import com.prometheussoftware.auikit.common.AUIKitApplication;

public class UIFont {

    private Typeface font;
    public int size;
    public int style;

    /** @param name Font name from assets.
     * Create a class AppTheme for retrieving font names */
    public UIFont(String name) {
        font = Typeface.createFromAsset(AUIKitApplication.getWindow().getAssets(), name);
    }

    /** @param name Font name from assets.
     * Create a class AppTheme for retrieving font names
     * @param style Pass values from static Typeface values for style:
     * NORMAL = 0;
     * BOLD = 1;
     * ITALIC = 2;
     * BOLD_ITALIC = 3;
     * */
    public UIFont(String name, int style) {
        font = Typeface.createFromAsset(AUIKitApplication.getWindow().getAssets(), name);
        this.style = style;
    }

    /** @param name Font name from assets.
     * Create a class AppTheme for retrieving font names
     * @param style Pass values from static Typeface values for style:
     * NORMAL = 0;
     * BOLD = 1;
     * ITALIC = 2;
     * BOLD_ITALIC = 3;
     * */
    public UIFont(String name, int style, int size) {
        font = Typeface.createFromAsset(AUIKitApplication.getWindow().getAssets(), name);
        this.style = style;
        this.size = size;
    }

    public Typeface get() {
        return font;
    }
}
