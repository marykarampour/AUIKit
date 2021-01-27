package com.prometheussoftware.auikit.uiview;

import android.webkit.WebView;

public class UIWebView extends UISingleView <WebView> {

    public UIWebView() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();
        view = new WebView(getActivity());
    }

    public void loadUrl(String url) {
        view.loadUrl(url);
    }
}
