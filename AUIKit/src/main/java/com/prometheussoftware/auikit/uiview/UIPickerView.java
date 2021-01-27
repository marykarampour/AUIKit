package com.prometheussoftware.auikit.uiview;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.widget.NumberPicker;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;

public class UIPickerView <O> extends UISingleView <UIPickerView.NumberPickerView> {

    private ArrayList<O> objects;
    private int style = App.theme().Picker_View_Style();

    public UIPickerView() {
        super();
        init();
    }

    public UIPickerView(int style) {
        super();
        this.style = style;
        init();
    }

    @Override public void initView() {
        super.initView();
        view = new NumberPickerView(getActivity(), style);
    }

    public void setObjects(ArrayList<O> objects) {
        this.objects = objects;
        view.setDisplayedValues(null);

        if (ArrayUtility.isEmpty(objects)) return;

        String[] stringArray = new String[objects.size()];
        for (int i=0; i < objects.size(); i++) {
            stringArray[i] = objects.get(i).toString();
        }

        setMinValue(0);
        setMaxValue(Math.max(stringArray.length - 1, 0));
        view.setDisplayedValues(stringArray);
        view.setWrapSelectorWheel(false);
    }

    public void setMinValue(int minValue) {
        view.setMinValue(minValue);
    }

    public void setMaxValue(int num) {
        view.setMaxValue(num);
    }

    public void setOnValueChangedListener(NumberPicker.OnValueChangeListener onValueChangeListener) {
        view.setOnValueChangedListener(onValueChangeListener);
    }

    public O objectAtIndex(int index) {
        return ArrayUtility.safeGet(objects, index);
    }

    public String titleAtIndex(int index) {
        return StringUtility.nonNull(objectAtIndex(index));
    }

    public void setSelected(int index) {
        view.setValue(index);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    protected class NumberPickerView extends NumberPicker {

        public NumberPickerView(Context context, int style) {
            super(new ContextThemeWrapper(context, style));
        }
    }
}
