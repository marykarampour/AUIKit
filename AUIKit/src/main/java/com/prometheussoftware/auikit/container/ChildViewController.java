package com.prometheussoftware.auikit.container;

import android.graphics.Color;

import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class ChildViewController <V extends UIView, O, C extends UIViewController & HeaderFooterVCProtocol.Container> extends UIViewController <V> implements HeaderFooterVCProtocol.Child<O, C> {

    protected C headerDelegate;
    protected O object;

    @Override public void viewDidLoad() {
        super.viewDidLoad();
        view().setBackgroundColor(Color.WHITE);
    }

    @Override public C headerDelegate() {
        return headerDelegate;
    }

    @Override
    public void setHeaderDelegate(C headerDelegate) {
        this.headerDelegate = headerDelegate;
    }

    @Override public O object() {
        return object;
    }

    @Override public void setObject(O object) {
        this.object = object;
    }
}
