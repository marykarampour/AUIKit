package com.prometheussoftware.auikit.uiview;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.card.MaterialCardView;
import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.utility.ConstraintUtility;
import com.prometheussoftware.auikit.utility.ViewUtility;

public class UISingleLayerView <U extends UIView> extends UIView {

    /** This is the main content view of this class
     * add all subviews to this view */
    private U view;
    private int shadowSize;

    /** This is the layer to which this view is added
     * Use this to add corners and elevation
     * @apiNote Be careful, when dealing with low level
     * Android Views use getLayer() instead of this */
    private MaterialCardView layer;

    public UISingleLayerView() {
        super();
        baseInit();
    }

    private void baseInit() {
        init();
        setBackgroundColor(Color.TRANSPARENT);
        setContentBackgroundColor(UIColor.clear());
    }

    //endregion

    private void createLayer() {

        layer = new MaterialCardView(getActivity());

        LinearLayout.LayoutParams layerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layer.setLayoutParams(layerParams);
        layer.setElevation(0.0f);
        layer.setClickable(false);
    }

    public MaterialCardView getLayer() {
        return layer;
    }

    public void setView(U view) {
        this.view = view;
        view.setBackgroundColor(Color.TRANSPARENT);

        ViewUtility.addViewWithID(layer, this);
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        layer.addView(view);
    }

    protected void createView() {
        setView((U)new UIView());
    }

    public U getView() {
        return view;
    }

    //region UIView

    @Override public void initView() {
        super.initView();
        createLayer();
        createView();
    }

    @Override public void loadView() {
        super.loadView();
        ViewUtility.addViewWithID(layer, this);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setCardLayerBackgroundColor(UIColor.clear());
    }

    @Override public void constraintLayout() {
        super.constraintLayout();
        ConstraintUtility.constraintSidesForView(constraintSet, layer, new UIEdgeInsets(shadowSize(), shadowSize(), shadowSize(), shadowSize()));
        applyConstraints();
    }

    //endregion

    //region styling

    /** Background color of the view */
    public void setContentBackgroundColor(UIColor color) {
        view.setBackgroundColor(color);
    }

    /** Background color of the layer's card view, it is the card's background color */
    public void setCardLayerBackgroundColor(UIColor color) {
        layer.setCardBackgroundColor(color.get());
    }

    /** Background color of the layer view, it is the card's background color */
    public void setLayerBackgroundColor(UIColor color) {
        layer.setBackgroundColor(color.get());
    }

    public void setBorderColor(int color) {
        layer.setStrokeColor(color);
    }

    public void setBorderColor(UIColor color) {
        layer.setStrokeColor(color.get());
    }

    public void setBorderWidth(int width) {
        layer.setStrokeWidth(width);
    }

    public void setCornerRadius(float radius) {
        layer.setRadius(radius);
    }

    public int shadowSize() {
        return this.shadowSize;
    }

    //endregion

    @Override
    protected void onDraw(Canvas canvas) {
        boolean notSelfCanvas = getWidth() < canvas.getWidth() || getHeight() < canvas.getHeight();
        if (notSelfCanvas && 0 < layer.getRadius()) {
            Path clipPath = new Path();
            float radius = layer.getRadius();
            RectF rect = new RectF(0, 0, getWidth(), getHeight());

            clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW);
            canvas.clipPath(clipPath);
        }
        super.onDraw(canvas);
    }
}
