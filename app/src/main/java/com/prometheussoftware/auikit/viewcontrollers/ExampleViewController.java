package com.prometheussoftware.auikit.viewcontrollers;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class ExampleViewController extends UIViewController {

    private UIColor color;

    public ExampleViewController(UIColor color) {
        super();
        init();
        this.color = color;
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("ExampleVC");
        view().setBackgroundColor(color);
    }
}
