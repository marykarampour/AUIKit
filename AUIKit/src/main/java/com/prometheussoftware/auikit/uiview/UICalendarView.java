package com.prometheussoftware.auikit.uiview;

import android.widget.CalendarView;

import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.uiview.protocols.UIControlProtocol;
import com.prometheussoftware.auikit.utility.ArrayUtility;

public class UICalendarView extends UIView implements UIControlProtocol {

    protected UISingleView<CalendarView> calendar;
    protected UIButton button;

    public UICalendarView() {
        super();
        init();
    }

    @Override
    public void initView() {
        super.initView();
        button = new UIButton();
        calendar = new UISingleView(new CalendarView(getActivity()));
    }

    @Override
    public void loadView() {
        super.loadView();
        addSubview(button);
        addSubview(calendar);
    }

    @Override
    public void constraintLayout() {
        super.constraintLayout();

        constraintHeightForView(button, App.constants().Default_Row_Height());
        constraintWidthForView(calendar, Constants.Screen_Size().getWidth());
        constraintHeightForView(calendar, calendarSize());
        constraintVertically(ArrayUtility.arrayOf(button, calendar), 0, false);
        applyConstraints();
    }

    protected int calendarSize() {
        return Dimensions.Int_256();
    }

    public int estimatedHeight() {
        return App.constants().Default_Row_Height() + calendarSize();
    }

    @Override
    public void addTarget(Object ID, UITargetDelegate target) {
        button.addTarget(ID, target);
    }
}
