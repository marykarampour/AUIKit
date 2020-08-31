package com.prometheussoftware.auikit.uiview;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIImage;

public class UIImageView extends UISingleLayerView <UIImageView.UIImageLayer> {

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

    @Override
    protected void createView() {
        setView(new UIImageLayer());
    }

    public ImageView view() {
        return getView().view;
    }

    public void setImage(UIImage image) {
        this.image = image;
        if (image != null) {

            Bitmap bitmap = image.getBitmap();
            if (bitmap != null) {
                view().setImageBitmap(bitmap);
            }
            else {
                Drawable drawable = image.getDrawable();
                view().setImageDrawable(drawable);
            }
        }
        else {
            view().setImageDrawable(null);
        }
        view().setBackground(null);
    }

    public void setScaleType(ImageView.ScaleType type) {
        view().setScaleType(type);
    }

    @Override
    public void setTintColor(int tintColor) {
        super.setTintColor(tintColor);
        image.setTintColor(tintColor);
    }

    protected static class UIImageLayer extends UISingleView <ImageView> {

        public UIImageLayer() {
            super();
            init();
        }

        @Override public void initView() {
            super.initView();
            view = new ImageView(getWindow());
        }
    }
}
