package com.prometheussoftware.auikit.classes;

import android.graphics.Typeface;

import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.model.BaseModel;

public class UIFont extends BaseModel {

    public enum STYLE {
        REGULAR,
        BOLD,
        ITALIC
    }

    private Typeface font;
    private int size;
    private STYLE style = STYLE.REGULAR;

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

    public static UIFont systemFont(int size, STYLE style) {
        UIFont font = new UIFont();
        font.font = Typeface.DEFAULT;
        font.size = size;
        font.style = style;
        return font;
    }

    public UIFont() {
        super();
    }

    public int style() {
        switch (this.style) {
            case BOLD:  return Typeface.BOLD;
            case ITALIC:return Typeface.ITALIC;
            default:    return Typeface.NORMAL;
        }
    }

    public int size() {
        return size;
    }

    /** @param name Font name from assets.
     * Create a class AppTheme for retrieving font names */
    public UIFont(String name) {
        font = Typeface.createFromAsset(MainApplication.getContext().getAssets(), name);
    }

    /** @param name Font name from assets.
     * Create a class AppTheme for retrieving font names
     * @param style Pass values from static Typeface values for style:
     * NORMAL = 0;
     * BOLD = 1;
     * ITALIC = 2;
     * BOLD_ITALIC = 3;
     * */
    public UIFont(String name, STYLE style) {
        font = Typeface.createFromAsset(MainApplication.getContext().getAssets(), name);
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
    public UIFont(String name, STYLE style, int size) {
        font = Typeface.createFromAsset(MainApplication.getContext().getAssets(), name);
        this.style = style;
        this.size = size;
    }

    public Typeface get() {
        return font;
    }
}
