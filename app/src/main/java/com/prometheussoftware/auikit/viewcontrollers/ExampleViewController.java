package com.prometheussoftware.auikit.viewcontrollers;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class ExampleViewController extends UIViewController {

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("ExampleVC");
        view().setBackgroundColor(UIColor.blue(1.0f));
    }
}
