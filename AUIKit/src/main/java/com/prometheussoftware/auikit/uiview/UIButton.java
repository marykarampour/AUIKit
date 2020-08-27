package com.prometheussoftware.auikit.uiview;

import android.view.Gravity;
import android.widget.ImageView;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.classes.UIImage;

public class UIButton extends UIControl {

    private UILabel     titleLabel;
    private UIImageView imageView;

    private UIEdgeInsets titleEdgeInsets;
    private UIEdgeInsets imageEdgeInsets;

    public UIButton() {
        super();
        init();
    }

    @Override public void initView() {
        super.initView();
        titleLabel = new UILabel();
        titleLabel.setGravity(Gravity.CENTER);

        imageView = new UIImageView();
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubView(titleLabel);
        addSubView(imageView);
    }

    @Override
    public void constraintLayout() {
        removeAllConstraints();
        super.constraintLayout();
        constraintSidesForView(titleLabel, titleEdgeInsets);
        constraintSidesForView(imageView, imageEdgeInsets);
        applyConstraints();
    }

    public UILabel getTitleLabel() {
        return titleLabel;
    }

    public UIImageView getImageView() {
        return imageView;
    }

    public void setTitleEdgeInsets(UIEdgeInsets titleEdgeInsets) {
        this.titleEdgeInsets = titleEdgeInsets;
        if (isLoaded()) {
            constraintLayout();
        }
    }

    public void setImageEdgeInsets(UIEdgeInsets imageEdgeInsets) {
        this.imageEdgeInsets = imageEdgeInsets;
        if (isLoaded()) {
            constraintLayout();
        }
    }

    public void setText(String s) {
        titleLabel.getView().setText(s);
    }

    public void setFont(UIFont font) {
        titleLabel.setFont(font);
    }

    public void setTextColor(UIColor color) {
        titleLabel.setTextColor(color);
    }

    public void setImage(UIImage image) {
        if (imageView.getTintColor() == null) {
            imageView.setTintColor(getTintColor());
        }
        imageView.setImage(image);
    }

    public void setScaleType(ImageView.ScaleType type) {
        imageView.setScaleType(type);
    }
}
