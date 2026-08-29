package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.uiview.UIBarButton;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UINavBarButtonTarget implements NavBarButtonTargetProtocol {

    private UIViewController viewControllerContainingNavigationBar;
    private Map<UIBarButton.POSITION, List<UIBarButton>> barButtons;
    private NavBarButtonTargetProtocol delegate = this;

    public UINavBarButtonTarget() {
        super();
    }

    public UINavBarButtonTarget(NavBarButtonTargetProtocol delegate) {
        super();
        this.delegate = delegate;
    }

    public void setDelegate(NavBarButtonTargetProtocol delegate) {
        this.delegate = delegate;
    }

    public UIBarButton buttonOfType (int type, UIBarButton.POSITION position) {
        if (barButtons == null) return null;
        return ArrayUtility.objectPassingTest(barButtons.get(position), (UIBarButton obj) -> obj.type.intValue() == type);
    }

    public UIBarButton doneButton() {
        return ArrayUtility.objectPassingTest(barButtons.get(UIBarButton.POSITION.RIGHT), (UIBarButton obj) -> isDoneButtonOfType(obj.type));
    }

    public boolean isDoneButtonOfType (UIBarButton.TYPE type) {
        return type == UIBarButton.TYPE.SYSTEM_DONE || type == UIBarButton.TYPE.SYSTEM_EDIT || type == UIBarButton.TYPE.SYSTEM_SAVE;
    }

    @Override
    public void addButtonOfType(UIBarButton.TYPE type, UIBarButton.POSITION position) {

        UIBarButton button = buttonOfType(type.intValue(), position);

        if (barButtons == null) {
            barButtons = new HashMap();
            barButtons.put(UIBarButton.POSITION.RIGHT, new ArrayList());
            barButtons.put(UIBarButton.POSITION.LEFT, new ArrayList());
        }
        else if (button != null) {
            barButtons.get(position).remove(button);
        }

        if (type == UIBarButton.TYPE.SYSTEM_EDIT) {
            button = UIBarButton.editNavBarButtonObject();
        }
        else {
            UIBarButton.POSITION pos = positionForButtonOfType(type);
            String title = titleForButtonOfType(type);
            UIImage image = imageForButtonOfType(type);
            boolean enabled = isEnabledButtonOfType(type);

            UITargetDelegate.TouchUp target = sender -> internalButtonPressed((UIBarButton) sender);

            if (image != null)
                button = new UIBarButton(image, target);
            else if (type.intValue() < UIBarButton.TYPE.SYSTEM_COUNT.intValue() || StringUtility.isNotEmpty(title))
                button = UIBarButton.navBarButtonWithTitle(title, type, target);
            else
                button = UIBarButton.spacer();

            button.position = pos;
            button.setEnabled(enabled);
            button.type = type;
        }

        barButtons.get(position).add(button);
    }

    public void clearBarButtons() {
        barButtons = null;
    }

    public void internalButtonPressed (UIBarButton sender) {
        viewControllerContainingNavigationBar.endEditing();

        Object object = saveObject();
        if (object == null) object = sender;

        UITargetDelegate.TouchUp target = actionForButtonOfType(sender.type);
        target.controlReleased(sender);
    }

    @Override
    public void setNavBarItems() {
        this.delegate.setNavBarItems();
    }

    @Override
    public boolean hasButtonOfType (UIBarButton.TYPE type) {
        return this.delegate.hasButtonOfType(type);
    }

    @Override
    public UIBarButton.POSITION positionForButtonOfType (UIBarButton.TYPE type) {
        return this.delegate.positionForButtonOfType(type);
    }

    @Override
    public String titleForButtonOfType (UIBarButton.TYPE type) {
        return this.delegate.titleForButtonOfType(type);
    }

    @Override
    public UIImage imageForButtonOfType (UIBarButton.TYPE type) {
        return this.delegate.imageForButtonOfType(type);
    }

    @Override
    public Object saveObject() {
        return this.delegate.saveObject();
    }

    @Override
    public boolean isEnabledButtonOfType (UIBarButton.TYPE type) {
        return this.delegate.isEnabledButtonOfType(type);
    }

    @Override
    public UITargetDelegate.TouchUp actionForButtonOfType (UIBarButton.TYPE type) {
        return this.delegate.actionForButtonOfType(type);
    }

    @Override
    public UIViewController viewControllerContainingNavigationBar() {
        return viewControllerContainingNavigationBar;
    }

    @Override
    public void setViewControllerContainingNavigationBar(UIViewController viewControllerContainingNavigationBar) {
        this.viewControllerContainingNavigationBar = viewControllerContainingNavigationBar;
    }

    @Override
    public Map<UIBarButton.POSITION, List<UIBarButton>> getBarButtons() {
        return barButtons;
    }
}
