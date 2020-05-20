package com.prometheussoftware.auikit.uiview;

import android.view.Gravity;

import com.prometheussoftware.auikit.common.App;

public class UITextField extends UITextView {

    public UITextField() {
        super();
    }

    @Override public void initView() {
        super.initView();
        view().setLines(1);
        view().setSingleLine(true);
        int insets = (int)(App.constants().Min_TextView_Height() / 4.0f);
        view().setPadding(2*insets, insets, 2*insets, insets);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }
}
