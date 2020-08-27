package com.prometheussoftware.auikit.common;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.prometheussoftware.auikit.common.protocols.AssetsProtocol;

public abstract class Assets implements AssetsProtocol {

    protected static Context context () {
        return MainApplication.getContext();
    }

    public static Drawable assetWithTint (int id, int color) {
        if (id <= 0) return null;

        Drawable drawable = ContextCompat.getDrawable(context(), id);
        DrawableCompat.setTint(drawable.mutate(), color);
        return drawable;
    }

    public static Drawable imageFromID(int id) {
        return (id == 0) ? null : ContextCompat.getDrawable(context(), id);
    }
}
