package com.prometheussoftware.auikit.classes;

import android.graphics.Typeface;

import com.prometheussoftware.auikit.common.AUIKitApplication;
import com.prometheussoftware.auikit.model.BaseModel;

public class UIFont extends BaseModel {

    private Typeface font;
    public int size;
    public int style;

    public static UIFont systemFont() {
        UIFont font = new UIFont();
        font.font = Typeface.DEFAULT;
        return font;
    }

    public static UIFont systemFont(int size) {
        UIFont font = new UIFont();
        font.font = Typeface.DEFAULT;
        font.size = size;
        return font;
    }

    public static UIFont systemFont(int size, int style) {
        UIFont font = new UIFont();
        font.font = Typeface.DEFAULT;
        font.size = size;
        font.style = style;
        return font;
    }

    public UIFont() {
    }

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
