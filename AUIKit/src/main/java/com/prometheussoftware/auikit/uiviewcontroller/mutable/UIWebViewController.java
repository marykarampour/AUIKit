package com.prometheussoftware.auikit.uiviewcontroller.mutable;

import com.prometheussoftware.auikit.uiview.UIWebView;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class UIWebViewController extends UIViewController <UIWebView> {

    public UIWebViewController(String URL) {
        super();

        UIWebView view = new UIWebView();
        view.getView().loadUrl(URL);
        setView(view);
    }
}
