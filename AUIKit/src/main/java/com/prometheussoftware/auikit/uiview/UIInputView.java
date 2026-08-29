package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.List;

public class UIInputView extends UIView {

    private UILabel label;
    private UIButton button;
    private UITextField textField;
    private StringUtility.TYPE format;

    private int fieldWidth;
    private int horizontalMargin;
    private int verticalMargin;

    static {
        Identifier.Register(UIInputView.class);
    }

    public UIInputView () {
        this(StringUtility.TYPE.STRING, 0, 0, 0);
    }

    public UIInputView (int padding) {
        this(StringUtility.TYPE.STRING, App.constants().Input_TextField_Width(), padding, padding);
    }

    public UIInputView (int fieldWidth, int padding) {
        this(StringUtility.TYPE.STRING, fieldWidth, padding, padding);
    }

    public UIInputView (StringUtility.TYPE type, int fieldWidth, int horizontalMargin, int verticalMargin) {
        super();
        this.format = type;
        this.fieldWidth = fieldWidth;
        this.horizontalMargin = horizontalMargin;
        this.verticalMargin = verticalMargin;
        init();
    }

    @Override
    public void initView() {
        super.initView();
        label = new UILabel();
        button = new UIButton();

        textField = new UITextField();
        textField.setFormat(format);
        textField.setCornerRadius(App.constants().Control_Corner_Radius());
        textField.setBorderWidth(App.constants().Border_Width());
        textField.setBorderColor(App.theme().TextField_Placeholder_Color());
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubview(label);
        addSubview(button);
        addSubview(textField);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        int width = Constants.Screen_Size().getWidth() - fieldWidth;
        List<UIView> views = new ArrayList();
        views.add(label);
        views.add(button);
        views.add(textField);

        constraintWidthForView(label, width);
        constraintWidthForView(button, Dimensions.Int_32());
        constraintHorizontally(views, horizontalMargin, horizontalMargin, verticalMargin, false);
        applyConstraints();
    }

    public UILabel getLabel() {
        return label;
    }

    public UIButton getButton() {
        return button;
    }

    public UITextField getTextField() {
        return textField;
    }

    public void setIndexPath(IndexPath path) {
        textField.setIndexPath(path);
    }
}
