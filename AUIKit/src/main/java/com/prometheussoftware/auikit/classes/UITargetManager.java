package com.prometheussoftware.auikit.classes;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;
import java.util.HashMap;

public final class UITargetManager {

    private HashMap<Object, ArrayList<UITargetDelegate>> targets = new HashMap<>();
    private Delegate delegate;

    /** Default is true. If true it allows a single key in targets map
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

    static {
        Identifier.Register(UITargetManager.class);
    }

    public UITargetManager(Delegate delegate) {
        super();
        this.delegate = delegate;
    }

    //region events

    public boolean handleOnKeyDown(int keyCode, KeyEvent event) {
        for (Object ID : targets.keySet()) {
            for (UITargetDelegate target : targets.get(ID)) {
                if (target instanceof UITargetDelegate.KeyDown) {
                    if (!delegate.targetWillSetKey(target, true)) return false;
                    UITargetDelegate.KeyDown keyDown = (UITargetDelegate.KeyDown) target;
                    keyDown.keyPressed(delegate, keyCode);
                }
            }
        }
        return true;
    }

    public boolean handleOnKeyUp(int keyCode, KeyEvent event) {
        for (Object ID : targets.keySet()) {
            for (UITargetDelegate target : targets.get(ID)) {
                if (target instanceof UITargetDelegate.KeyUp) {
                    if (!delegate.targetWillSetKey(target, false)) return false;
                    UITargetDelegate.KeyUp keyUp = (UITargetDelegate.KeyUp) target;
                    keyUp.keyReleased(delegate, keyCode);
                }
            }
        }
        return true;
    }


    public boolean handleOnTouch(View v, MotionEvent event) {
        for (Object ID : targets.keySet()) {
            for (UITargetDelegate target : targets.get(ID)) {
                if (target instanceof UITargetDelegate) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (target instanceof UITargetDelegate.TouchUp) {
                            if (!delegate.targetWillSetTouch(target, false)) return false;
                            UITargetDelegate.TouchUp touchUp = (UITargetDelegate.TouchUp) target;
                            touchUp.controlReleased(delegate);
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (target instanceof UITargetDelegate.TouchDown) {
                            if (!delegate.targetWillSetTouch(target, true)) return false;
                            UITargetDelegate.TouchDown touchDown = (UITargetDelegate.TouchDown) target;
                            touchDown.controlPressed(delegate);
                        }
                    }
                }
            }
        }
        return true;
    }

    //endregion


    //region targets

    public void setTarget(UITargetDelegate.TouchUp target) {

        ArrayList<UITargetDelegate> delegates = new ArrayList<>();
        delegates.add(target);
        targets.put(this, delegates);
    }

    public void setKeyTarget(UITargetDelegate.KeyUp target) {

        ArrayList<UITargetDelegate> delegates = new ArrayList<>();
        delegates.add(target);
        targets.put(this, delegates);
    }

    /** @apiNote It does not distinguish between different instances
     * of the same lambda, user must be careful not to add the
     * same target multiple times, that would result in the
     * lambda being executed multiple times. Set
     *  multiTargetEnabled = false to guarantee unique action on each
     *  touch event */
    public void addTarget(Object ID, UITargetDelegate target) {

        ArrayList<UITargetDelegate> delegates = targets.get(ID);
        if (delegates == null) {
            delegates = new ArrayList<>();
        }

        if (delegates.isEmpty() || multiTargetEnabled) {
            addTargetToDelegates(ID, target, delegates);
        }
        else {
            boolean hasTarget = false;

            for (UITargetDelegate tar: delegates) {
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
    private String identifierForTarget (UITargetDelegate target) {

        ArrayList components = ArrayUtility.arrayList(target.getClass().toString().split("\\$"));
        if (1 < components.size()) {
            components.remove(components.size()-1);
            return components.toString();
        }
        return "";
    }

    private void addTargetToDelegates(Object ID, UITargetDelegate target, ArrayList<UITargetDelegate> delegates) {
        delegates.add(target);
        targets.put(ID, delegates);
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
        targets.remove(target);
    }

    public HashMap<Object, ArrayList<UITargetDelegate>> getTargets() {
        return targets;
    }

    public boolean isMultiTargetEnabled() {
        return multiTargetEnabled;
    }

    public void setMultiTargetEnabled(boolean multiTargetEnabled) {
        this.multiTargetEnabled = multiTargetEnabled;
    }

    //endregion

    public interface Delegate extends UITargetDelegate.Sender {
        /** @apiNote Called just before controlPressed or controlReleased are triggered. */
        boolean targetWillSetTouch(UITargetDelegate target, boolean down);

        /** @apiNote Called just before keyPressed or keyReleased are triggered. */
        boolean targetWillSetKey(UITargetDelegate target, boolean down);
    }
}
