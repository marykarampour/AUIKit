package com.prometheussoftware.auikit.uiviewcontroller;

public interface TransitioningDelegate {

    enum RESULT_TYPE {
        UNKNOWN,
        SUCCESS,
        FAILURE
    }

    void viewControllerDidReturnWithResult (UIViewController viewController, RESULT_TYPE resultType, Object object);
}
