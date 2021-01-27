package com.prometheussoftware.auikit.classes;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.prometheussoftware.auikit.common.AUIKitApplication;
import com.prometheussoftware.auikit.common.Assets;
import com.prometheussoftware.auikit.common.MainApplication;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.utility.DataUtility;

public class UIImage extends BaseModel {

    private Bitmap bitmap;
    private Drawable drawable;
    private int image;
    private int tintColor;

    public UIImage() {
        super();
    }

    public UIImage(int image) {
        super();
        setImage(image);
    }

    public UIImage(Bitmap bitmap) {
        super();
        this.bitmap = bitmap;
    }

    public UIImage(Drawable drawable) {
        super();
        this.drawable = drawable;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.image = 0;
        this.drawable = null;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.drawable.setTint(tintColor);
        this.image = 0;
        this.bitmap = null;
    }

    public void setImage(int image) {
        this.image = image;
        this.drawable = null;
        this.bitmap = null;
    }

    public Bitmap bitmap() {
        return bitmap;
    }

    public Drawable drawable() {
        if (drawable != null) {
            drawable.setTint(tintColor);
            return drawable;
        }
        else if (0 < image) {
            Drawable drawable = Assets.assetWithTint(image, tintColor);
            return drawable;
        }
        return null;
    }

    public Bitmap getBitmap() {
        if (bitmap != null) return bitmap;
        Drawable drawable = drawable();
        return DataUtility.imageFromDrawable(drawable);
    }

    public Drawable getDrawable() {
        Drawable drawable = drawable();
        if (drawable != null) {
            return drawable;
        }
        else if (bitmap != null) {
            drawable = new BitmapDrawable(MainApplication.getContext().getResources(), bitmap);
            drawable.setTint(tintColor);
            return drawable;
        }
        return null;
    }

    public int getTintColor() {
        return tintColor;
    }

    public void setTintColor(int tintColor) {
        this.tintColor = tintColor;
    }

    public void dispose() {
        bitmap = null;
        drawable = null;
    }

    @Override
    protected void finalize() throws Throwable {

        try {
            dispose();
        }
        catch (Exception e) { }
        finally {
            super.finalize();
        }
    }
}
