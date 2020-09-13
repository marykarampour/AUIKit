package com.prometheussoftware.auikit.genericviews;

import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Size;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.uiview.UIImageView;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.uiview.UISwitch;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.HashMap;

public abstract class UIAccessoryView extends UISwitch implements UIAccessoryViewProtocol {

    private boolean on;

    /** Default is AppTheme.accessorySelectedColor */
    private UIColor selectedColor = App.theme().Accessory_Selected_Color();

    /** Default is AppTheme.accessoryDisabledColor */
    private UIColor disabledColor = App.theme().Accessory_Disabled_Color();

    /** Default is AppTheme.accessoryDeselectedColor */
    private UIColor deselectedColor = App.theme().Accessory_Deselected_Color();

    protected UIImage onImage;
    protected UIImage offImage;
    protected String offTitle;
    protected String onTitle;

    static {
        Identifier.Register(UIAccessoryView.class);
    }

    public UIAccessoryView() {
        super();
    }

    public void setOn(boolean on) {
        this.on = on;
        updateViews();
    }

    public boolean isOn() {
        return on;
    }

    /** Call setEnabled or updateViews after on/off images and colors are set to update views */
    @Override public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateViews();
    }

    protected void updateImage (UIImageView image) {
        image.setTintColor(colorForState());
        image.setImage(on ? onImage : offImage);
    }

    protected void updateLabel (UILabel label) {
        label.setText(on ? onTitle : offTitle);
        label.setTextColor(colorForState());
    }

    private UIColor colorForState() {
        return isEnabled() ? (on ? selectedColor : deselectedColor) : disabledColor;
    }

    protected UILabel createLabel() {

        UILabel label = new UILabel();
        label.getView().setLines(numberOfLines());
        label.getView().setPadding(textPadding(), textPadding(), textPadding(), textPadding());
        label.setFont(font());
        label.getView().setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        return label;
    }

    @Override
    public Size size() {
        return App.constants().Accessory_Size();
    }

    @Override
    public UIFont font() {
        return App.theme().Medium_Bold_Font();
    }

    //region subclasses

    public static UIAccessoryView build(int type) {

        UIAccessoryView view;
        boolean hasImage = TYPE.IMAGE.isOption(type);
        boolean hasTitle = TYPE.TITLE.isOption(type);

        if (hasImage && hasTitle) {
            view = new TitleImage();
        }
        else if (hasImage) {
            view = new Image();
        }
        else if (hasTitle) {
            view = new Title();
        }
        else {
            view = new View();
        }
        view.init();
        return view;
    }

    public static UIAccessoryView build(TYPE type) {
        return build(type.intValue());
    }

    public static class Image extends UIAccessoryView {

        private UIImageView imageView;

        protected Image() {
            super();
        }

        @Override public void initView() {
            super.initView();
            imageView = new UIImageView();
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        @Override public void loadView() {
            super.loadView();
            addSubView(imageView);
        }

        @Override public void constraintLayout() {
            super.constraintLayout();
            constraintSidesForView(imageView, imageInsets());
            applyConstraints();
        }

        @Override public void updateViews() {
            updateImage(imageView);
        }
    }

    public static class Title extends UIAccessoryView {

        private UILabel label;

        protected Title() {
            super();
        }

        @Override public void initView() {
            super.initView();
            label = createLabel();
        }

        @Override public void loadView() {
            super.loadView();
            addSubView(label);
        }

        @Override public void constraintLayout() {
            super.constraintLayout();
            constraintSidesForView(label);
            applyConstraints();
        }

        @Override public void updateViews() {
            updateLabel(label);
        }

        @Override public Size size() {
            return new Size(widthForLabel(label), super.size().getHeight());
        }
    }

    public static class TitleImage extends UIAccessoryView {

        private UIImageView imageView;
        private UILabel label;

        protected TitleImage() {
            super();
        }

        @Override public void initView() {
            super.initView();

            imageView = new UIImageView();
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            label = createLabel();
        }

        @Override public void loadView() {
            super.loadView();
            addSubView(label);
            addSubView(imageView);
        }

        @Override public void constraintLayout() {
            super.constraintLayout();

            constraintSizeForView(imageView, super.size());
            constraintCenterYForView(imageView);

            constraintForView(ConstraintSet.TOP, label, labelInsets().top);
            constraintForView(ConstraintSet.START, label, labelInsets().left);
            constraintForView(ConstraintSet.BOTTOM, label, labelInsets().bottom);
            constraintForView(ConstraintSet.END, imageView, imageInsets().right);

            constraintViews(label, ConstraintSet.END, imageView, ConstraintSet.START, imageInsets().left + labelInsets().right);

            applyConstraints();
        }

        @Override public void updateViews() {
            updateImage(imageView);
            updateLabel(label);
        }

        @Override public Size size() {
            return new Size(
                    widthForLabel(label) + super.size().getWidth() + imageInsets().left + labelInsets().left + labelInsets().right,
                    super.size().getHeight() + labelInsets().top + labelInsets().bottom);
        }
    }

    public static class View <V extends UIView> extends UIAccessoryView {

        private V view;

        protected View() {
            super();
        }

        public void setView(V view) {
            this.view = view;
            addSubView(view);
            constraintSidesForView(view, imageInsets());
            applyConstraints();
        }

        public V getView() {
            return view;
        }

        @Override public void constraintLayout() {
        }

        @Override
        public void updateViews() {
        }

        @Override
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    //endregion

    //region fields

    public void setOnImage(UIImage onImage) {
        this.onImage = onImage;
    }

    public void setOffImage(UIImage offImage) {
        this.offImage = offImage;
    }

    public void setOnTitle(String onTitle) {
        this.onTitle = onTitle;
    }

    public void setOffTitle(String offTitle) {
        this.offTitle = offTitle;
    }

    public void setSelectedColor(UIColor selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setDeselectedColor(UIColor deselectedColor) {
        this.deselectedColor = deselectedColor;
    }

    public void setDisabledColor(UIColor disabledColor) {
        this.disabledColor = disabledColor;
    }

    //endregion

    //region helpers

    protected int widthForLabel(UILabel label) {
        Paint paint = label.getView().getPaint();
        Rect rect = new Rect();

        if (onTitle != null) {
            paint.getTextBounds(onTitle, 0, onTitle.length(), rect);
        }
        int on = rect.width();

        if (onTitle != null) {
            paint.getTextBounds(offTitle, 0, offTitle.length(), rect);
        }
        int off = rect.width();

        return Math.max(on, off);
    }

    //endregion

    public enum TYPE {
        VIEW(0),
        IMAGE(1),
        TITLE(2);

        private int value;
        private static HashMap<Integer, TYPE> map = new HashMap<>();

        static  {
            for (TYPE type : values()) {
                map.put(type.value, type);
            }
        }

        TYPE(int i) {
            value = i;
        }

        public int intValue() { return value; }

        public static TYPE valueOf (int i) {
            return map.get(i);
        }

        public boolean isOption (int option) {
            return (option & value) == value;
        }

    }

}

