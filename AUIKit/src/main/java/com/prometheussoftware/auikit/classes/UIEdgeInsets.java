package com.prometheussoftware.auikit.classes;

import com.prometheussoftware.auikit.model.BaseModel;

public class UIEdgeInsets extends BaseModel {

    public int top;
    public int left;
    public int bottom;
    public int right;

    public UIEdgeInsets(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public UIEdgeInsets(int inset) {
        this.top = inset;
        this.left = inset;
        this.bottom = inset;
        this.right = inset;
    }

    public UIEdgeInsets() {
        this.top = 0;
        this.left = 0;
        this.bottom = 0;
        this.right = 0;
    }

    public boolean isZero() {
        return top == 0 && bottom == 0 && left == 0 && right == 0;
    }
}
