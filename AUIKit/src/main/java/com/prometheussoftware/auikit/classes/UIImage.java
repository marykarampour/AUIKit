package com.prometheussoftware.auikit.classes;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.prometheussoftware.auikit.common.AUIKitApplication;
import com.prometheussoftware.auikit.common.Assets;
import com.prometheussoftware.auikit.utility.DataUtility;

public class UIImage {

    private Bitmap bitmap;
    private Drawable drawable;
    private int tintColor;

    public UIImage() {
    }

    public UIImage(int image) {
        this.drawable = Assets.assetWithTint(image, getTintColor());
    }

    public UIImage(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public UIImage(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public Bitmap getBitmap() {
        if (bitmap != null) return bitmap;
        else if (drawable != null) return DataUtility.imageFromDrawable(drawable);
        return null;
    }

    public Drawable getDrawable() {
        if (drawable != null) return drawable;
        else if (bitmap != null) return new BitmapDrawable(AUIKitApplication.getWindow().getResources(), bitmap);
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
