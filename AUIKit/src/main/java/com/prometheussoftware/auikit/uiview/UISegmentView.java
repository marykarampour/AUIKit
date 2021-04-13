package com.prometheussoftware.auikit.uiview;

import android.graphics.PointF;
import android.util.Size;
import android.view.Gravity;
import android.view.MotionEvent;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

import java.util.ArrayList;

public class UISegmentView extends UIView {

    public UIColor disabledColor;
    public UIColor disabledTextColor;
    public UIColor selectedColor;
    public UIColor deselectedColor;
    public UIColor selectedTextColor;
    public UIColor deselectedTextColor;
    public UIColor contentBackgroundColor;
    private UIFont font;

    private Size size = new Size(0, 0);
    private int interItemSpacing;
    private int selectedIndex;
    public SelectionDelegate delegate;

    private ArrayList<UILabel> labels;
    private ArrayList<String> titles;
    private UISingleLayerView backView;

    public interface SelectionDelegate {
        void itemPressedAtIndex(int index);
    }

    public UISegmentView() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();
        backView = new UISingleLayerView();
        backView.setViewBackgroundColor(UIColor.clear());
        backView.setUserInteractionEnabled(false, true);
        backView.getView().setUserInteractionEnabled(false, true);
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubView(backView);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        backView.getView().removeAllConstraints();
        backView.getView().constraintHorizontally(labels, interItemSpacing, 0, 0, true);
        backView.getView().applyConstraints();

        constraintCenterXForView(backView);
        constraintCenterYForView(backView);
        constraintSizeForView(backView, size);
        applyConstraints();
    }

    public void setTitles(ArrayList<String> titles, Size size, int interItemSpacing) {
        this.size = size;
        this.interItemSpacing = interItemSpacing;
        setTitles(titles);
    }

    public void setTitles(ArrayList<String> titles, Size size) {
        this.size = size;
        setTitles(titles);
    }

    public void setTitles(ArrayList<String> titles) {

        this.titles = titles;
        labels = new ArrayList<>();
        backView.getView().removeAllViews();

        for (String title : titles) {
            UILabel label = label(title);
            labels.add(label);
            backView.getView().addSubView(label);
        }

        setSelectedIndex(-1);
    }

    private UILabel label(String title) {
        UILabel view = new UILabel();
        view.setText(title);
        view.setFont(font);
        view.setTextColor(disabledTextColor);
        view.setBackgroundColor(disabledColor);
        view.setGravity(Gravity.CENTER);
        view.setNumberOfLines(1);
        view.setUserInteractionEnabled(false, true);
        view.getView().setPadding(0, 0, 0, 0);
        return view;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;

        UILabel selected = null;
        if (0 <= selectedIndex && selectedIndex < labels.size()) {
            selected = labels.get(selectedIndex);
        }

        for (UILabel label : labels) {
            if (selected == null) {
                label.setBackgroundColor(disabledColor);
                label.setTextColor(disabledTextColor);
            }
            else {
                boolean isSelected = label == selected;
                label.setBackgroundColor(isSelected ? selectedColor : deselectedColor);
                label.setTextColor(isSelected ? selectedTextColor : deselectedTextColor);
            }
        }
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedItem(String title) {
        int index = titles.indexOf(title);
        setSelectedIndex(index);
    }

    public String selectedItem() {
        if (0 <= selectedIndex && selectedIndex < titles.size()) {
            return titles.get(selectedIndex);
        }
        return null;
    }

    public boolean touchEnded(MotionEvent event) {

        if (labels.size() == 0) return true;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            //TODO: works in this case, but for general case of more items
            // this needs to account for paddings left/right
            PointF point = new PointF(event.getX(), event.getY());
            int width = getWidth();
            int backWidth = size.getWidth();
            int padding = (int)Math.max(0, (width - backWidth) / 2.0f);
            int itemArea = padding + (backWidth / labels.size());
            setSelectedIndex(((int)point.x) / itemArea);
            if (delegate != null) delegate.itemPressedAtIndex(selectedIndex);
            return false;
        }
        return true;
    }

    public void setFont(UIFont font) {
        this.font = font;
        if (labels == null) return;

        for (UILabel label : labels) {
            label.setFont(font);
        }
    }

    public void setCornerRadius(float radius) {
        backView.setCornerRadius(radius);
    }

    public void setContentBackgroundColor(UIColor contentBackgroundColor) {
        this.contentBackgroundColor = contentBackgroundColor;
        backView.setContentBackgroundColor(contentBackgroundColor);
    }
}
