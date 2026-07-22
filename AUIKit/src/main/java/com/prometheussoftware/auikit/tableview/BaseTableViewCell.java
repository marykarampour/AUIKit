package com.prometheussoftware.auikit.tableview;

import android.util.Size;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Dimensions;

public class BaseTableViewCell extends UITableViewCell {

    public BaseTableViewCell() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();

        getTitleLabel().setTextColor(App.theme().Black_Blue_Color());
        getTitleLabel().setFont(App.theme().Medium_Regular_Font());
        contentView.setBackgroundColor(UIColor.white(1.0f));
    }

    @Override
    protected int leftPadding() {
        return Dimensions.Int_8();
    }

    @Override
    protected int rightPadding() {
        return Dimensions.Int_8();
    }

    @Override
    protected UIEdgeInsets insets() {
        return new UIEdgeInsets(Dimensions.Int_4(), Dimensions.Int_8(), Dimensions.Int_4(), Dimensions.Int_8());
    }

    public static class Editing extends BaseTableViewCell {

        @Override
        public void setEditingStyle(EDITING_STYLE editingStyle) {
            super.setEditingStyle(editingStyle);

            UIImage image = null;
            switch (editingStyle) {
                case INSERT:
                    image = App.assets().Plus_Circle_Fill_Image();
                    break;
                case DELETE:
                    image = App.assets().Minus_Circle_Fill_Image();
                    break;
                default:
                    break;
            }
            getImageView().setImage(image);
        }

        @Override
        public Size leftViewSize() {
            return Dimensions.size(Dimensions.Int_28());
        }
    }
}
