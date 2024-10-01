package com.prometheussoftware.auikit.uiview;

import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;
import java.util.HashMap;

public class UIControl extends UIView implements View.OnTouchListener {

    private HashMap<Object, ArrayList<TargetDelegate>> targets = new HashMap<>();
    public IndexPath IndexPath;

    /** Default is true. IF true it allows a single key in targets map
     * have multiple targets, that is addTarget will add to this array,
     * and all of the corresponding actions will be triggered on a touch
     * event.
     * This allows:
     * true  -> t1a1 t2a1 - t1a1 t1a1 - t1a1 t1a2 - t1a1 t2a2
     * false -> t1a1 t2a1 -           -           - t1a1 t2a2
     *
     * @apiNote Set this to false in case of table views since they tend
     * to relaod and recycle views multiple times, resulting in targets
     * being re-added inadvertently. */
    private boolean multiTargetEnabled = true;

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
            for (Object ID : targets.keySet()) {
                for (TargetDelegate target : targets.get(ID)) {
                    if (target instanceof TargetDelegate) {
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            setHighlighted(false);

                            long currentTouchUpTimeStamp = SystemClock.elapsedRealtime();
                            if (currentTouchUpTimeStamp - lastTouchUpTimeStamp < DOUBLE_TAP_INTERVAL) return false;

                            lastTouchUpTimeStamp = currentTouchUpTimeStamp;
                            if (target instanceof TouchUp) {
                                TouchUp touchUp = (TouchUp)target;
                                touchUp.controlReleased(this);
                            }
                        }
                        else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            setHighlighted(true);
                            if (target instanceof TouchDown) {
                                TouchDown touchDown = (TouchDown)target;
                                touchDown.controlPressed(this);
                            }
                        }
                    }
                }
            }
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

    public void setTarget(TouchUp target) {

        ArrayList<TargetDelegate> delegates = new ArrayList<>();
        delegates.add(target);
        targets.put(this, delegates);
    }

    /** @apiNote It does not distinguish between different instances
     * of the same lambda, user must be careful not to add the
     * same target multiple times, that would result in the
     * lambda being executed multiple times. Set
     *  multiTargetEnabled = false to guarantee unique action on each
     *  touch event */
    public void addTarget(Object ID, TargetDelegate target) {

        ArrayList<TargetDelegate> delegates = targets.get(ID);
        if (delegates == null) {
            delegates = new ArrayList<>();
        }

        if (0 == delegates.size() || multiTargetEnabled) {
            addTargetToDelegates(ID, target, delegates);
        }
        else {
            boolean hasTarget = false;

            for (TargetDelegate tar: delegates) {
                if (identifierForTarget(target).equals(identifierForTarget(tar))) {
                    hasTarget = true;
                    break;
                }
            }
            if (!hasTarget) {
                addTargetToDelegates(ID, target, delegates);
            }
        }
    }

    /** Sample identifier of:
     * com.prometheussoftware.auikit.tableview.-$$Lambda$UITableViewHolder$Cell$v2easIbfya2-GSyqkKBKhC2F3Oo
     * would be:
     * com.prometheussoftware.auikit.tableview.-$$Lambda$UITableViewHolder$Cell */
    private String identifierForTarget (TargetDelegate target) {

        ArrayList components = ArrayUtility.arrayList(target.getClass().toString().split("\\$"));
        if (1 < components.size()) {
            components.remove(components.size()-1);
            return components.toString();
        }
        return "";
    }

    private void addTargetToDelegates(Object ID, TargetDelegate target, ArrayList<TargetDelegate> delegates) {
        delegates.add(target);
        targets.put(ID, delegates);
    }

    public void addTouchDownTarget(Object ID, TouchDown target) {
        addTarget(ID, target);
    }

    public void addTouchUpTarget(Object ID, TouchUp target) {
        addTarget(ID, target);
    }

    public void removeTarget(TargetDelegate target) {
        targets.remove(target);
    }

    public HashMap<Object, ArrayList<TargetDelegate>> getTargets() {
        return targets;
    }

    public boolean isMultiTargetEnabled() {
        return multiTargetEnabled;
    }

    public void setMultiTargetEnabled(boolean multiTargetEnabled) {
        this.multiTargetEnabled = multiTargetEnabled;
    }

    public interface TargetDelegate { }

    @FunctionalInterface
    public interface TouchDown extends TargetDelegate {
        void controlPressed(UIControl sender);
    }

    @FunctionalInterface
    public interface TouchUp extends TargetDelegate {
        void controlReleased(UIControl sender);
    }

    //endregion
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
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
