package com.prometheussoftware.auikit.genericviews;

import android.util.Size;
import android.view.MotionEvent;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.uiview.UIView;

public class UICheckbox <L extends UIAccessoryView, R extends UIAccessoryView> extends UIMultiViewLabel <L, R, UIView> {

    public UICheckbox() {
        super();
    }

    static {
        Identifier.Register(UICheckbox.class);
    }

    @Override public void initView() {
        super.initView();
        rightView.setTarget(v -> switchCheckbox(rightView, rightView.getLastTouch()));
        setRightViewSize(rightView.size());
    }

    @Override public void createRightView() {
        rightView = (R) UIAccessoryView.build(UIAccessoryView.TYPE.IMAGE);
        rightView.setEnabled(true);
    }

    @Override public R getRightView() {
        return super.getRightView();
    }

    @Override public L getLeftView() {
        return super.getLeftView();
    }

    protected void switchCheckbox(UIAccessoryView view, MotionEvent event) {
        rightView.setOn(!rightView.isOn());
        handleSwitchCheckbox(rightView.isOn());
    }

    protected void handleSwitchCheckbox(boolean on) {
        setSelected(on);
    }

    public Size estimatedSize() {
        return new Size(Constants.Screen_Size().getWidth(), App.constants().Default_Row_Height());
    }

}
