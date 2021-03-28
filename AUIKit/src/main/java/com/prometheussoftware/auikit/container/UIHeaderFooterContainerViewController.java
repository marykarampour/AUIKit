package com.prometheussoftware.auikit.container;

import android.animation.ObjectAnimator;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.animation.AccelerateInterpolator;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.callback.CompletionCallback;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public abstract class UIHeaderFooterContainerViewController <H extends UIView, F extends UIView, C extends UIView, VC extends UIViewController & HeaderFooterVCProtocol.Child> extends UIViewController implements HeaderFooterVCProtocol.Container {

    private UIView backView;
    protected C contentView;
    protected H headerView;
    protected F footerView;
    protected VC childViewController;
    protected Object object;

    public UIHeaderFooterContainerViewController() {
        super();
    }

    public UIHeaderFooterContainerViewController(Object object) {
        super();
        this.object = object;
    }

    @Override public UIViewController init() {
        createChildVCWithChildObject(object);
        if (childViewController != null) childViewController.setHeaderDelegate(this);
        return super.init();
    }

    @Override public void viewDidLoad() {
        super.viewDidLoad();

        createHeaderView();
        createFooterView();
        setContentView();

        backView = new UIView();

        view().addSubView(backView);
        backView.addSubView(headerView);
        backView.addSubView(contentView);
        backView.addSubView(footerView);
        constraintViews();
    }

    @Override public void createHeaderView() {
        headerView = (H)new UIView();
    }

    @Override public void createFooterView() {
        footerView = (F)new UIView();
    }

    @Override public int maxFooterHeight() {
        return Dimensions.Int_256();
    }

    @Override public int maxHeaderHeight() {
        return Dimensions.Int_256();
    }

    @Override public void createChildVCWithChildObject(Object object) {
        createChildVC();
    }

    @Override public void setContentView() {
        if (childViewController != null) {
            addChildViewController(childViewController);
            contentView = (C) childViewController.view();
        }
        else {
            contentView = (C) new UIView();
        }
    }

    public void setObject(Object object) {
        this.object = object;
        if (childViewController != null) childViewController.setObject(object);
    }

    protected void constraintViews() {

        view().constraintSidesForView(backView);
        view().applyConstraints();

        backView.constraintHeightForView(headerView, headerHeight());
        backView.constraintHeightForView(footerView, footerHeight());

        backView.constraintForView(ConstraintSet.TOP, headerView);
        backView.constraintForView(ConstraintSet.START, headerView);
        backView.constraintForView(ConstraintSet.END, headerView);

        backView.constraintForView(ConstraintSet.START, contentView);
        backView.constraintForView(ConstraintSet.END, contentView);

        backView.constraintForView(ConstraintSet.BOTTOM, footerView);
        backView.constraintForView(ConstraintSet.START, footerView);
        backView.constraintForView(ConstraintSet.END, footerView);

        backView.constraintViews(contentView, ConstraintSet.TOP, headerView, ConstraintSet.BOTTOM);
        backView.constraintViews(contentView, ConstraintSet.BOTTOM, footerView, ConstraintSet.TOP);

        backView.applyConstraints();
    }

    public void setFooterExpanded(boolean expanded, boolean animated, CompletionCallback completion) {
        setExpanded(expanded, footerView, footerHeight(), maxFooterHeight(), animated, completion);
    }

    public void setHeaderExpanded(boolean expanded, boolean animated, CompletionCallback completion) {
        setExpanded(expanded, headerView, headerHeight(), maxHeaderHeight(), animated, completion);
    }

    private void setExpanded(boolean expanded, UIView view, int height, int maxHeight, boolean animated, CompletionCallback completion) {

        float alpha = expanded ? 1.0f : 0.0f;

        if (animated) {
            animateViewExpandColor(expanded, view, alpha);
            Transition transition = new AutoTransition();
            transition.setDuration(animationDuration());
            transition.setInterpolator(new AccelerateInterpolator());
            TransitionManager.beginDelayedTransition(view, transition);
        }
        else {
            view.setAlpha(alpha);
        }

        backView.constraintHeightForView(view, expanded ? maxHeight : height);
        backView.applyConstraints();

        if (completion != null) {
            if (animated) {
                new Handler().postDelayed( () -> completion.done(), animationDuration() );
            }
            else {
                completion.done();
            }
        }
    }

    private void animateViewExpandColor(boolean expanded, UIView view, float alpha) {

        ObjectAnimator.ofFloat(view, "alpha", alpha)
                .setDuration(animationDuration())
                .start();
    }

    public H getHeaderView() {
        return headerView;
    }

    public F getFooterView() {
        return footerView;
    }

    public C getContentView() {
        return contentView;
    }
}
