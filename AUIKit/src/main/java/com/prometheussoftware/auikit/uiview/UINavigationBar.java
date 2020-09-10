package com.prometheussoftware.auikit.uiview;

import android.util.Size;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Assets;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public class UINavigationBar extends UIView {

    private ArrayList<UIButton> leftBarButtonItems = new ArrayList<>();
    private ArrayList<UIButton> rightBarButtonItems = new ArrayList<>();
    private UILabel titleLabel;
    private UIView titleView;
    private UIImageView shadow;

    public UINavigationBar() {
        super();
        init();
        initBase();
    }

    private void initBase() {

        addItem(titleLabel, UIEnum.ALIGNMENT.CENTER_X);
        UIButton leftButton = barButtonItem(App.assets().Left_Chevron_Image());
        setLeftBarButtonItem(leftButton);
    }

    @Override
    public void initView() {
        super.initView();

        titleLabel = new UILabel();
        titleLabel.setGravity(Gravity.CENTER);
        titleLabel.setFont(App.theme().Nav_Bar_Font());
        titleLabel.setTextColor(App.theme().Nav_Bar_Tint_Color());

        shadow = new UIImageView();
        shadow.setScaleType(ImageView.ScaleType.FIT_XY);

        setBackgroundColor(App.theme().Nav_Bar_Background_Color());
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubView(titleLabel);
        addSubView(shadow);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        constraintHeightForView(shadow, App.constants().Nav_Bar_Shadow_Size());
        constraintForView(ConstraintSet.START, shadow);
        constraintForView(ConstraintSet.END, shadow);
        constraintForView(ConstraintSet.BOTTOM, shadow);

        applyConstraints();
    }

    //region items
    //TODO: WIP - debugging
    private UIButton barButtonItem(UIImage image) {
        UIButton button = new UIButton();
        button.setImage(image);
        button.setTintColor(App.theme().Nav_Bar_Tint_Color());
        button.setBackgroundColor(UIColor.red(1.0f));
        return button;
    }

    /** Adds a view at the position defined by UIEnum.ALIGNMENT.
     * @param view The view to be added. Pass null to add a button at left or right, or label in center. */
    public void addItem (UIView view, UIEnum.ALIGNMENT position) {

        switch (position) {
            case LEFT: {
                if (view == null) {
                    addItem(new UIButton(), position);
                    return;
                }
                else if (view instanceof UIButton) {
                    addSubView(view);

                    constraintSizeForView(view, App.constants().Nav_Bar_Icon_Size());
                    constraintCenterXForView(view);

                    if (leftBarButtonItems.size() == 0) {

                        constraintForView(ConstraintSet.START, view);
                        constraintViews(titleLabel, ConstraintSet.START, view, ConstraintSet.END);
                    }
                    else {
                        constraintViews(view, ConstraintSet.START, ArrayUtility.lastObject(leftBarButtonItems), ConstraintSet.END);
                        constraintViews(titleLabel, ConstraintSet.START, view, ConstraintSet.END);
                    }
                }
            }
            case RIGHT: {
                if (view == null) {
                    addItem(new UIButton(), position);
                    return;
                }
                else if (view instanceof UIButton) {

                    addSubView(view);

                    constraintSizeForView(view, App.constants().Nav_Bar_Icon_Size());
                    constraintCenterXForView(view);

                    if (rightBarButtonItems.size() == 0) {

                        constraintForView(ConstraintSet.END, view);
                        constraintViews(titleLabel, ConstraintSet.END, view, ConstraintSet.START);
                    }
                    else {
                        constraintViews(view, ConstraintSet.END, ArrayUtility.lastObject(rightBarButtonItems), ConstraintSet.START);
                        constraintViews(titleLabel, ConstraintSet.END, view, ConstraintSet.START);
                    }
                }
            }
            default: {
                if (view == null) {
                    addItem(new UILabel(), position);
                    return;
                }
                else {
                    titleView = view;
                    addSubView(view);

                    constraintCenterXForView(view);
                    constraintCenterYForView(view);

                    constraintHeightForView(view, ConstraintSet.WRAP_CONTENT);
                    constraintWidthForView(view, ConstraintSet.WRAP_CONTENT);
                }
            }
        }

        applyConstraints();
    }

    //endregion

    //region fields

    public Size iconSize() {
        return App.constants().Nav_Bar_Icon_Size();
    }

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void setShadowTintColor(UIColor shadowTintColor) {
        if (shadowTintColor == null || shadowTintColor.get() == 0) {
            shadow.setImage(null);
        }
        else {
            shadow.setTintColor(shadowTintColor);
            shadow.setImage(App.assets().Shadow_Gradient_Image());
        }
    }

    //endregion

    //region controls

    public UIView getTitleView() {
        return titleView;
    }

    public ArrayList<UIButton> getLeftBarButtonItems() {
        return leftBarButtonItems;
    }

    public ArrayList<UIButton> getRightBarButtonItems() {
        return rightBarButtonItems;
    }

    public void setLeftBarButtonItems(ArrayList<UIButton> leftBarButtonItems) {
        setBarButtonItems(leftBarButtonItems, UIEnum.ALIGNMENT.LEFT);
    }

    public void setRightBarButtonItems(ArrayList<UIButton> rightBarButtonItems) {
        setBarButtonItems(leftBarButtonItems, UIEnum.ALIGNMENT.RIGHT);
    }

    private void constraintItems (ArrayList<UIButton> items, UIEnum.ALIGNMENT position) {

        if (items == null || items.size() == 0) return;
        UIButton first = ArrayUtility.safeGet(items, 0);
        UIButton last = ArrayUtility.safeGetFromEnd(items, 0);
        constraintHorizontally(items, 0, false);
        constraintSizeForViews(items, App.constants().Nav_Bar_Icon_Size());

        if (position == UIEnum.ALIGNMENT.LEFT) {
            constraintForView(ConstraintSet.START, first);
            constraintViews(titleLabel, ConstraintSet.START, last, ConstraintSet.END);
        }
        else {
            constraintForView(ConstraintSet.END, first);
            constraintViews(titleLabel, ConstraintSet.END, last, ConstraintSet.START);
        }
        applyConstraints();
    }

    /** The contents of this property always refer to the first bar button item in the leftBarButtonItems array.
     * Assigning a new value to this property replaces the first item in the leftBarButtonItems array with the new value.
     * Setting this property to null removes the first item in the array.
     * If the bar button item is already in the array, it is moved from its current location to the front of the array.
     * */
    public void setLeftBarButtonItem(UIButton item) {
        setBarButtonItem(item, UIEnum.ALIGNMENT.LEFT);
    }

    /** The contents of this property always refer to the first bar button item in the rightBarButtonItems array.
     * Assigning a new value to this property replaces the first item in the rightBarButtonItems array with the new value.
     * Setting this property to null removes the first item in the array.
     * If the bar button item is already in the array, it is moved from its current location to the front of the array.
     * */
    public void setRightBarButtonItem(UIButton item) {
        setBarButtonItem(item, UIEnum.ALIGNMENT.RIGHT);
    }

    public void setBarButtonItem(UIButton item, UIEnum.ALIGNMENT position) {

        ArrayList<UIButton> buttons = barButtonItemsForPosition(position);

        if (ArrayUtility.firstObject(buttons) == item) return;

        ArrayList<UIButton> tempButtons = (ArrayList<UIButton>) buttons.clone();

        clearBarButtonItems(position);

        if (item == null) {
            ArrayUtility.safeRemove(tempButtons, 0);
        }
        else {
            ArrayUtility.safeMove(tempButtons, 0, item);
        }

        constraintBarButtonItems(tempButtons, position);
    }

    public void setBarButtonItems(ArrayList<UIButton> items, UIEnum.ALIGNMENT position) {
        clearBarButtonItems(position);
        if (items == null || items.size() == 0) return;
        constraintBarButtonItems(items, position);
    }

    private ArrayList<UIButton> barButtonItemsForPosition(UIEnum.ALIGNMENT position) {
        return (position == UIEnum.ALIGNMENT.LEFT ? leftBarButtonItems : rightBarButtonItems);
    }

    private void clearBarButtonItems(UIEnum.ALIGNMENT position) {
        if (position == UIEnum.ALIGNMENT.LEFT) {
            clearBarButtonItems(leftBarButtonItems);
        }
        else {
            clearBarButtonItems(rightBarButtonItems);
        }
    }

    private void clearBarButtonItems(ArrayList<UIButton> items) {
        items.clear();
        removeSubViews(items);
    }

    private void constraintBarButtonItems(ArrayList<UIButton> items, UIEnum.ALIGNMENT position) {
        addSubViews(items);
        constraintItems(items, position);

        if (position == UIEnum.ALIGNMENT.LEFT) {
            leftBarButtonItems = items;
        }
        else {
            rightBarButtonItems = items;
        }
    }

    public UIButton leftBarButtonItem() {
        return ArrayUtility.firstObject(leftBarButtonItems);
    }

    public UIButton rightBarButtonItem() {
        return ArrayUtility.firstObject(rightBarButtonItems);
    }

    //endregion
}
