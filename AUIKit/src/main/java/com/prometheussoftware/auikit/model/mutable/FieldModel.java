package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.model.ArrayPropertyProtocol;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Range;
import com.prometheussoftware.auikit.uiview.UITextField;
import com.prometheussoftware.auikit.uiview.UITextView;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.Set;

public class FieldModel extends BaseModel implements MutableProtocol.Field, ArrayPropertyProtocol {

    transient
    MutableProtocol.Delegate updateDelegate;

    static {
        BaseModel.Register(FieldModel.class);
    }

    String textForObjectType (String text , int type) {
        return isUppercaseStringObjectType(type) ? text.toUpperCase() : text;
    }

    @Override
    public ArrayList array() {
        return null;
    }

    @Override
    public void setArray(ArrayList array) {}

    //Field
    @Override
    public Object valueForObjectType(int type) {
        String key = propertyEnumDictionary().get(type);
        return valueForKey(key);
    }

    @Override
    public void setValueForObjectType(Object value, int type) {
        String key = propertyEnumDictionary().get(type);
        setValueForKeyForAllAccessLevels(value, key);
    }

    @Override
    public void setValueForSectionType(Object value, int section) {
        Set<Integer> types = objectTypesForSectionType(section);

        for (Integer type : types) {
            String key = propertyEnumDictionary().get(type);
            for (Class cls : BaseModel.classOfPropertyForObjectClass(key, getClass())) {
                if (cls != null && cls.isAssignableFrom(value.getClass())) {
                    setValueForObjectType(value, type);
                    return;
                }
                else if (ArrayPropertyProtocol.class.isAssignableFrom(cls) && value instanceof ArrayList) {
                    ArrayPropertyProtocol obj = (ArrayPropertyProtocol) valueForObjectType(type);
                    if (obj != null) obj.setArray((ArrayList) value);
                }
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
        if (indexPath == null) return;

        String text = StringUtility.isNotEmpty(newText) ? newText : textField.getText();
        text = textForObjectType(text, indexPath.row);
        Integer type = indexPath.row;
        Object value = text;
        String name = propertyEnumDictionary().get(type);

        for (Class cls : classOfPropertyForObjectClass(name, getClass())) {
            if (Number.class.isAssignableFrom(cls))
                value = StringUtility.numValue(text);
        }

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

    @Override
    public boolean textViewShouldChangeCharactersInRange(UITextView textView, Range range, String replacementString) {
        return true;
    }

    //endregion

}
