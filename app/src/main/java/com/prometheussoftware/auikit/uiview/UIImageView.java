package com.prometheussoftware.auikit.uiview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.prometheussoftware.auikit.classes.UIImage;

public class UIImageView extends UISingleView <ImageView> {

    private UIImage image;

    public UIImageView() {
        super();
        init();
    }

    public UIImageView(UIImage image) {
        super();
        this.image = image;
        init();
    }

    public UIImageView(int image) {
        super();
        this.image = new UIImage(image);
        init();
    }

    @Override public void initView() {
        super.initView();
        view = new ImageView(getWindow());
    }

    public void setImage(UIImage image) {
        this.image = image;
        if (image != null) {

            Bitmap bitmap = image.getBitmap();
            if (bitmap != null) {
                view.setImageBitmap(bitmap);
            }
            else {
                Drawable drawable = image.getDrawable();
                view.setImageDrawable(drawable);
            }
        }
        else {
            view.setImageDrawable(null);
        }
        view.setBackground(null);
    }

    public void setScaleType(ImageView.ScaleType type) {
        view.setScaleType(type);
    }

    @Override
    public void setTintColor(int tintColor) {
        super.setTintColor(tintColor);
        image.setTintColor(tintColor);
    }
}
