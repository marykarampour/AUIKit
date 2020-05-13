package com.prometheussoftware.auikit.uiview;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;

import java.util.HashMap;

public class UIControl extends UIView implements View.OnTouchListener {

    private HashMap<Object, TargetDelegate> targets = new HashMap<>();
    public IndexPath IndexPath;
    private MotionEvent lastTouch;
    private GestureDetector gestureDetector;

    static {
        Identifier.Register(UIControl.class);
    }

    public UIControl() {
        super();
        setUserInteractionEnabled(true);
        createEventListeners();
    }

    public MotionEvent getLastTouch() {
        return lastTouch;
    }

    @Override public void constraintLayout() {
        super.constraintLayout();
        disableChildViews();
    }

    protected void disableChildViews() {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if ((child instanceof UIView)) {
                disableChildView((UIView) child);
            }
            else {
                disableChildView(child);
            }
        }
    }

    private void disableChildView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int i = 0; i < group.getChildCount(); i++) {
                group.getChildAt(i).setClickable(false);
            }
        }
        else {
            view.setClickable(false);
        }
    }

    private void disableChildView(UIView view) {
        view.setUserInteractionEnabled(false);
        for (int i = 0; i < view.getChildCount(); i++) {

            View child = view.getChildAt(i);
            if (child instanceof UIView) {
                UIView vv = (UIView) child;
                vv.setUserInteractionEnabled(false, true);
            } else {
                child.setClickable(false);
            }
        }
    }

    //region touches and targets

    private void createEventListeners() {

        gestureDetector = new GestureDetector(getWindow(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                lastTouch = e;
                return true;
            }
        });

        setOnTouchListener(this);
    }

    /** This is overwritten to handle multiple targets.
     * It only allows setOnTouchListener on this, any other
     * object is added as a target if it implements TargetDelegate */
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        if (l == this) {
            super.setOnTouchListener(l);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            for (Object ID : targets.keySet()) {

                TargetDelegate target = targets.get(ID);
                if (target instanceof TargetDelegate) {
                    target.controlPressed(this);
                }
            }
            return false;
        }
        return true;
    }

    //endregion

    public void setTarget(TargetDelegate target) {
        targets.put(this, target);
    }

    public void addTarget(Object ID, TargetDelegate target) {
        if (targets.get(ID) == null) {
            targets.put(ID, target);
        }
    }

    public void removeTarget(TargetDelegate target) {
        targets.remove(target);
    }

    public HashMap<Object, TargetDelegate> getTargets() {
        return targets;
    }

    @FunctionalInterface
    public interface TargetDelegate {
        void controlPressed(UIControl sender);
    }


//     TODO: need to remove targets, but when to add them? same in UIResponder
//     TODO: make this work with lambda
//      SerializableLambda is the way to derive info from lambda and guarantee uniqueness of target in the targets
//      but that is not supported in Android
//    @FunctionalInterface
//    public interface TargetDelegate <T> extends Consumer<T>, Serializable {
//        default void controlPressed(UIControl sender, SerializableConsumer<Object> function) {
//            function.accept(sender);
//        }
//    }
//
//    interface SerializableConsumer<T> extends Consumer<T>, Serializable {
//        void controlPressed(UIControl sender);
//    }
//     TODO: layers and controls
}
