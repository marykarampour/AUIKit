package com.prometheussoftware.auikit.classes;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.utility.DateUtility;

import java.util.Date;

public class DateViewAttributes extends BaseModel {

    public IndexPath indexPath;
    public Date date = new Date();
    public DateUtility.FORMAT format;
    public boolean isFirstDateOfMonth;
    public boolean showDatePicker;
    public boolean clearTitle;
    public boolean canChooseFutureDate;
    public boolean isUTC;
    public boolean isEditable;
    public boolean hidden;
    //public UIDatePickerMode mode;TODO: create


}
