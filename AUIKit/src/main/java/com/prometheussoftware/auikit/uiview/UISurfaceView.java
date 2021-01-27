package com.prometheussoftware.auikit.uiview;

import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class UISurfaceView extends UISingleView <SurfaceView> {

    public UISurfaceView() {
        super();
        init();
    }

    @Override
    public void initView() {
        view = new SurfaceView(getActivity());
        setHidden(false);
        setBackgroundColor(Color.TRANSPARENT);
    }

    public SurfaceHolder getHolder() {
        return view.getHolder();
    }
}
