package com.prometheussoftware.auikit.genericviews;

import android.util.Size;
import android.view.MotionEvent;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.uiview.UIView;

public abstract class UICheckbox <L extends UIView, R extends UIView> extends UIMultiViewLabel <L, R, UIView> implements UICheckboxProtocol {

    public enum TYPE {
        LEFT,
        RIGHT
    }

    public UICheckbox() {
        super();
        init();
    }

    static {
        Identifier.Register(UICheckbox.class);
    }

    public UICheckbox withType (TYPE type) {
        switch (type) {
            case LEFT: return new Left();
            default:   return new Right();
        }
    }

    @Override
    public void init() {
        setSize();
        super.init();
    }

    @Override public void initView() {
        super.initView();
        checkView().setTarget(v -> switchCheckbox(checkView(), checkView().getLastTouch()));
        setRightViewSize(checkView().size());
    }


    @Override public R getRightView() {
        return super.getRightView();
    }

    @Override public L getLeftView() {
        return super.getLeftView();
    }

    @Override public void switchCheckbox(UIAccessoryView view, MotionEvent event) {
        checkView().setOn(!checkView().isOn());
        handleSwitchCheckbox(checkView().isOn());
    }

    @Override public void handleSwitchCheckbox(boolean on) {
        checkView().setSelected(on);
    }

    @Override public Size estimatedSize() {
        return new Size(Constants.Screen_Size().getWidth(), App.constants().Default_Row_Height());
    }

    public static class Left extends UICheckbox <UIAccessoryView, UIView> {

        @Override
        public UIAccessoryView checkView() {
            return leftView;
        }

        @Override public void createLeftView() {
            leftView = UIAccessoryView.build(UIAccessoryView.TYPE.IMAGE);
            leftView.setEnabled(true);
        }
    }

    public static class Right extends UICheckbox <UIView, UIAccessoryView> {

        @Override
        public UIAccessoryView checkView() {
            return rightView;
        }

        @Override public void createRightView() {
            rightView = UIAccessoryView.build(UIAccessoryView.TYPE.IMAGE);
            rightView.setEnabled(true);
        }
    }

}
