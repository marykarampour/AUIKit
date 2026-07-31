package com.prometheussoftware.auikit.tableview;

import android.util.Size;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.genericviews.UIAccessoryView;
import com.prometheussoftware.auikit.genericviews.UIMultiViewLabel;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.uiview.UIButton;
import com.prometheussoftware.auikit.uiview.UIControl;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.UIControlProtocol;
import com.prometheussoftware.auikit.uiview.protocols.UIEditingAccessoryProtocol;
import com.prometheussoftware.auikit.utility.ViewUtility;

import java.util.HashMap;
import java.util.Map;

public abstract class UITableViewCell <A extends UIAccessoryView, S extends UIView> extends UIMultiViewLabel <UIButton, A, UIView> implements UIControlProtocol, UIEditingAccessoryProtocol {

    private ACCESSORY_TYPE accessoryType;
    private SELECTION_STYLE selectionStyle;
    private EDITING_STYLE editingStyle;
    private STYLE style;

    /** Use only in case of static cells */
    public IndexPath indexPath;

    protected S separator;

    private UIControl interactionLayer;

    private int separatorHeight;

    static {
        Identifier.Register(UITableViewCell.class);
    }

    /** Call init() in constructor of subclass */
    public UITableViewCell() {
        this(STYLE.DEFAULT);
    }

    public UITableViewCell(STYLE style) {
        super(style == STYLE.SUBTITLE ? LABEL_TYPE.COUNT.intValue() : 1);
        setEnabled(true);
        accessoryType = ACCESSORY_TYPE.NONE;
        this.style = style;
    }

    private void baseSetup() {
        if (separatorHeight == 0) separatorHeight = separatorHeight();
        getLabel(LABEL_TYPE.TEXT.intValue()).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        getLabel(LABEL_TYPE.DETAIL_TEXT.intValue()).setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
    }

    @Override
    public void init() {
        setSize();
        super.init();
        baseSetup();
        constraintLayout();
        addInteractionLayer();
    }

    @Override
    public void initView() {
        super.initView();
        createSeparator();
        createInteractionLayer();
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        contentView.constraintHeightForView(separator, separatorHeight);
        contentView.constraintForView(ConstraintSet.START, separator, 0);
        contentView.constraintForView(ConstraintSet.END, separator, 0);
        contentView.constraintForView(ConstraintSet.BOTTOM, separator, 0);
        contentView.applyConstraints();
    }

    protected void createSeparator() {
        separator = (S) new UIView();
        separator.setEnabled(false);
        setSeparatorColor(App.theme().Tableview_Separator_Color());
        contentView.addSubview(separator);
    }

    private void createInteractionLayer() {
        interactionLayer = new UIControl();
        interactionLayer.setMultiTargetEnabled(false);
    }

    private void addInteractionLayer() {
        setTopView(interactionLayer, false, false);
    }

    @Override
    public void createRightView() {
        rightView = (A) UIAccessoryView.build(UIAccessoryView.TYPE.IMAGE);
        rightView.setEnabled(true);
    }

    @Override
    public void createLeftView() {
        leftView = new UIButton();
        leftView.getImageView().view().setAdjustViewBounds(true);
        leftView.setScaleType(ImageView.ScaleType.CENTER_CROP);
    }

    @Override
    public void addTarget(Object ID, UITargetDelegate target) {
        if (interactionLayer != null) interactionLayer.addTarget(ID, target);
    }

    @Override
    public void addAccessoryTarget(Object ID, UITargetDelegate target) {
        if (rightView != null) rightView.addTarget(ID, target);
    }

    @Override
    public void addEditingTarget(Object ID, UITargetDelegate target) {
        if (leftView != null) leftView.addTarget(ID, target);
    }

    //region sizes

    @Override
    public Size estimatedSize() {
        return new Size(Constants.Screen_Size().getWidth(), App.constants().Default_Row_Height());
    }

    public void setHeight(int height, UIView mainView) {

        constraintSet.clear(mainView.getId());

        boolean hasHeight = height > contentView.getPaddingBottom() + contentView.getPaddingTop();
        if (hasHeight) constraintSidesForView(mainView);
        setMinHeight(height);

        constraintHeightForView(mainView, height);
        contentView.applyConstraints();
    }

    public void setAccessorySize(Size size) {
        setRightViewSize(size);
    }

    public void setSeparatorHeight(int separatorHeight) {
        this.separatorHeight = separatorHeight;
        contentView.constraintHeightForView(separator, separatorHeight);
        contentView.applyConstraints();
    }

    protected int separatorHeight() {
        return Dimensions.Int_1();
    }

    public void setSizeForView(Size size, UIView view) {
        setSizeForView(size, view, new UIEdgeInsets());
    }

    public void setSizeForView(Size size, UIView view, UIEdgeInsets insets) {
        setSizeForView(size, view, insets, -1);
    }

    public void setSeparatorColor(UIColor separatorColor) {
        separator.setBackgroundColor(separatorColor);
    }

    public void setSizeForView(Size size, UIView view, int verticalAlignment) {
        setSizeForView(size, view, new UIEdgeInsets(), verticalAlignment);
    }

    public void setSizeForView(Size size, UIView view, UIEdgeInsets insets, int verticalAlignment) {

        if (!ViewUtility.isChildView(contentView, view)) return;

        boolean hasHeight = this.getHeight() == 0 || (this.getHeight() > 0 && (size.getHeight() + insets.top + insets.bottom) < this.getHeight());
        if (hasHeight) {
            contentView.constraintSizeForView(view, size);
            switch (verticalAlignment) {
                case ConstraintSet.TOP: {
                    contentView.constraintForView(verticalAlignment, view, insets.top);
                    break;
                }
                case ConstraintSet.BOTTOM: {
                    contentView.constraintForView(verticalAlignment, view, insets.bottom);
                    break;
                }
                default: {
                    contentView.constraintCenterYForView(view);
                    break;
                }
            }
        }
        else {
            contentView.constraintForView(ConstraintSet.TOP, view, insets.top);
            contentView.constraintForView(ConstraintSet.BOTTOM, view, insets.bottom);
            contentView.getConstraintSet().setDimensionRatio(view.getId(), "1:1");
        }
        contentView.applyConstraints();
    }

    //region helpers

    @Override
    public Size rightViewSize() {
        switch (accessoryType) {
            case DETAIL_DISCLOSURE_BUTTON: {
                Size size = App.constants().TableView_Accessory_Size();
                return new Size(size.getWidth()*2, size.getHeight());
            }
            default:
                return App.constants().TableView_Accessory_Size();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !isEnabled();
    }

    //endregion

    //region views

    public A getAccessoryView() {
        return rightView;
    }

    public UIButton getImageView() {
        return leftView;
    }

    public SELECTION_STYLE getSelectionStyle() {
        return selectionStyle;
    }

    public void setSelectionStyle(SELECTION_STYLE selectionStyle) {
        this.selectionStyle = selectionStyle;
    }

    public EDITING_STYLE getEditingStyle() {
        return editingStyle;
    }

    @Override
    public void setEditingStyle(EDITING_STYLE editingStyle) {
        this.editingStyle = editingStyle;
    }

    public S getSeparator() {
        return separator;
    }

    public UIView getContentView() {
        return contentView;
    }

    @Override
    public A getRightView() {
        return super.getRightView();
    }

    @Override
    public UIButton getLeftView() {
        return super.getLeftView();
    }

    public void setAccessoryType(ACCESSORY_TYPE accessoryType) {
        this.accessoryType = accessoryType;
        switch (accessoryType) {
            case CHECKMARK: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Checkmark_Image());
                rightView.setOffImage(App.assets().Checkmark_Image());
            }
            break;
            case DETAIL_BUTTON: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Details_Image());
                rightView.setOffImage(App.assets().Details_Image());
            }
            break;
            case DISCLOSURE_INDICATOR: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Disclosure_Image());
                rightView.setOffImage(App.assets().Disclosure_Image());
            }
            break;
            case DROPDOWN_INDICATOR_EXPANDED: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Down_Chevron_Image());
                rightView.setOffImage(App.assets().Down_Chevron_Image());
            }
            break;
            case DROPDOWN_INDICATOR_COLLAPSED: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Right_Chevron_Image());
                rightView.setOffImage(App.assets().Right_Chevron_Image());
            }
            break;
            case DETAIL_DISCLOSURE_BUTTON: {
                rightView.setGone(false);
                rightView.setOnImage(App.assets().Details_Disclosure_Image());
                rightView.setOffImage(App.assets().Details_Disclosure_Image());
            }
            break;
            case CUSTOM: {
                rightView.setGone(false);
            }
            break;
            default: {
                rightView.setGone(true);
            }
            break;
        }

        rightView.setOn(true);
        setSizeForView(rightViewSize(), rightView);
    }

    public STYLE getStyle() {
        return style;
    }

    public void setStyle(STYLE style) {
        this.style = style;
    }

    public enum STYLE {
        DEFAULT,
        SUBTITLE
    }

    public enum ACCESSORY_TYPE {
        NONE,
        DISCLOSURE_INDICATOR,
        DROPDOWN_INDICATOR_COLLAPSED,
        DROPDOWN_INDICATOR_EXPANDED,
        DETAIL_DISCLOSURE_BUTTON,
        CHECKMARK,
        DETAIL_BUTTON,
        CUSTOM
    }

    public enum SELECTION_STYLE {
        NONE,
        DEFAULT
    }

    public enum EDITING_STYLE {
        NONE,
        DELETE,
        INSERT
    }

    public enum LABEL_TYPE {
        TEXT(0),
        DETAIL_TEXT(1),
        COUNT(2);

        LABEL_TYPE(int value) {
            this.value = value;
        }

        private final int value;

        public int intValue() {
            return this.value;
        }

        private static final Map map = new HashMap<>();

        static {
            for (LABEL_TYPE type : LABEL_TYPE.values()) {
                map.put(type.value, type);
            }
        }

        public static LABEL_TYPE valueOf(int i) {
            return (LABEL_TYPE) map.get(i);
        }
    }

    //endregion

    //region actions

    public void setSelectionAction(Object obj, UITargetDelegate.TouchUp target) {
        interactionLayer.addTouchUpTarget(obj, target);
    }

    public void setSelectionKeyAction(Object obj, UITargetDelegate.KeyUp target) {
        interactionLayer.addKeyUpTarget(obj, target);
    }

    //endregion

    /** onAttachedToWindow and onDetachedFromWindow are called
     * multiple times in recycler view */
    @Override public void viewDidLoad() {
        loaded = true;
    }

    /** onAttachedToWindow and onDetachedFromWindow are called
     * multiple times in recycler view */
    @Override public void unLoadView() {
        loaded = false;
    }


    //region subclass

    /** A basic concrete subclass of UITableViewCell */
    public static class Concrete extends UITableViewCell {

        public Concrete() {
            super();
            init();
        }

        @Override
        public void initView() {
            super.initView();

            getLabel(LABEL_TYPE.TEXT.intValue()).setTextColor(UIColor.black(1.0f));
            getLabel(LABEL_TYPE.DETAIL_TEXT.intValue()).setTextColor(UIColor.gray(1.0f));
            contentView.setBackgroundColor(UIColor.white(1.0f));
        }

        @Override
        protected int leftPadding() {
            return Dimensions.Int_8();
        }

        @Override
        protected int rightPadding() {
            return Dimensions.Int_8();
        }

        @Override
        protected UIEdgeInsets insets() {
            return new UIEdgeInsets(Dimensions.Int_8(), Dimensions.Int_8(), Dimensions.Int_8(), Dimensions.Int_8());
        }
    }

    //endregion

}
