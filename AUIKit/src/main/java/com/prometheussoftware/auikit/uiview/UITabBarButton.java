package com.prometheussoftware.auikit.uiview;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.utility.ArrayUtility;

public class UITabBarButton extends UIButton {

    @Override
    public void setTintColor(UIColor tintColor) {
        super.setTintColor(tintColor);
        getTitleLabel().setTextColor(tintColor);
        getImageView().setTintColor(tintColor);
    }

    @Override
    public void initView() {
        super.initView();

        getTitleLabel().setFont(App.theme().Tab_Bar_Font());
        getImageView().setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        setBackgroundColor(App.theme().Tab_Bar_Background_Color());
    }

    @Override
    public void constraintLayout() {
        constraintHeightForView(getImageView(), App.constants().Tab_Bar_Icon_Height());
        constraintVertically(ArrayUtility.arrayOf(getImageView(), getTitleLabel()), Dimensions.Int_1(), Dimensions.Int_2(), Dimensions.Int_4(), false, ConstraintSet.TOP | ConstraintSet.BOTTOM);
        applyConstraints();
    }
}
