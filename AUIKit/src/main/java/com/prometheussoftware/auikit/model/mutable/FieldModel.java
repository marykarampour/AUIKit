package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Range;
import com.prometheussoftware.auikit.uiview.UITextField;
import com.prometheussoftware.auikit.uiview.UITextView;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.utility.DateUtility;
import com.prometheussoftware.auikit.utility.MapUtility;
import com.prometheussoftware.auikit.utility.NumberUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FieldModel extends BaseModel implements MutableProtocol.Field {

    MutableProtocol.Delegate updateDelegate;

    @Override
    public <O extends ViewContentProtocol.Placeholder> List<O> arrayForObjectType(int type) { return null; }

    @Override
    public <O extends ViewContentProtocol.Placeholder> List<O> arrayForSectionType (int type) { return null; }

    NumberUtility.STYLE numberStyleForObjectType (int type) {
        StringUtility.TYPE textType = textTypeForObjectType(type);
        switch (textType) {
            case FLOAT:
            case FLOAT_POSITIVE:
                return NumberUtility.STYLE.DECIMAL;
            default:
                return NumberUtility.STYLE.NONE;
        }
    }

    String textForObjectType (String text , int type) {
        return isUppercaseStringObjectType(type) ? text.toUpperCase() : text;
    }

    //Field
    @Override
    public Map<Integer, String> propertyEnumDictionary() {
        return Collections.emptyMap();
    }

    @Override
    public Map<Integer, Integer> sectionEnumDictionary() {
        ArrayList<Integer> keys = new ArrayList<>(propertyEnumDictionary().keySet());
        return MapUtility.mapWithObjectForKeys(keys, keys);
    }

    @Override
    public Map<Integer, String> titleEnumDictionary() {
        return propertyEnumDictionary();
    }

    @Override
    public boolean hasValueForObjectType(int type) {
        return valueForObjectType(type) != null;
    }

    @Override
    public boolean hasValueForSectionType(int section) {
        if (isEditableSectionType(section)) return true;

        Set<Integer> types = objectTypesForSectionType(section);
        for (Integer type : types) {
            if (hasValueForObjectType(type)) return true;
        }
        return false;
    }

    @Override
    public boolean boolValueForObjectType(int type) {
        Object obj = valueForObjectType(type);
        if (obj instanceof Boolean) return (Boolean)obj;
        return obj != null;
    }

    @Override
    public boolean boolValueForSectionType(int section) {
        Set<Integer> types = objectTypesForSectionType(section);
        for (Integer type : types) {
            Object obj = valueForObjectType(type);
            if (obj instanceof Boolean) return (Boolean)obj;
        }
        return valuesForSectionType(section).size() != 0;
    }

    @Override
    public Date dateValueForObjectType(int type) {
        Object obj = valueForObjectType(type);
        if (obj instanceof Date) return (Date)obj;
        return null;
    }

    @Override
    public Date dateValueForSectionType(int section) {
        Set<Integer> types = objectTypesForSectionType(section);
        for (Integer type : types) {
            Object obj = valueForObjectType(type);
            if (obj instanceof Date) return (Date)obj;
        }
        return null;
    }

    @Override
    public DateUtility.FORMAT dateFormatForObjectType(int type) {
        return DateUtility.FORMAT.DAY_TIME_STYLE;
    }

    @Override
    public DateUtility.FORMAT dateFormatForSectionType(int section) {
        return DateUtility.FORMAT.DAY_TIME_STYLE;
    }

    @Override
    public StringUtility.TYPE textTypeForObjectType(int type) {
        return StringUtility.TYPE.NONE;
    }

    @Override
    public boolean shouldValidateWhenEditingObjectType(int type) {
        return false;
    }

    @Override
    public Number numberValueForObjectType(int type) {
        Object obj = valueForObjectType(type);
        if (obj instanceof Number) return (Number)obj;
        return null;
    }

    @Override
    public Number numberValueForSectionType(int section) {
        Set<Integer> types = objectTypesForSectionType(section);
        for (Integer type : types) {
            Object obj = valueForObjectType(type);
            if (obj instanceof Number) return (Number)obj;
        }
        return null;
    }

    @Override
    public Object valueForObjectType(int type) {
        String key = propertyEnumDictionary().get(type);
        if (!BaseModel.hasMethod(getClass(), key, true))
            return null;
        return valueForKey(key);
    }

    @Override
    public Set valuesForSectionType(int section) {
        Set arr = new HashSet();
        Set<Integer> types = objectTypesForSectionType(section);
        for (Integer type : types) {
            Object obj = valueForObjectType(type);
            if (obj != null) arr.add(obj);
        }
        return arr;
    }

    @Override
    public String titleForObjectType(int type) {
        String key = propertyEnumDictionary().get(type);
        return StringUtility.splitStringForUppercaseComponents(key.toUpperCase(), true);
    }

    @Override
    public String titleForSectionType(int section) {
        String key = titleEnumDictionary().get(section);
        return StringUtility.splitStringForUppercaseComponents(key.toUpperCase(), true);
    }

    @Override
    public Set<Integer> typesForSection(int section) {
        return MapUtility.allKeysForObject(sectionEnumDictionary(), section);
    }

    @Override
    public String stringValueForObjectType(int type) {
        Object object = valueForObjectType(type);

        if (object == null) return null;
        if (object instanceof String) return (String)object;
        if (object instanceof Date)
            return localDateStringWithDateForObjectType((Date)object, type);
        if (object instanceof Number)
            return NumberUtility.stringValueWithStyle((Number)object, numberStyleForObjectType(type), floatingDigits());
        return object.toString();
    }

    @Override
    public String stringValueForSectionType(int section) {

        Set values = valuesForSectionType(section);
        Object str = values.stream().anyMatch(o -> o instanceof String);

        if (str != null) return str.toString();

        Set<Integer> types = objectTypesForSectionType(section);
        for (Integer type : types) {
            String obj = stringValueForObjectType(type);
            if (obj != null) return obj;
        }

        return values.stream().findAny().toString();
    }

    @Override
    public Set objectTypesForSectionType(int section) {
        return MapUtility.allKeysForObject(sectionEnumDictionary(), section);
    }

    @Override
    public String badgeValueForSectionType(int section) {
        return "";
    }

    @Override
    public int floatingDigits() {
        return 2;
    }

    @Override
    public String localDateStringWithDate(Date date) {
        return DateUtility.dateStringWithFormat(date, DateUtility.FORMAT.DAY_TIME_STYLE.getName());
    }

    @Override
    public String localDateStringWithDateForObjectType(Date date, int type) {
        return DateUtility.dateStringWithFormat(date, dateFormatForObjectType(type).getName());
    }

    @Override
    public String localDateStringForObjectType(int type) {
        Object object = valueForObjectType(type);
        if (object instanceof Date)
            return localDateStringWithDateForObjectType((Date)object, type);
        return "";
    }

    @Override
    public void setValueForObjectType(Object value, int type) {
        String key = propertyEnumDictionary().get(type);
        if (BaseModel.hasMethod(getClass(), key, true))
            setValueForKeyForAllAccessLevels(value, key);
    }

    @Override
    public void setValueForSectionType(Object value, int section) {
        Set<Integer> types = objectTypesForSectionType(section);

        for (Integer type : types) {
            String key = propertyEnumDictionary().get(type);
            Class cls = BaseModel.classOfPropertyForObjectClass(key, getClass());
            if (cls.isAssignableFrom(value.getClass())) {
                setValueForObjectType(value, type);
                return;
            }
        }
    }

    @Override
    public void switchBoolValueForObjectType(int type) {
        String key = propertyEnumDictionary().get(type);
        Object value = valueForKey(key);

        if (value instanceof Boolean)
            setValueForKey(!(Boolean) value, key);
    }

    @Override
    public void switchBoolValueForSectionType(int section) {
        Set<Integer> types = objectTypesForSectionType(section);

        for (Integer type : types) {
            switchBoolValueForObjectType(type);
        }
    }

    @Override
    public boolean isLongValueForObjectType(int type) {
        return App.constants().MaxValue1CellCharacterCount() <= stringValueForObjectType(type).length();
    }

    @Override
    public boolean isLongValueForSectionType(int section) {
        Set<Integer> types = objectTypesForSectionType(section);

        for (Integer type : types) {
            if (isLongValueForObjectType(type))
                return true;
        }
        return false;
    }

    @Override
    public boolean isEditableSectionType(int section) {
        return true;
    }

    @Override
    public boolean isCommentSectionType(int section) {
        return false;
    }

    @Override
    public boolean isUppercaseStringObjectType(int type) {
        return false;
    }

    @Override
    public boolean isEmailSectionType(int section) {
        return false;
    }

    @Override
    public boolean isPhoneSectionType(int section) {
        return false;
    }

    @Override
    public String missingValueErrorMessage() {
        return "";
    }

    @Override
    public String missingObjectErrorMessage() {
        return "";
    }

    @Override
    public void setUpdateDelegate(MutableProtocol.Delegate obj) {
        updateDelegate = obj;
    }

    @Override
    public MutableProtocol.Delegate UpdateDelegate() {
        return updateDelegate;
    }

    /** @brief Call this method to send a message to updateDelegate that a value is updated.
    Useful in cases custom calculations require a view update. */
    public void dispatchUpdateDelegateWithObjectType(int type) {
        if (UpdateDelegate() == null) return;
        UpdateDelegate().objectDidUpdateObjectType(this, type);
    }

    /**
     * @brief Call this method to send a message to updateDelegate that a value is updated.
     * Useful in cases custom calculations require a view update. */
    public void dispatchUpdateDelegateWithObjectType(int type, UITextField textField, boolean endEditing, IndexPath indexPath) {
        if (UpdateDelegate() == null) return;
        UpdateDelegate().objectDidUpdateObjectType(this, type, textField, endEditing, indexPath);
    }

    //endregion


    //region Text

    void handleTextFieldUpdates (UITextField textField, String newText, boolean setTextField, boolean endEditing) {

        IndexPath indexPath = textField.getIndexPath();
        String text = 0 < newText.length() ? newText : textField.getText();
        text = textForObjectType(text, indexPath.row);
        Integer type = indexPath.row;
        Object value = text;
        String name = propertyEnumDictionary().get(type);
        Class cls = classOfPropertyForObjectClass(name, getClass());

        if (Number.class.isAssignableFrom(cls))
            value = StringUtility.numValue(text);

        setValueForObjectType(value, type);

        if (setTextField)
            textField.setText(endEditing || shouldValidateWhenEditingObjectType(type) ? stringValueForObjectType(type) : text);

        dispatchUpdateDelegateWithObjectType(type, textField, endEditing, indexPath);
    }

    @Override
    public void textViewDidBeginEditing(UITextView textView) {}

    @Override
    public void textViewDidEndEditing(UITextView textView) {
        if (textView instanceof UITextField) {
            handleTextFieldUpdates((UITextField) textView, null, true, true);
        }
        else {
            IndexPath indexPath = textView.getIndexPath();
            textView.setText(textForObjectType(textView.getText(), indexPath.row));
            setValueForObjectType(textView.getText(), indexPath.row);
        }
    }

    @Override
    public void textViewDidChangeCharactersInRange(UITextView textView, Range range) {
        if (textView instanceof UITextField) {
            handleTextFieldUpdates((UITextField) textView, textView.getText().substring(range.location, range.location+range.length), true, false);
        }
    }

    @Override
    public void textViewDidReturn(UITextView textView) {}

    @Override
    public boolean implementsTextViewDidChangeCharactersInRange() {
        return true;
    }

    //endregion

}
