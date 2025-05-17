package com.prometheussoftware.auikit.uiview;

import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.classes.UITargetManager;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;

import java.util.ArrayList;
import java.util.HashMap;

public class UIControl extends UIView implements View.OnTouchListener, UITargetManager.Delegate, UITargetDelegate.Sender {

    private UITargetManager targetManager = new UITargetManager(this);
    public IndexPath IndexPath;

    private MotionEvent lastTouch;
    private long lastTouchUpTimeStamp;
    private static final long DOUBLE_TAP_INTERVAL = 1000;

    private boolean isLongTouch;
    private GestureDetector gestureDetector;

    /** Default is false. this gets set/cleared automatically when touch
     * enters/exits during tracking and cleared on up */
    private boolean highlighted;

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

        gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                lastTouch = e;
                isLongTouch = false;
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                lastTouch = e;
                isLongTouch = false;
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
                lastTouch = e;
                isLongTouch = true;
            }
        });

        setOnTouchListener(this);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        targetManager.handleOnKeyUp(keyCode, event);
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        targetManager.handleOnKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
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

        if (!isUserInteractionEnabled()) return false;
        if (!isControlAction(event)) return true;

        if (gestureDetector.onTouchEvent(event) || isLongTouch) {
            targetManager.handleOnTouch(v, event);
            return false;
        }
        return true;
    }

    private boolean isControlAction (MotionEvent event) {
        return  (event.getAction() == MotionEvent.ACTION_UP) ||
                (event.getAction() == MotionEvent.ACTION_DOWN);
    }

    //endregion

    //region targets

    public void setTarget(UITargetDelegate.TouchUp target) {
        targetManager.setTarget(target);
    }

    public void setKeyTarget(UITargetDelegate.KeyUp target) {
        targetManager.setKeyTarget(target);
    }

    /** @apiNote It does not distinguish between different instances
     * of the same lambda, user must be careful not to add the
     * same target multiple times, that would result in the
     * lambda being executed multiple times. Set
     *  multiTargetEnabled = false to guarantee unique action on each
     *  touch event */
    public void addTarget(Object ID, UITargetDelegate target) {
        targetManager.addTarget(ID, target);
    }

    public void addTouchDownTarget(Object ID, UITargetDelegate.TouchDown target) {
        addTarget(ID, target);
    }

    public void addTouchUpTarget(Object ID, UITargetDelegate.TouchUp target) {
        addTarget(ID, target);
    }

    public void addKeyDownTarget(Object ID, UITargetDelegate.KeyDown target) {
        addTarget(ID, target);
    }

    public void addKeyUpTarget(Object ID, UITargetDelegate.KeyUp target) {
        addTarget(ID, target);
    }

    public void removeTarget(UITargetDelegate target) {
        targetManager.removeTarget(target);
    }

    public HashMap<Object, ArrayList<UITargetDelegate>> getTargets() {
        return targetManager.getTargets();
    }

    public boolean isMultiTargetEnabled() {
        return targetManager.isMultiTargetEnabled();
    }

    public void setMultiTargetEnabled(boolean multiTargetEnabled) {
        targetManager.setMultiTargetEnabled(multiTargetEnabled);
    }

    //endregion


    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public boolean targetWillSetTouch(UITargetDelegate target, boolean down) {
        setHighlighted(down);

        if (!down) {
            long currentTouchUpTimeStamp = SystemClock.elapsedRealtime();
            if (currentTouchUpTimeStamp - lastTouchUpTimeStamp < DOUBLE_TAP_INTERVAL)
                return false;
            lastTouchUpTimeStamp = currentTouchUpTimeStamp;
        }
        return true;
    }

    @Override
    public boolean targetWillSetKey(UITargetDelegate target, boolean down) {
        setHighlighted(down);
        return true;
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
