package com.prometheussoftware.auikit.tableview;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
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
}
