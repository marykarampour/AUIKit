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
        view().setPadding(insets, 0, insets, 0);
        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

        setBackgroundColor(App.theme().TextField_Background_Color());
        setHintTextColor(App.theme().TextField_Placeholder_Color());
        setTextColor(App.theme().TextField_Text_Color());
    }
}
