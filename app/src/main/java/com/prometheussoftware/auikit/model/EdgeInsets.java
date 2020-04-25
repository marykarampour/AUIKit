package com.prometheussoftware.auikit.model;

public class EdgeInsets {

    public int top;
    public int left;
    public int bottom;
    public int right;

    public EdgeInsets (int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public EdgeInsets () {
        this.top = 0;
        this.left = 0;
        this.bottom = 0;
        this.right = 0;
    }

    public boolean isZero() {
        return top == 0 && bottom == 0 && left == 0 && right == 0;
    }
}
