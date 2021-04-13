package com.prometheussoftware.auikit.uiview;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.animation.PathInterpolator;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.callback.CompletionCallback;
import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.uiviewcontroller.Navigation;
import com.prometheussoftware.auikit.utility.ImageUtility;

public class UITransitioningContainerView extends UIView {

    /** For views where the layout needs time to load, e.g. recyclerview
     * this value is used to cause a delay that allows enough time for
     * the view to load. */
    private static final int LAYOUT_LOAD_WAIT_DURATION = 200;

    /** The content view which is visible */
    private UIView currentContentView;

    private UIView fadeOverlay;

    /** Used to simulate transition animations */
    private UIImageView transitioningView;

    private Handler animationHandler = new Handler();

    protected PathInterpolator interpolator = new PathInterpolator(0.720f, 0.105f, 0.650f, 0.725f);

    public int animationDuration = 200;
    public int fadeDuration = 1000;

    public UITransitioningContainerView() {
        super();

        transitioningView = new UIImageView();
        transitioningView.setBackgroundColor(Color.WHITE);
        transitioningView.setScaleType(ImageView.ScaleType.FIT_XY);

        fadeOverlay = new UIView();
        addSubView(transitioningView);
        addSubView(fadeOverlay);

        constraintSidesForView(transitioningView);
        applyConstraints();
    }

    private void addCurrentContentView() {
        addSubView(currentContentView);
        constraintSidesForView(currentContentView);
        applyConstraints();
    }

    public void setCurrentContentView(UIView currentContentView, CompletionCallback callback) {
        updateTransitioningImage(() -> {
            removeView(this.currentContentView);
            if (currentContentView != null) {
                this.currentContentView = currentContentView;
            }
            callback.done();
        });
    }

    private void updateTransitioningImage(CompletionCallback callback) {
        if (this.currentContentView != null) {
            ImageUtility.imageFromView(this.currentContentView, new Handler(), new ImageUtility.BitmapCopy() {
                @Override
                public void finishedWithResult(Bitmap bitmap) {
                    transitioningView.getView().getView().setImageBitmap(bitmap);
                    callback.done();
                }

                @Override
                public void failed() {
                    callback.done();
                }
            });
        }
        else {
            callback.done();
        }
    }

    //region animation

    private void didFinishAnimation(CompletionCallback completion) {

        new Handler().postDelayed( () -> {
            transitioningView.setImage(null);
            fadeOverlay.setBackgroundColor(Color.TRANSPARENT);
            if (completion != null) completion.done();
        }, transitionAnimationDuration() );
    }

    public void applyTransitionAnimation (boolean isDismiss, Navigation.TRANSITION_ANIMATION type, CompletionCallback completion) {
        switch (type) {
            case NONE:      addCurrentContentView(); if (completion != null) completion.done(); break;
            case FADE:      applyFadeAnimation(isDismiss, completion); break;
            case UPDOWN:    applyVerticalAnimation(isDismiss, true, completion); break;
            case DOWNUP:    applyVerticalAnimation(isDismiss, false, completion); break;
            case RIGHTLEFT: applyHorizontalAnimation(isDismiss, false, completion); break;
            case LEFTRIGHT: applyHorizontalAnimation(isDismiss, true, completion); break;
        }
    }

    private void applyTransitionColorFade(boolean isDismiss) {

        int color = UIColor.black(0.7f).get();
        int start = !isDismiss ? Color.TRANSPARENT : color;
        int end = isDismiss ? Color.TRANSPARENT : color;
        ObjectAnimator.ofObject(fadeOverlay, "backgroundColor", new ArgbEvaluator(), start, end)
                .setDuration(fadeDuration)
                .start();
    }

    private void applyFadeAnimation (boolean isDismiss, CompletionCallback completion) {

        addCurrentContentView();

        UIView topView = isDismiss ? transitioningView : currentContentView;
        UIView bottomView = !isDismiss ? transitioningView : currentContentView;
        topView.bringToFront();
        topView.setAlpha(0);
        bottomView.setHidden(false);

        bottomView.animate()
                .alpha(isDismiss ? 1 : 0)
                .setDuration(transitionAnimationDuration())
                .setInterpolator(interpolator)
                .start();
        topView.animate()
                .alpha(!isDismiss ? 1 : 0)
                .setDuration(transitionAnimationDuration())
                .setInterpolator(interpolator)
                .start();
        didFinishAnimation(completion);
    }

    private void applyHorizontalAnimation(boolean isDismiss, boolean leftRight, CompletionCallback completion) {
        if (isDismiss) {
            applyHorizontalDismissAnimation(leftRight, completion);
        }
        else {
            applyHorizontalPresentAnimation(leftRight, completion);
        }
    }

    private void applyVerticalAnimation(boolean isDismiss, boolean topBottom, CompletionCallback completion) {
        if (isDismiss) {
            applyVerticalDismissAnimation(topBottom, completion);
        }
        else {
            applyVerticalPresentAnimation(topBottom, completion);
        }
    }

    private void applyHorizontalPresentAnimation(boolean leftRight, CompletionCallback completion) {

        int width = Constants.Screen_Size().getWidth();
        int constraintView = leftRight ? ConstraintSet.END : ConstraintSet.START;
        int constraintParent = !leftRight ? ConstraintSet.END : ConstraintSet.START;

        addSubView(currentContentView);
        clearConstraints(transitioningView);
        constraintSidesForView(transitioningView);

        constraintWidthForView(currentContentView, width);
        applySideConstraints(currentContentView, ConstraintSet.TOP, ConstraintSet.BOTTOM, constraintView, constraintParent);
        constraintSameForViews(fadeOverlay, transitioningView);

        applyTransitionColorFade(false);

        animationHandler.removeCallbacksAndMessages(null);
        animationHandler.postDelayed( () -> {
            prepareAnimation();

            constraintWidthForView(currentContentView, 0);
            constraintForView(ConstraintSet.START, currentContentView, 0);
            constraintForView(ConstraintSet.END, currentContentView, 0);
            applyConstraints();

            didFinishAnimation(completion);
        }, LAYOUT_LOAD_WAIT_DURATION);
    }

    private void applyHorizontalDismissAnimation(boolean leftRight, CompletionCallback completion) {

        int width = Constants.Screen_Size().getWidth();
        int constraintView = !leftRight ? ConstraintSet.END : ConstraintSet.START;
        int constraintParent = leftRight ? ConstraintSet.END : ConstraintSet.START;

        applyInitialConstraintsForVerticalDismiss();
        constraintSameForViews(fadeOverlay, transitioningView);

        applyTransitionColorFade(false);

        animationHandler.removeCallbacksAndMessages(null);
        animationHandler.postDelayed( () -> {
            clearConstraints(transitioningView);
            prepareAnimation();

            constraintWidthForView(transitioningView, width);
            applySideConstraints(transitioningView, ConstraintSet.TOP, ConstraintSet.BOTTOM, constraintView, constraintParent);

            didFinishAnimation(completion);
        }, LAYOUT_LOAD_WAIT_DURATION);
    }

    private void applyVerticalPresentAnimation( boolean topBottom, CompletionCallback completion) {

        int height = Constants.Screen_Size().getHeight();
        int constraintView = topBottom ? ConstraintSet.BOTTOM : ConstraintSet.TOP;
        int constraintParent = !topBottom ? ConstraintSet.BOTTOM : ConstraintSet.TOP;

        addSubView(currentContentView);
        clearConstraints(transitioningView);
        constraintSidesForView(transitioningView);

        constraintHeightForView(currentContentView, height);
        applySideConstraints(currentContentView, ConstraintSet.START, ConstraintSet.END, constraintView, constraintParent);
        constraintSameForViews(fadeOverlay, transitioningView);

        applyTransitionColorFade(false);

        animationHandler.removeCallbacksAndMessages(null);
        animationHandler.postDelayed( () -> {
            prepareAnimation();

            constraintHeightForView(currentContentView, 0);
            constraintForView(ConstraintSet.TOP, currentContentView, 0);
            constraintForView(ConstraintSet.BOTTOM, currentContentView, 0);
            applyConstraints();

            didFinishAnimation(completion);
        }, LAYOUT_LOAD_WAIT_DURATION);
    }

    private void applyVerticalDismissAnimation(boolean topBottom, CompletionCallback completion) {

        int height = Constants.Screen_Size().getHeight();
        int constraintView = !topBottom ? ConstraintSet.BOTTOM : ConstraintSet.TOP;
        int constraintParent = topBottom ? ConstraintSet.BOTTOM : ConstraintSet.TOP;

        applyInitialConstraintsForVerticalDismiss();
        constraintSameForViews(fadeOverlay, transitioningView);

        applyTransitionColorFade(false);

        animationHandler.removeCallbacksAndMessages(null);
        animationHandler.postDelayed( () -> {

            clearConstraints(transitioningView);
            prepareAnimation();

            constraintHeightForView(transitioningView, height);
            applySideConstraints(transitioningView, ConstraintSet.START, ConstraintSet.END, constraintView, constraintParent);

            didFinishAnimation(completion);
        }, LAYOUT_LOAD_WAIT_DURATION);
    }

    private void applyInitialConstraintsForVerticalDismiss() {
        addSubView(currentContentView);
        transitioningView.bringToFront();
        fadeOverlay.bringToFront();

        clearConstraints(transitioningView);
        constraintSidesForView(transitioningView);
        constraintSidesForView(currentContentView);
        applyConstraints();
    }

    private void applySideConstraints(UIView childView, int sideStart, int sideEnd, int constraintView, int constraintParent) {
        constraintForView(sideStart, childView, 0);
        constraintForView(sideEnd, childView, 0);
        constraintForView(constraintView, childView, constraintParent, 0);
        applyConstraints();
    }

    private void prepareAnimation () {
        Transition transition = new AutoTransition();
        transition.setDuration(transitionAnimationDuration());
        transition.setInterpolator(interpolator);
        TransitionManager.beginDelayedTransition(this, transition);
    }

    protected int transitionAnimationDuration() {
        return animationDuration;
    }

    //endregion
}
