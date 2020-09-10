package com.prometheussoftware.auikit.common;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.protocols.AssetsProtocol;

public class Assets implements AssetsProtocol {

    protected static Context context () {
        return MainApplication.getContext();
    }

    public static Drawable assetWithTint (int id, int color) {
        if (id <= 0) return null;

        Drawable drawable = ContextCompat.getDrawable(context(), id);
        drawable.mutate();
        drawable.setTint(color);
        return drawable;
    }

    public static Drawable drawableFromID(int id) {
        return (id == 0) ? null : ContextCompat.getDrawable(context(), id);
    }

    public static UIImage imageFromID(int id) {
        return new UIImage(id);
    }

    // navigation

    @Override
    public UIImage Shadow_Gradient_Image() {
        return imageFromID(AssetIDs.Shadow_Gradient_ID());
    }

    @Override
    public UIImage Left_Chevron_Image() {
        return imageFromID(AssetIDs.Left_Chevron__ID());
    }

    @Override
    public UIImage Right_Chevron_Image() {
        return imageFromID(AssetIDs.Right_Chevron__ID());
    }

    @Override
    public UIImage Up_Chevron_Image() {
        return imageFromID(AssetIDs.Up_Chevron__ID());
    }

    @Override
    public UIImage Down_Chevron_Image() {
        return imageFromID(AssetIDs.Down_Chevron__ID());
    }
}
