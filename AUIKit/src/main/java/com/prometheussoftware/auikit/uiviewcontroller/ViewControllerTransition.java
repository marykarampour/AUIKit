package com.prometheussoftware.auikit.uiviewcontroller;

public interface ViewControllerTransition {
    enum RESULT_TYPE {
        UNKNOWN,
        SUCCESS,
        FAILURE
    }

    interface Delegate {
        default void viewControllerDidReturnWithResult (UIViewController viewController, RESULT_TYPE resultType, Object object) {}
    }

    interface Protocol {
        default void setTransitionDelegate (Delegate transitionDelegate) {}
        default Delegate transitionDelegate() { return null; }
        default void didSetTransitionDelegate (Delegate transitionDelegate) {}
    }
}
