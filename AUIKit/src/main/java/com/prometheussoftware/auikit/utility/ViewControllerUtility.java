package com.prometheussoftware.auikit.utility;

import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.uiview.UIBarButton;
import com.prometheussoftware.auikit.uiviewcontroller.UINavigationController;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public class ViewControllerUtility {

    public static UIBarButton setNextButton (UIViewController sourceVC, UIViewController destinationVC) {
        return setNextButton(sourceVC, destinationVC, null, null);
    }

    public static UIBarButton setNextButton (UIViewController sourceVC, UIViewController destinationVC, ControlStateCheck check) {
        return setNextButton(sourceVC, destinationVC, check, null);
    }

    public static UIBarButton setNextButton (UIViewController sourceVC, UIViewController destinationVC, NextAction action) {
        return setNextButton(sourceVC, destinationVC, null, action);
    }

    public static UIBarButton setNextButton (UIViewController sourceVC, UIViewController destinationVC, ControlStateCheck check, NextAction action) {

        UIBarButton button = new UIBarButton();
        button.setText(Constants.Next_STR());
        button.setTarget( b -> nextPressed(sourceVC, destinationVC, check, action) );
        sourceVC.getNavigationBar().setRightBarButtonItem(button);
        return button;
    }

    private static void nextPressed (UIViewController sourceVC, UIViewController destinationVC, ControlStateCheck check, NextAction action) {

        sourceVC.endEditing();

        if (check != null && !check.isEnabled()) return;

        UINavigationController nav = sourceVC.getNavigationController();
        if (nav != null) {
            if (action != null)
                action.perform();
            nav.pushViewController(destinationVC, true);
        }
    }

    public interface ControlStateCheck {
        boolean isEnabled();
    }

    public interface NextAction {
        void perform();
    }
}
