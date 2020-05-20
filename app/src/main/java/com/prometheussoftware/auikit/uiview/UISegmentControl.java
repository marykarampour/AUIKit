package com.prometheussoftware.auikit.uiview;

import android.view.MotionEvent;
import android.view.View;

public class UISegmentControl extends UISegmentView implements View.OnTouchListener {

    public UISegmentControl() {
        super();
        setOnTouchListener(this);
        setUserInteractionEnabled(true);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.touchEnded(event);
        return false;
    }
}
