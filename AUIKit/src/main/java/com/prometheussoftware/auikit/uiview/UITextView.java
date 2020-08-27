package com.prometheussoftware.auikit.uiview;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Size;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.core.widget.TextViewCompat;

import com.prometheussoftware.auikit.baseviews.TextView;
import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.model.Range;
import com.prometheussoftware.auikit.model.Text;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.uiview.protocols.KeyboardEventListener;
import com.prometheussoftware.auikit.utility.DEBUGLOG;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.lang.reflect.Field;

public class UITextView <V extends UIView, W extends UIView> extends UISingleLayerView <UITextView.UITextLayer> implements KeyboardEventListener, TextView.KeyDownDelegate {

    private enum IGNORE_VALIDATION_STATUS {
        NONE,
        IGNORE,
        CUSTOM,
        DONE
    }

    private UITextView self = this;
    private static boolean isFocused;
    private TextViewDelegate delegate;
    private TextChange currentChange;
    private Text.REGEX_FORMAT format;

    /** -1 indicates no limit on number of characters */
    private int maxChars = -1;

    private IGNORE_VALIDATION_STATUS ignoreValidation = IGNORE_VALIDATION_STATUS.NONE;

    protected V leftView;
    protected W rightView;
    protected Size accessorySize = new Size(Dimensions.Int_28(), Dimensions.Int_28());
    protected int accessoryPadding = Dimensions.Int_16();
    protected int horizontalTextPadding = Dimensions.Int_4();
    protected int verticalTextPadding = Dimensions.Int_4();

    private UITextView nextTextView;

    private CharSequence hint;

    private boolean shouldChangeCharacters = true;

    public UITextView() {
        super();
        init();
    }

    @Override
    protected void createView() {
        setView(new UITextLayer());
    }

    public TextView view() {
        return getView().view;
    }

    @Override
    public void initView() {
        super.initView();

        currentChange = new TextChange();

        view().addTextChangedListener(this);
        view().setOnFocusChangeListener(this);
        view().setOnEditorActionListener(this);
        view().setImeOptions(keyboardOption(EditorInfo.IME_ACTION_DONE));
        view().setKeyDownDelegate(this);
        view().setPadding(horizontalTextPadding, verticalTextPadding, horizontalTextPadding, verticalTextPadding);
        view().setOnClickListener((v) -> {
            if (!isFocused) {
                setFocused(true);
            }
        });

        setFocusable(true);
        setFocusableInTouchMode(true);
    }

    public void setTextAppearance (int style, int textColor, int placeholderColor) {
        TextViewCompat.setTextAppearance(view(), style);
        view().setTextColor(textColor);
        view().setHintTextColor(placeholderColor);
    }

    public static <V extends UIView, W extends UIView> UITextView editText(@Nullable V leftView, @Nullable W rightView, Size accessorySize) {

        UITextView view = new UITextView();
        view.accessorySize = accessorySize;
        view.setLeftView(leftView);
        view.setRightView(rightView);
        return view;
    }

    public void setNextTextView(UITextView nextTextView) {
        this.nextTextView = nextTextView;
        setKeyboardOption(nextTextView == null ? EditorInfo.IME_ACTION_DONE : EditorInfo.IME_ACTION_NEXT);
    }

    public UITextView getNextTextView() {
        return nextTextView;
    }

    public void setKeyboardOption(int option) {
        view().setImeOptions(keyboardOption(option));
    }

    public void setLeftView(V leftView) {
        this.leftView = leftView;
        if (leftView != null) {
            addView(leftView);

            if (leftView != null) {
                constraintCenterYForView(leftView);
                constraintSizeForView(leftView, accessorySize);
                constraintForView(ConstraintSet.START, leftView, accessoryPadding);
            }
            applyConstraints();
        }
    }

    public void setRightView(W rightView) {
        this.rightView = rightView;
        if (rightView != null) {
            addView(rightView);

            if (rightView != null) {
                constraintCenterYForView(rightView);
                constraintSizeForView(rightView, accessorySize);
                constraintForView(ConstraintSet.END, rightView, accessoryPadding);
            }
            applyConstraints();
        }
    }

    public void setDelegate(TextViewDelegate delegate) {
        this.delegate = delegate;
    }

    public TextView getEditText() {
        return view();
    }

    public String getText () {
        return view().getText().toString();
    }

    public V getLeftView() {
        return leftView;
    }

    public W getRightView() {
        return rightView;
    }

    public void setText(CharSequence s) {
        view().setText(s);
        view().setHint(StringUtility.isEmpty(s) ? hint : "");
    }

    public void setHint(CharSequence s) {
        this.hint = s;
        view().setHint(StringUtility.isEmpty(getText()) ? hint : "");
    }

    /** Call this method to set text ignoring regex validation */
    public void setTextWithoutValidation (String text) {
        ignoreValidation = IGNORE_VALIDATION_STATUS.IGNORE;
        setText(text);
    }

    public void setTextWithCustomValidation (String text) {
        ignoreValidation = IGNORE_VALIDATION_STATUS.CUSTOM;
        setText(text);
    }

    public void setIgnoreValidation(IGNORE_VALIDATION_STATUS ignoreValidation) {
        this.ignoreValidation = ignoreValidation;
    }

    public void setFormat(Text.REGEX_FORMAT format) {
        this.format = format;
        switch (format) {
            case INT:
            case INT_POSITIVE: {
                view().setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            break;
            case PASSWORD: {
                view().setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            break;
            case PHONE: {
                view().setInputType(InputType.TYPE_CLASS_PHONE);
            }
            break;
            case EMAIL: {
                view().setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            }
            break;
            case ALPHABET:
            case ALPHANUMERIC:
            default: {
                view().setInputType(InputType.TYPE_CLASS_TEXT);
            }
            break;
        }
    }

    public Text.REGEX_FORMAT getFormat() {
        return format;
    }

    public void setMaxChars(int maxChars) {
        this.maxChars = maxChars;
    }

    public int getMaxChars() {
        return maxChars;
    }

    @Override
    public boolean isFocused() {
        return isFocused;
    }

    public void setFocused(boolean focused) {
        isFocused = focused;

        if (focused) {
            dispatchTextViewDidBeginEditing();
        }
        else {
            dispatchTextViewDidEndEditing();
        }
    }

    //region helpers

    public void hideKeyboard () {
        getWindow().dismissKeyboard();
    }

    private void dispatchTextViewDidBeginEditing() {
        if (delegate != null && getWindow() != null)
            runOnUiThread(() -> {
                delegate.TextViewDidBeginEditing(self);
                view().setSelection(getText().length());
            });
    }

    private void dispatchTextViewDidEndEditing() {
        if (delegate != null && getWindow() != null)
            runOnUiThread(() -> delegate.TextViewDidEndEditing(self));
    }

    private boolean dispatchTextViewShouldChangeCharactersInRange() {
        if (delegate != null && getWindow() != null) {
            boolean hasMethod = delegate.implementsTextViewDidChangeCharactersInRange();
            if (hasMethod) {
                return delegate.TextViewShouldChangeCharactersInRange(self, currentChange.range, currentChange.text.toString());
            }
        }
        return canAddToCharCount();
    }

    private void dispatchTextViewDidChangeCharactersInRange() {
        if (delegate != null && getWindow() != null)
            runOnUiThread(() -> delegate.TextViewDidChangeCharactersInRange(self, currentChange.range));
    }

    private boolean dispatchTextViewShouldReturn() {
        if (delegate != null && getWindow() != null)
            return delegate.TextViewShouldReturn(self);
        return true;
    }

    private void dispatchTextViewDidReturn() {
        if (delegate != null && getWindow() != null)
            runOnUiThread(() -> delegate.TextViewDidReturn(self));
    }

    private boolean canAddToCharCount() {
        return maxChars == -1 || (0 <= maxChars && view().length() <= maxChars);
    }

    //endregion

    //region events

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (EditText.class.isInstance(v)) {
            if (v == view()) {
                setFocused(hasFocus);
                currentChange = new TextChange();
            }
            else {
                setFocused(false);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (!isFocused) return;

        currentChange.range = new Range(start + Math.min(count, after), Math.abs(after - count));
        currentChange.changeCount = after - count;
        currentChange.currentText = view().getText().toString();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!isFocused) return;

        if (ignoreValidation != IGNORE_VALIDATION_STATUS.IGNORE) {
            shouldChangeCharacters = dispatchTextViewShouldChangeCharactersInRange();
            if (shouldChangeCharacters) {
                currentChange.text = s;
                currentChange.currentText = view().getText().toString();
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!isFocused) return;
        switch (ignoreValidation) {
            case NONE: {
                if (shouldChangeCharacters) {
                    if (!StringUtility.hasValidCharacers(s.toString(), format, maxChars)) {
                        setTextWithoutValidation(currentChange.currentText);
                    }
                    else {
                        ignoreValidation = IGNORE_VALIDATION_STATUS.DONE;
                    }
                }
                else if (maxChars < view().length()) {
                    ignoreValidation = IGNORE_VALIDATION_STATUS.DONE;
                    view().setText(currentChange.currentText);
                }
                view().setSelection(currentChange.cursorLocation());
            }
            break;
            case IGNORE: {
                ignoreValidation = IGNORE_VALIDATION_STATUS.DONE;
            }
            break;
            case DONE: {
                ignoreValidation = IGNORE_VALIDATION_STATUS.NONE;
                if (!shouldChangeCharacters) {
                    view().setText(currentChange.currentText);
                }
            }
            break;
        }
    }

    @Override
    public boolean onEditorAction(android.widget.TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_NEXT: {
                if (nextTextView != null) {
                    view().clearFocus();
                    nextTextView.getEditText().requestFocus();
                    return true;
                }
            }
            case KeyEvent.KEYCODE_ENTER:
            case KeyEvent.KEYCODE_NUMPAD_ENTER:
            case EditorInfo.IME_ACTION_DONE: {
                view().clearFocus();
                hideKeyboard();

                boolean shouldReturn = dispatchTextViewShouldReturn();
                if (shouldReturn) {
                    dispatchTextViewDidReturn();
                }
                return !shouldReturn;
            }
            default: return false;
        }
    }

    @Override
    public void keyDownPressed() {
        setFocused(false);
    }

    public interface TextViewDelegate {

        // return NO to disallow editing.
        default boolean TextViewShouldBeginEditing (UITextView TextView) {
            return true;
        }
        // became first responder
        void TextViewDidBeginEditing (UITextView TextView);
        // return YES to allow editing to stop and to resign first responder status. NO to disallow the editing session to end
        default boolean TextViewShouldEndEditing (UITextView TextView) {
            return true;
        }
        // may be called if forced even if shouldEndEditing returns NO (e.g. view removed from window) or endEditing:YES called
        void TextViewDidEndEditing (UITextView TextView);
        // return NO to not change text
        default boolean TextViewShouldChangeCharactersInRange (UITextView TextView, Range range, String replacementString) {
            if (TextView != null && TextView.delegate != null && TextView.delegate.implementsTextViewDidChangeCharactersInRange()) {
                return TextView.delegate.TextViewShouldChangeCharactersInRange(TextView, range, replacementString);
            }
            return StringUtility.hasValidCharacers(TextView.getText(), TextView.getFormat(), TextView.getMaxChars());
        }
        // called when text is changed
        default void TextViewDidChangeCharactersInRange (UITextView TextView, Range range) { }
        // called when clear button pressed. return NO to ignore (no notifications)
        default boolean TextViewShouldClear (UITextView TextView) {
            return true;
        }
        // called when 'return' key pressed. return NO to ignore.
        default boolean TextViewShouldReturn (UITextView TextView) {
            return TextView.view().getMaxLines() == 1 && TextView.view().getMinLines() == 1 && TextView.nextTextView == null;
        }

        default void TextViewDidReturn (UITextView TextView) {
            TextView.hideKeyboard();
        }

        /** If your class implements the default method, this need to be implemented as well and return true
         * Class declaringClass = delegate.getClass().
         * getDeclaredMethod("TextViewShouldChangeCharactersInRange",
         * UITextView.class, Range.class, String.class).getDeclaringClass();
         * Will not work, because the defaults are regarded as declared methods, even if they are not implemented in the class
         * */
        default boolean implementsTextViewDidChangeCharactersInRange() {
            return false;
        }
    }

    public static class TextChange {
        protected CharSequence text;
        protected String currentText;
        protected Range range = new Range();
        protected int changeCount;

        protected int cursorLocation() {
            if (0 <= changeCount) {
                return range.location + changeCount;
            }
            return range.location;
        }
    }

    //endregion

    //region utility

    //This is not a great idea, but the only existing solution
    //https://stackoverflow.com/questions/57360338/edittext-handle-color-for-28p-with-29q-as-target-api
    public void setCursorColor(UIColor color) {
        try {
            TextView editText = view();

            // Get the editor
            Field field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(editText);

            if (color != null) {
                editText.setHighlightColor(color.get());

                // Get the cursor drawable, tint it, and set it on the TextView Editor
                // Get the cursor resource id
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ColorFilter colorFilter = new BlendModeColorFilter(color.get(), BlendMode.SRC_ATOP);
                    editText.getTextCursorDrawable().mutate().setColorFilter(colorFilter);
                    editText.getTextSelectHandle().mutate().setColorFilter(colorFilter);
                    editText.getTextSelectHandleLeft().mutate().setColorFilter(colorFilter);
                    editText.getTextSelectHandleRight().mutate().setColorFilter(colorFilter);
                }
                else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    try {
                        Field fieldP = TextView.class.getDeclaredField("mCursorDrawableRes");
                        fieldP.setAccessible(true);
                        int drawableResId = fieldP.getInt(editText);

                        // Get the editor
                        fieldP = TextView.class.getDeclaredField("mEditor");
                        fieldP.setAccessible(true);
                        Object editorP = fieldP.get(editText);

                        // Get the drawable and set a color filter
                        Drawable drawable = ContextCompat.getDrawable(getWindow(), drawableResId);
                        drawable.setColorFilter(color.get(), PorterDuff.Mode.SRC_ATOP);

                        // Set the drawables
                        fieldP = editorP.getClass().getDeclaredField("mDrawableForCursor");
                        fieldP.setAccessible(true);
                        fieldP.set(editorP, drawable);
                    } catch (final Exception e) {
                        DEBUGLOG.s(e);
                    }
                }
            }
        } catch (Exception e) {
            DEBUGLOG.s(e, this);
        }
    }
    
    private int keyboardOption(int option) {
        return option | EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN;
    }

    public void setAccessorySize(Size accessorySize) {
        this.accessorySize = accessorySize;
    }

    public void setAccessoryPadding(int accessoryPadding) {
        this.accessoryPadding = accessoryPadding;
    }

    public void setSecure() {
        view().setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        view().setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void setGravity(int gravity) {
        view().setGravity(gravity);
    }

    //endregion

    //region layer

    public static class UITextLayer extends UISingleView <TextView> {

        public UITextLayer() {
            super();
            init();
        }

        @Override public void initView() {
            super.initView();
            view = new TextView(getWindow());
            view.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            view.setBackground(null);
            view.setSingleLine(true);
            view.setFocusable(false);
            view.setFocusableInTouchMode(true);
        }
    }

    //endregion
}
