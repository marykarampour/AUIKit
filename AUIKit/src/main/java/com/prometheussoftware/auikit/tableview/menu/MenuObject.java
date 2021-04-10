package com.prometheussoftware.auikit.tableview.menu;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.utility.DEBUGLOG;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MenuObject {

    public String title;
    public UIFont font;
    public UIColor textColor;
    public boolean hidden;
    public Action action;
    private Class VCClass;
    /** In case the VC is a subclass initialized based on type
     * set trueVCClass to that class, default is VCClass.
     * This is set in viewController() */
    private Class trueVCClass;
    private int type;

    public void setTrueVCClass() {
        viewController();
    }

    public void setTrueVCClass(Class trueVCClass) {
        this.trueVCClass = trueVCClass;
    }

    public Class getTrueVCClass() {
        return trueVCClass == null ? VCClass : trueVCClass;
    }

    public void setVCClass(Class VCClass) {
        this.VCClass = VCClass;
//        setTrueVCClass();
    }

    public Class getVCClass() {
        return VCClass;
    }

    public void setType(int type) {
        this.type = type;
//        setTrueVCClass();
    }

    public int getType() {
        return type;
    }

    public <VC extends UIViewController> VC viewController() {
        //TODO: do not init if already exists
        //TODO: add support for builder using type

        UIViewController nextViewController = null;

        if (UIViewController.class.isAssignableFrom(VCClass)) {

            try {
                Constructor constructor = VCConstructor(int.class);
                if (constructor == null) {
                    constructor = VCConstructor();
                    if (constructor != null) {
                        constructor.setAccessible(true);
                        nextViewController = (UIViewController) constructor.newInstance();
                    }
                }
                else {
                    constructor.setAccessible(true);
                    nextViewController = (UIViewController) constructor.newInstance(type);
                }
            }
            catch (IllegalAccessException e) { e.printStackTrace(); }
            catch (InstantiationException e) { e.printStackTrace(); }
            catch (InvocationTargetException e) { e.printStackTrace(); }
        }
        if (nextViewController != null) {
            trueVCClass = nextViewController.getClass();
        }
        return (VC) nextViewController;
    }

    private Constructor VCConstructor(Class<?>... parameterTypes) {
        try {
            return VCClass.getDeclaredConstructor(parameterTypes);
        }
        catch (NoSuchMethodException e) {
            DEBUGLOG.s(VCClass, " --> Failed to create menu item from VCClass");
            e.printStackTrace();
            try {
                return VCClass.getConstructor(parameterTypes);
            }
            catch (NoSuchMethodException ex) {
                DEBUGLOG.s(VCClass, " --> Failed to create menu item from VCClass");
                ex.printStackTrace();
                return null;
            }
        }
    }

    public Action getAction() {
        return action;
    }

    public interface Action {
        void itemPressed(MenuObject item);
    }
}
