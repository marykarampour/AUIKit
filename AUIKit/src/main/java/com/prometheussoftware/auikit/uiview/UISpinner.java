package com.prometheussoftware.auikit.uiview;

import android.util.Size;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.utility.ConstraintUtility;
import com.prometheussoftware.auikit.utility.ViewUtility;

public class UISpinner extends UIView {

    private ProgressBar hud;
    private Size hudSize = App.constants().Spinner_Hud_Size();

    public UISpinner() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();
        hud = new ProgressBar(getActivity());
        setHudColor(App.theme().Spinner_Hud_Color());
    }

    @Override
    public void loadView() {
        super.loadView();
        ViewUtility.addViewWithID(hud, this);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        constraintSet.centerHorizontally(hud.getId(), ConstraintSet.PARENT_ID);
        constraintSet.centerVertically(hud.getId(), ConstraintSet.PARENT_ID);
        ConstraintUtility.constraintSizeForView(constraintSet, hud, hudSize);
        applyConstraints();
    }

    public void show () {
        if (hud == null) return;
        runOnUiThread(() -> {
            setGone(false);
            bringToFront();
            hud.animate();
        });
    }

    public void hide () {
        if (hud == null) return;
        runOnUiThread(() -> {
            setGone(true);
            hud.clearAnimation();
        });
    }

    public void setHudColor(UIColor color) {
        hud.getIndeterminateDrawable().setColorFilter(color.get(), android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    public void setHudSize(int size) {
        hudSize = Dimensions.size(size);
        ConstraintUtility.constraintSizeForView(constraintSet, hud, hudSize);
        applyConstraints();
    }
}
