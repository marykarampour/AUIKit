package com.prometheussoftware.auikit.uiview;

import android.view.Gravity;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.utility.StringUtility;

public class UIButton extends UIControl {

    private UILabel     titleLabel;
    private UIImageView imageView;

    private UIEdgeInsets titleEdgeInsets = new UIEdgeInsets();
    private UIEdgeInsets imageEdgeInsets = new UIEdgeInsets();

    private UIColor normalTextColor;
    private UIColor highlightTextColor;

    static {
        BaseModel.Register(UIButton.class);
    }

    public UIButton() {
        super();
        init();
    }

    @Override public void initView() {
        super.initView();
        titleLabel = new UILabel();
        titleLabel.setGravity(Gravity.CENTER);
        titleLabel.setBackgroundColor(UIColor.clear());

        imageView = new UIImageView();
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubview(titleLabel);
        addSubview(imageView);
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

    public void setImage(UIImage image) {
        if (imageView.getTintColor() == null) {
            imageView.setTintColor(getTintColor());
        }
        imageView.setImage(image);
    }

    public void setBorderColor(UIColor tintColor) {
        imageView.setBorderColor(tintColor);
    }

    public void setBorderWidth(int width) {
        imageView.setBorderWidth(width);
    }

    public void setCornerRadius(float radius) {
        imageView.setCornerRadius(radius);
    }

    public void setScaleType(ImageView.ScaleType type) {
        imageView.setScaleType(type);
    }

    public void setTextColor(UIColor color) {
        titleLabel.setTextColor(color);
    }

    @Override
    public void setTintColor(UIColor tintColor) {
        super.setTintColor(tintColor);
        imageView.setTintColor(tintColor);
    }

    @Override
    public void setHighlighted(boolean highlighted) {
        super.setHighlighted(highlighted);
        if (highlightTextColor != null) {
            setTextColor(highlighted ? highlightTextColor : normalTextColor);
        }
    }

    public void setHighlightTextColor(UIColor highlightTextColor) {
        this.highlightTextColor = highlightTextColor;
    }

    public void setNormalTextColor(UIColor normalTextColor) {
        this.normalTextColor = normalTextColor;
        setTextColor(normalTextColor);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || !UIButton.class.isAssignableFrom(obj.getClass())) return false;

        UIButton button = (UIButton) obj;
        boolean thisEmpty = StringUtility.isEmpty(titleLabel.getText());
        boolean empty = StringUtility.isEmpty(button.titleLabel.getText());

        if (thisEmpty && empty) {
            if (imageView.getImage() == null && button.imageView.getImage() == null) return super.equals(obj);

            if (button.imageView.getImage() != null && imageView.getImage() != null) {
                if ((imageView.getImage().isEmpty() && !button.imageView.getImage().isEmpty()) || (!imageView.getImage().isEmpty() && button.imageView.getImage().isEmpty()))
                    return false;
                else
                    return imageView.getImage().equals(button.imageView.getImage());
            }
            else {
                return false;
            }
        }
        else {
            if (!thisEmpty && !empty) {
                return titleLabel.getText().equals(button.titleLabel.getText());
            }
            else {
                return false;
            }
        }
    }
}
