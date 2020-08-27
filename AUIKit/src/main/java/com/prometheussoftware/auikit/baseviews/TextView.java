package com.prometheussoftware.auikit.baseviews;

import android.content.Context;
import android.view.KeyEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class TextView extends AppCompatEditText {

    private KeyDownDelegate keyDownDelegate;

    public TextView(Context context) {
        super(context);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setFocusable(false);
            setFocusableInTouchMode(true);
            setFocusable(true);
            if (keyDownDelegate != null) keyDownDelegate.keyDownPressed();
        }
        return super.onKeyPreIme(keyCode, event);
    }

    public void setKeyDownDelegate(KeyDownDelegate keyDownDelegate) {
        this.keyDownDelegate = keyDownDelegate;
    }

    public interface KeyDownDelegate {
        void keyDownPressed();
    }
}
