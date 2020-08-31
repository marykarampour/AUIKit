package com.prometheussoftware.auikit.viewcontrollers;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.tableview.UITableViewCell;

public class ExampleTableViewCell extends UITableViewCell {

    public ExampleTableViewCell() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();

        getTitleLabel().setTextColor(UIColor.black(1.0f));
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
}
