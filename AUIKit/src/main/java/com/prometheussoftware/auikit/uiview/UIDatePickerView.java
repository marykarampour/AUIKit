package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.classes.DateViewAttributes;
import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.utility.DateUtility;

public class UIDatePickerView extends UICalendarView {

    public DateViewAttributes info;
    public Delegate delegate;
    private boolean expanded = false;

    public UIDatePickerView() {
        super();

        animationDuration = 50;
        info = new DateViewAttributes();
        info.format = DateUtility.FORMAT.FULL_STYLE;
    }

    @Override
    public void initView() {
        super.initView();

        button.setTarget((sender) -> setExpanded(!expanded));
        button.setBackgroundColor(UIColor.green(0.2f));
        button.setTextColor(UIColor.black(0.9f));
        button.setText("No Date Selected");

        calendar.view.setOnDateChangeListener((calendarView, y, m, d) -> {
            info.date = DateUtility.dateWithComponents(y, m, d);
            String date = DateUtility.dateStringWithFormat(info.date, info.format);
            button.setText(date);
        });
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
        setExpanded(expanded, calendar, 0, calendarSize(), true, null);
        if (delegate != null) delegate.didSetDatePickerHidden(this, !expanded);
    }

    @Override
    public int estimatedHeight() {
        return expanded ? super.estimatedHeight() : App.constants().Default_Row_Height();
    }

    public interface Delegate {
        default void didSetDatePickerHidden (UIDatePickerView view, boolean hidden) {}
    }
}
