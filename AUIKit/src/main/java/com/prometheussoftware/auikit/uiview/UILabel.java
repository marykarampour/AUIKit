package com.prometheussoftware.auikit.uiview;

import android.widget.TextView;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

public class UILabel extends UISingleView <TextView> {

    public UILabel() {
        super();
        init();
    }

    @Override public void initView() {
        super.initView();
        view = new TextView(getWindow());
    }

    public String getText() {
        return view.getText().toString();
    }

    public void setText(CharSequence s) {
        view.setText(s);
    }

    public void setTextColor(UIColor color) {
        view.setTextColor(color.get());
    }

    public void setFont(UIFont font) {
        if (0 < font.size) view.setTextSize(font.size);
        if (0 < font.style) {
            view.setTypeface(font.get(), font.style);
        }
        else {
            view.setTypeface(font.get());
        }
    }

    public void setGravity(int gravity) {
        view.setGravity(gravity);
    }

    public void setNumberOfLines(int lines) {
        view.setLines(lines);
    }

    //TODO: setAutoSizeText
}
