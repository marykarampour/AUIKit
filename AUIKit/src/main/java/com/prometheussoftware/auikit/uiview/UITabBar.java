package com.prometheussoftware.auikit.uiview;

import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Assets;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.uiviewcontroller.UIBarItem;
import com.prometheussoftware.auikit.uiviewcontroller.UITabBarItem;
import com.prometheussoftware.auikit.uiviewcontroller.UITabBarProtocol;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public class UITabBar extends UIView {

    private UITabBarProtocol delegate;

    /** get/set visible UITabBarItems. default is nil.
     * changes not animated. shown in order */
    private ArrayList<UITabBarItem> items;

    private ArrayList<UITabBarButton> buttons;

    /** will show feedback based on mode. default is nil */
    private UITabBarItem selectedItem;

    private UIColor barTintColor = App.theme().Tab_Bar_Tint_Color();

    private UIColor unselectedItemTintColor = App.theme().Tab_Bar_Tint_Color();

    private UIView contentView;

    private UIImageView selectionIndicatorImageView;

    private UIImageView shadowImageView;

    private UIImageView backgroundImageView;

    public UITabBar() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();

        contentView = new UIView();
        contentView.setBackgroundColor(UIColor.clear());

        shadowImageView = new UIImageView();
        shadowImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        shadowImageView.setTintColor(UIColor.gray(0.4f));

        backgroundImageView = new UIImageView();
        selectionIndicatorImageView = new UIImageView();

        setBackgroundImage(Assets.Pixel_Image());
        setSelectionIndicatorImage(Assets.Pixel_Image());
        setShadowImage(Assets.Shadow_Gradient_Down_Image());
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubView(shadowImageView);
        addSubView(backgroundImageView);
        addSubView(contentView);
        contentView.addSubView(selectionIndicatorImageView);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        UIEdgeInsets contentInsets = new UIEdgeInsets(contentPadding(), 0, 0, 0);

        constraintSidesForView(backgroundImageView, contentInsets);
        constraintSidesForView(contentView, contentInsets);

        constraintForView(ConstraintSet.TOP, shadowImageView);
        constraintForView(ConstraintSet.END, shadowImageView);
        constraintForView(ConstraintSet.START, shadowImageView);
        constraintViews(shadowImageView, ConstraintSet.BOTTOM, backgroundImageView, ConstraintSet.TOP);

        applyConstraints();
    }

    private int contentPadding() {
        return Dimensions.Int_2();
    }

    public void setItems(ArrayList<UITabBarItem> items) {

        ArrayList<UITabBarButton> arr = new ArrayList<>();

        contentView.removeAllConstraints();
        contentView.removeAllViews();

        for (UITabBarItem item : items) {

            UITabBarButton button = new UITabBarButton();
            button.setText(item.getTitle());
            button.setImage(item.getImage());
            button.setTarget( b -> {
                selectItem(item);
            });

            contentView.addSubView(button);
            arr.add(button);
        }

        this.items = items;
        this.buttons = arr;

        contentView.constraintHorizontally(arr, Dimensions.Int_2(), true);
        contentView.applyConstraints();
    }

    private void selectItem (UITabBarItem item) {
        if (item == null || item.equals(selectedItem)) return;

        this.selectedItem = item;
        contentView.removeConstraints(selectionIndicatorImageView);

        for (UITabBarItem obj : items) {
            UITabBarButton button = buttonForItem(obj);
            if (obj.equals(item)) {
                button.setTintColor(barTintColor);
                button.setImage(obj.getSelectedImage());
                constraintSameForViews(selectionIndicatorImageView, button);
            }
            else {
                button.setImage(obj.getImage());
                button.setTintColor(unselectedItemTintColor);
            }
        }

        if (delegate != null) delegate.tabBarDidSelectItem(this, item);
    }

    public void selectItemAtIndex (int index) {
        if (index < 0 || items.size() <= index) return;
        selectItem(items.get(index));
    }

    private UITabBarButton buttonForItem (UIBarItem item) {
        int index = items.indexOf(item);
        return ArrayUtility.safeGet(buttons, index);
    }

    public void setDelegate(UITabBarProtocol delegate) {
        this.delegate = delegate;
    }

    public void setBarTintColor(UIColor barTintColor) {
        this.barTintColor = barTintColor;
    }

    public void setUnselectedItemTintColor(UIColor unselectedItemTintColor) {
        this.unselectedItemTintColor = unselectedItemTintColor;
    }

    public void setBackgroundImage(UIImage backgroundImage) {
        backgroundImageView.setImage(backgroundImage);
    }

    public void setSelectionIndicatorImage(UIImage selectionIndicatorImage) {
        selectionIndicatorImageView.setImage(selectionIndicatorImage);
    }

    public void setShadowImage(UIImage shadowImage) {
        shadowImageView.setImage(shadowImage);
    }
}
