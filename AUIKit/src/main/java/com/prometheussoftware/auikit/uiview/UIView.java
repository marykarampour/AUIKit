package com.prometheussoftware.auikit.uiview;

import android.graphics.Color;
import android.util.Size;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.common.BaseActivity;
import com.prometheussoftware.auikit.common.BaseWindow;
import com.prometheussoftware.auikit.uiview.protocols.UIViewProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.LifeCycleDelegate;
import com.prometheussoftware.auikit.utility.ViewUtility;

import java.util.ArrayList;

public class UIView extends ConstraintLayout implements UIViewProtocol {

    public static final int CONSTRAINT_NO_PADDING = Integer.MAX_VALUE;

    /** A reference to the main window of the application */
    private static BaseWindow window;

    /** A reference to the current activity of the application.
     * If window is set, this is the application window */
    private static BaseActivity activity;

    private int opacity = VISIBLE;
    private boolean userInteractionEnabled;
    private boolean selfSetOpacity;
    protected boolean loaded;
    private UIColor viewBackgroundColor = UIColor.clear();

    /** The tintColor is inherited through the superview hierarchy */
    private UIColor tintColor = new UIColor();

    protected ConstraintSet constraintSet = new ConstraintSet();

    private LifeCycleDelegate.View lifeCycleDelegate;

    public UIView() {
        super(activity);
        ViewUtility.setViewID(this);
    }

    //region visibility

    /** Do NOT call this directly, the OS might reset to VISIBLE
     * Use setHidden or setGone */
    @Override public void setVisibility(int visibility) {
        if (selfSetOpacity) {
            selfSetOpacity = false;
            super.setVisibility(visibility);
            setOpacity(visibility);
        }
        else {
            super.setVisibility(opacity);
        }
        if (isHidden()) {
            setClickable(false);
        }
        else {
            setClickable(userInteractionEnabled);
        }
    }

    /** Use this to switch INVISIBLE and VISIBLE */
    public void setHidden(boolean hidden) {
        selfSetOpacity = true;
        setVisibility(hidden ? INVISIBLE : VISIBLE);
    }

    public void setHidden(boolean hidden, boolean animated) {
        if (animated) {
            if (hidden) {
                animate()
                        .alpha(0.0f)
                        .setDuration(1000)
                        .withEndAction(() -> {
                            setAlpha(1.0f);
                            setHidden(true);
                        })
                        .start();
            }
            else {
                setAlpha(0f);
                setHidden(false);
                animate()
                        .alpha(1.0f)
                        .setDuration(1000)
                        .start();
            }
        }
        else {
            setHidden(hidden);
        }
    }

    public boolean isHidden() {
        return opacity != VISIBLE;
    }

    public boolean isGone() {
        return opacity == GONE || opacity == INVISIBLE;
    }

    /** Use this to switch GONE and opacity */
    public void setGone(boolean gone) {
        selfSetOpacity = true;
        setVisibility(gone ? GONE : (opacity != GONE ? opacity : VISIBLE));
    }

    /** Use this instead of setVisibility so that you can
     * set visibility any time after init even before view
     * being added to super view.
     * Takes the same values as setVisibility:
     * INVISIBLE VISIBLE GONE */
    private void setOpacity(int opacity) {
        this.opacity = opacity;
    }

    public boolean isUserInteractionEnabled() {
        return userInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled) {
        this.userInteractionEnabled = userInteractionEnabled;
    }

    public void setUserInteractionEnabled(boolean userInteractionEnabled, boolean decendents) {
        this.userInteractionEnabled = userInteractionEnabled;
        if (decendents) {
            for (int i=0; i < getChildCount(); i++) {

                View child = getChildAt(i);
                if (child instanceof UIView) {
                    UIView vv = (UIView) child;
                    vv.setUserInteractionEnabled(userInteractionEnabled, decendents);
                }
                else {
                    child.setClickable(userInteractionEnabled);
                }
            }
        }
        else {
            setUserInteractionEnabled(userInteractionEnabled);
        }
    }

    //endregion

    //region load and init

    public void setLifeCycleDelegate(LifeCycleDelegate.View lifeCycleDelegate) {
        this.lifeCycleDelegate = lifeCycleDelegate;
    }

    //These are called multiple times in recycler view
    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (lifeCycleDelegate != null) lifeCycleDelegate.viewWillBeLoaded();
        viewDidLoad();
        if (lifeCycleDelegate != null) lifeCycleDelegate.viewIsLoaded();
    }

    @Override protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (lifeCycleDelegate != null) lifeCycleDelegate.viewWillBeUnloaded();
        unLoadView();
        if (lifeCycleDelegate != null) lifeCycleDelegate.viewIsUnloaded();
    }

    /** Add this to your custom constructors or call it after initializing using default constructor */
    public void init() {
        initView();
        loadView();
    }

    /** Initialize subviews */
    @Override
    public void initView() { }

    /** Add subviews */
    @Override
    public void loadView() {
        setVisibility(opacity);
    }

    /** Register targets */
    @Override public void viewDidLoad() {
        loaded = true;
        setVisibility(opacity);
        setBackgroundColor(viewBackgroundColor.get());
        constraintLayout();
    }

    /** Unregister targets */
    @Override public void unLoadView() {
        loaded = false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    /** Constraint subviews */
    @Override
    public void constraintLayout() { }

    public static void setWindow(BaseWindow baseWindow) {
        window = baseWindow;
        activity = window;
    }

    public static BaseWindow getWindow() {
        return window;
    }

    public static BaseActivity getActivity() {
        return activity;
    }

    public static void setActivity(BaseActivity activity) {
        UIView.activity = activity;
    }

    public BaseWindow window() {
        return window;
    }

    public BaseActivity activity() {
        return activity;
    }

    public static void runOnUiThread(Runnable run) {
        if (activity != null)
            activity.runOnUiThread(run);
    }

    /** Call in subclass to set size */
    public void setSize() {

        Size size = estimatedSize();
        setMinHeight(size.getHeight());
        setMinWidth(size.getWidth());
    }

    public Size estimatedSize() {
        return new Size(0, 0);
    }

    //endregion

    //region draw

    public void setTintColor(UIColor tintColor) {
        this.tintColor = tintColor;
        for (int i=0; i < getChildCount(); i++) {

            if (getChildAt(i) instanceof UIView) {
                UIView view = (UIView) getChildAt(i);
                view.setTintColor(tintColor);
            }
        }
    }

    public void setTintColor(int tintColor) {
        setTintColor(UIColor.build(tintColor));
    }

    public UIColor getTintColor() {
        return tintColor;
    }

    public void setBackgroundColor(UIColor color) {
        setViewBackgroundColor(color);
    }

    public UIColor getBackgroundColor() {
        return viewBackgroundColor;
    }

    public void setViewBackgroundColor(UIColor viewBackgroundColor) {
        this.viewBackgroundColor = viewBackgroundColor;
        setBackgroundColor(viewBackgroundColor.get());
    }

    @Override
    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (color != viewBackgroundColor.get()) {
            if (color == Color.TRANSPARENT) {
                viewBackgroundColor = UIColor.clear();
            }
            else {
                viewBackgroundColor = UIColor.build(color);
            }
        }
    }

    //endregion

    //region utility

    /** Only returns subvews of type UIView */
    public ArrayList<UIView> subUIViews() {

        ArrayList<UIView> views = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {

            View view = getChildAt(i);
            if (view instanceof UIView) {
                views.add((UIView) view);
            }
        }
        return views;
    }

    /** Returns all subviews */
    public ArrayList<View> subViews() {

        ArrayList<View> views = new ArrayList<>();

        for (int i = 0; i < getChildCount(); i++) {

            View view = getChildAt(i);
            views.add(view);
        }
        return views;
    }

    public void addSubView(UIView view) {
        if (view == null) return;
        ViewUtility.addViewWithID(view, this);
        view.setVisibility(view.opacity);
    }

    public void removeSubView(UIView view) {

        if (view == null || view.getParent() != this) return;
        constraintSet.clear(view.getId());
        removeView(view);
    }

    public <T extends UIView> void removeSubViews(ArrayList<T> views) {

        if (views == null || views.size() == 0) return;

        for (UIView view : subUIViews()) {
            if (views.contains(view)) {
                removeSubView(view);
            }
        }
    }

    public <T extends UIView> void addSubViews(ArrayList<T> views) {

        if (views == null || views.size() == 0) return;

        ArrayList<UIView> subviews = subUIViews();

        for (UIView view : views) {
            if (!subviews.contains(view)) {
                addSubView(view);
            }
        }
    }

    public void addHorizontalBar(boolean top, boolean bottom, int color, int height) {

        if (top) {
            UIView topBar = new UIView();
            topBar.setBackgroundColor(color);
            addSubView(topBar);

            constraintHeightForView(topBar, height);
            constraintForView(ConstraintSet.END, topBar);
            constraintForView(ConstraintSet.TOP, topBar);
            constraintForView(ConstraintSet.START, topBar);
        }

        if (bottom) {
            UIView bottomBar = new UIView();
            bottomBar.setBackgroundColor(color);
            addSubView(bottomBar);

            constraintHeightForView(bottomBar, height);
            constraintForView(ConstraintSet.END, bottomBar);
            constraintForView(ConstraintSet.BOTTOM, bottomBar);
            constraintForView(ConstraintSet.START, bottomBar);
        }

        applyConstraints();
    }

    public UIView addVerticalBar(int margin, boolean onLeft, int color, int width) {

        UIView bar = new UIView();
        bar.setBackgroundColor(color);
        addSubView(bar);

        constraintWidthForView(bar, width);
        constraintForView((onLeft ? ConstraintSet.START : ConstraintSet.END), bar, margin);
        constraintForView(ConstraintSet.TOP, bar);
        constraintForView(ConstraintSet.BOTTOM, bar);

        applyConstraints();
        return bar;
    }

    public void addVerticalBar(UIView bar, int margin, boolean onLeft, int color, int width) {

        bar.setBackgroundColor(color);
        constraintWidthForView(bar, width);
        constraintForView((onLeft ? ConstraintSet.START : ConstraintSet.END), bar, margin);
        constraintForView(ConstraintSet.TOP, bar);
        constraintForView(ConstraintSet.BOTTOM, bar);

        applyConstraints();
    }

    //endregion

    //region constraints

    public void applyConstraints() {
        constraintSet.applyTo(this);
    }

    public void clearConstraints(UIView view) {
        if (view == null || view.getParent() != this) return;
        constraintSet.clear(view.getId());
    }

    public void clearConstraints(ArrayList<? extends UIView> views) {
        for (UIView view : views) {
            clearConstraints(view);
        }
    }

    public void constraintSidesForView (UIView view) {
        constraintSidesForView(view, new UIEdgeInsets());
    }

    public void constraintSidesForView (UIView view, UIEdgeInsets insets) {
        if (view == null || view.getParent() != this) return;
        constraintSet.connect(view.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, insets.left);
        constraintSet.connect(view.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, insets.right);
        constraintSet.connect(view.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, insets.top);
        constraintSet.connect(view.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, insets.bottom);
    }

    public void constraintForView (int constraint, UIView view) {
        constraintForView(constraint, view, 0);
    }

    /** @param constraint use ConstraintSet.HORIZONTAL and ConstraintSet.VERTICAL to
     * center horizontal or vertical */
    public void constraintForView (int constraint, UIView view, int padding) {
        constraintForView(constraint, view, constraint, padding);
    }

    public void constraintForView (int constraint, UIView view, int constraintParent, int padding) {
        if (view == null || view.getParent() != this) return;
        switch (constraint) {
            case ConstraintSet.HORIZONTAL:
                constraintCenterXForView(view);
                break;
            case ConstraintSet.VERTICAL:
                constraintCenterYForView(view);
                break;
            default:
                constraintSet.connect(view.getId(), constraint, ConstraintSet.PARENT_ID, constraintParent, padding);
                break;
        }
    }

    public void constraintCenterXForView (UIView view) {
        if (view == null || view.getParent() != this) return;
        constraintSet.centerHorizontally(view.getId(), ConstraintSet.PARENT_ID);
    }

    public void constraintCenterXForView (UIView view, int padding) {
        if (view == null || view.getParent() != this) return;
        constraintSet.centerHorizontally(view.getId(), ConstraintSet.PARENT_ID, ConstraintSet.LEFT, padding, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, -padding, 0.5f);
    }

    public void constraintCenterXForViews (ArrayList <? extends UIView> views) {
        if (views == null || views.size() == 0) return;
        for (UIView view : views) {
            constraintCenterXForView(view);
        }
    }

    public void constraintCenterYForView (UIView view) {
        if (view == null || view.getParent() != this) return;
        constraintSet.centerVertically(view.getId(), ConstraintSet.PARENT_ID);
    }

    public void constraintCenterYForView (UIView view, int padding) {
        if (view == null || view.getParent() != this) return;
        constraintSet.centerVertically(view.getId(), ConstraintSet.PARENT_ID, ConstraintSet.TOP, padding, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, -padding, 0.5f);
    }

    public void constraintCenterYForViews (ArrayList <? extends UIView> views) {
        if (views == null || views.size() == 0) return;
        for (UIView view : views) {
            constraintCenterYForView(view);
        }
    }

    public void constraintHeightForView (UIView view, int height) {
        if (view == null || view.getParent() != this) return;
        constraintSet.constrainHeight(view.getId(), height);
    }

    public void constraintWidthForView (UIView view, int width) {
        if (view == null || view.getParent() != this) return;
        constraintSet.constrainWidth(view.getId(), width);
    }

    public void constraintDefaultHeightForView (UIView view, int height) {
        if (view == null || view.getParent() != this) return;
        constraintSet.constrainDefaultHeight(view.getId(), height);
    }

    public void constraintDefaultWidthForView (UIView view, int width) {
        if (view == null || view.getParent() != this) return;
        constraintSet.constrainDefaultWidth(view.getId(), width);
    }

    public void constraintSizeForView (UIView view, Size size) {
        if (view == null || view.getParent() != this) return;
        constraintSet.constrainHeight(view.getId(), size.getHeight());
        constraintSet.constrainWidth(view.getId(), size.getWidth());
    }

    public void constraintSizeForViews (ArrayList<? extends UIView> views, Size size) {
        if (views == null || views.size() == 0) return;
        for (UIView view : views) {
            constraintSizeForView(view, size);
        }
    }

    public void constraintHorizontally (ArrayList<? extends UIView> views, int style, int margin) {

        ArrayList<UIView> totalViews = new ArrayList<>();

        for (UIView view : views) {
            if (view != null && view.getParent() == this) {
                totalViews.add(view);
            }
        }

        int total = totalViews.size();
        if (total == 0) return;
        int[] IDs = new int[total];

        for (UIView view : totalViews) {

            int index = totalViews.indexOf(view);
            IDs[index] = view.getId();

            constraintForView(ConstraintSet.TOP, view, margin);
            constraintForView(ConstraintSet.BOTTOM, view, margin);
        }

        constraintSet.createHorizontalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT,
                IDs, null, style);
    }

    public void constraintHorizontally (ArrayList<? extends UIView> views, int style) {
        constraintHorizontally(views, style, 0);
    }

    public void constraintVertically (ArrayList<? extends UIView> views, int style) {
        constraintVertically(views, style, 0);
    }

    public void constraintVertically (ArrayList<? extends UIView> views, int style, int margin) {

        ArrayList<UIView> totalViews = new ArrayList<>();

        for (UIView view : views) {
            if (view != null && view.getParent() == this) {
                totalViews.add(view);
            }
        }

        int total = totalViews.size();
        if (total == 0) return;
        int[] IDs = new int[total];

        for (UIView view : totalViews) {

            int index = totalViews.indexOf(view);
            IDs[index] = view.getId();

            constraintForView(ConstraintSet.START, view, margin);
            constraintForView(ConstraintSet.END, view, margin);
        }

        constraintSet.createVerticalChain(
                ConstraintSet.PARENT_ID, ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM,
                IDs, null, style);
    }

    public void constraintVertically (ArrayList<? extends UIView> views, int interItemMargin, boolean equalHeights) {
        constraintVertically(views, interItemMargin, CONSTRAINT_NO_PADDING, CONSTRAINT_NO_PADDING, equalHeights, 0);
    }

    public void constraintHorizontally (ArrayList<? extends UIView> views, int interItemMargin, boolean equalHeights) {
        constraintHorizontally(views, interItemMargin, CONSTRAINT_NO_PADDING, CONSTRAINT_NO_PADDING, equalHeights, 0);
    }

    public void constraintVertically (ArrayList<? extends UIView> views, int interItemMargin, int horizontalMargin, int verticalMargin) {
        constraintVertically(views, interItemMargin, horizontalMargin, verticalMargin, false, ConstraintSet.TOP | ConstraintSet.BOTTOM);
    }

    public void constraintHorizontally (ArrayList<? extends UIView> views, int interItemMargin, int horizontalMargin, int verticalMargin) {
        constraintHorizontally(views, interItemMargin, horizontalMargin, verticalMargin, false, ConstraintSet.START | ConstraintSet.END);
    }

    public void constraintVertically (ArrayList<? extends UIView> views, int interItemMargin, int horizontalMargin, int verticalMargin, boolean equalHeights) {
        constraintVertically(views, interItemMargin, horizontalMargin, verticalMargin, equalHeights, ConstraintSet.TOP | ConstraintSet.BOTTOM);
    }

    public void constraintHorizontally (ArrayList<? extends UIView> views, int interItemMargin, int horizontalMargin, int verticalMargin, boolean equalWidths) {
        constraintHorizontally(views, interItemMargin, horizontalMargin, verticalMargin, equalWidths, ConstraintSet.START | ConstraintSet.END);
    }

    public void constraintVerticallyAllSides (ArrayList<? extends UIView> views, int interItemMargin, boolean equalHeights) {
        constraintVertically(views, interItemMargin, 0, 0, equalHeights, ConstraintSet.TOP | ConstraintSet.BOTTOM);
    }

    public void constraintHorizontallyAllSides (ArrayList<? extends UIView> views, int interItemMargin, boolean equalWidths) {
        constraintHorizontally(views, interItemMargin, 0, 0, equalWidths, ConstraintSet.START | ConstraintSet.END);
    }

    /** @param parentConstraints Specifies constraints on parent's edges, pass 0 for none, ConstraintSet.TOP or ConstraintSet.BOTTOM or both, other values are ignored
     @param horizontalMargin Use CONSTRAINT_NO_PADDING to not constraint horizontally to the parent, it will center horizontally.
     @param verticalMargin The margin between the first item and the top of parent, and the last item and the bottom of parent. */
    public void constraintVertically (ArrayList<? extends UIView> views, int interItemMargin, int horizontalMargin, int verticalMargin, boolean equalHeights, int parentConstraints) {

        ArrayList<UIView> totalViews = new ArrayList<>();

        for (UIView view : views) {
            if (view != null && view.getParent() == this) {
                totalViews.add(view);
            }
        }

        int total = totalViews.size();
        if (total == 0) return;

        float percentHeight = (100f/totalViews.size()) / 100;
        UIView last = totalViews.get(0);

        if ((parentConstraints & ConstraintSet.TOP) == ConstraintSet.TOP) {
            constraintForView(ConstraintSet.TOP, last, verticalMargin);
        }

        for (UIView view : totalViews) {

            int index = totalViews.indexOf(view);

            if (horizontalMargin != CONSTRAINT_NO_PADDING) {
                constraintForView(ConstraintSet.START, view, horizontalMargin);
                constraintForView(ConstraintSet.END, view, horizontalMargin);
            }
            else {
                constraintCenterXForView(view);
            }
            if (((parentConstraints & ConstraintSet.BOTTOM) == ConstraintSet.BOTTOM) && index == totalViews.size() - 1) {
                constraintForView(ConstraintSet.BOTTOM, view, verticalMargin);
            }
            if (0 < index) {
                constraintViews(view, ConstraintSet.TOP, last, ConstraintSet.BOTTOM, interItemMargin);
            }
            if (equalHeights) {
                constraintSet.constrainPercentHeight(view.getId(), percentHeight);
            }
            last = view;
        }
    }

    /** @param parentConstraints Specifies constraints on parent's edges, pass 0 for none, ConstraintSet.START or ConstraintSet.END or both, other values are ignored
     @param verticalMargin Use CONSTRAINT_NO_PADDING to not constraint vertically to the parent, it will center vertically.
     @param horizontalMargin The margin between the first item and the start of parent, and the last item and the end of parent. */
    public void constraintHorizontally (ArrayList<? extends UIView> views, int interItemMargin, int horizontalMargin, int verticalMargin, boolean equalWidths, int parentConstraints) {

        ArrayList<UIView> totalViews = new ArrayList<>();

        for (UIView view : views) {
            if (view != null && view.getParent() == this) {
                totalViews.add(view);
            }
        }

        int total = totalViews.size();
        if (total == 0) return;

        float percentWidth = (100f/totalViews.size()) / 100;
        UIView last = totalViews.get(0);

        if ((parentConstraints & ConstraintSet.START) == ConstraintSet.START) {
            constraintForView(ConstraintSet.START, last, horizontalMargin);
        }

        for (UIView view : totalViews) {

            int index = totalViews.indexOf(view);

            if (verticalMargin != CONSTRAINT_NO_PADDING) {
                constraintForView(ConstraintSet.TOP, view, verticalMargin);
                constraintForView(ConstraintSet.BOTTOM, view, verticalMargin);
            }
            else {
                constraintCenterYForView(view);
            }
            if (((parentConstraints & ConstraintSet.END) == ConstraintSet.END) && index == totalViews.size() - 1) {
                constraintForView(ConstraintSet.END, view, horizontalMargin);
            }
            if (0 < index) {
                constraintViews(view, ConstraintSet.START, last, ConstraintSet.END, interItemMargin);
            }
            if (equalWidths) {
                constraintSet.constrainPercentWidth(view.getId(), percentWidth);
            }
            last = view;
        }
    }

    public void constraintSameForViews (int constraint, UIView view1, UIView view2, int padding) {
        if (view1 == null || view1.getParent() != this || view2 == null || view2.getParent() != this) return;
        constraintSet.connect(view1.getId(), constraint, view2.getId(), constraint, padding);
    }

    public void constraintSameForViews (int constraint, UIView view1, UIView view2) {
        constraintSameForViews(constraint, view1, view2, 0);
    }

    public void constraintSameForViews (UIView view1, UIView view2, UIEdgeInsets insets) {
        if (view1 == null || view1.getParent() != this || view2 == null || view2.getParent() != this) return;
        constraintSameForViews(ConstraintSet.TOP, view1, view2, insets.top);
        constraintSameForViews(ConstraintSet.END, view1, view2, insets.right);
        constraintSameForViews(ConstraintSet.START, view1, view2, insets.left);
        constraintSameForViews(ConstraintSet.BOTTOM, view1, view2, insets.bottom);
    }

    public void constraintSameForViews (UIView view1, UIView view2) {
        constraintSameForViews(view1, view2, new UIEdgeInsets());
    }

    public void constraintViews (UIView view1, int constraint1, UIView view2, int constraint2, int padding) {
        if (view1 == null || view1.getParent() != this || view2 == null || view2.getParent() != this) return;
        constraintSet.connect(view1.getId(), constraint1, view2.getId(), constraint2, padding);
    }

    public void constraintViews (UIView view1, int constraint1, UIView view2, int constraint2) {
        constraintViews(view1, constraint1, view2, constraint2, 0);
    }

    public void removeAllConstraints () {
        for (int i = 0; i < getChildCount(); i++) {
            View vi = getChildAt(i);
            constraintSet.clear(vi.getId());
        }
        constraintSet.applyTo(this);
    }

    public void removeConstraints (UIView view) {
        if (view == null || view.getParent() != this) return;
        constraintSet.clear(view.getId());
        constraintSet.applyTo(this);
    }

    public ConstraintSet getConstraintSet() {
        return constraintSet;
    }

    //endregion
}
