package com.prometheussoftware.auikit.classes;

import static android.view.View.TEXT_ALIGNMENT_CENTER;
import static android.view.View.TEXT_ALIGNMENT_TEXT_START;

import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.utility.StringFormatting;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LabelAttributes {

    protected String title;
    protected String subtitle;
    protected String value;
    protected String subvalue;
    protected String placeholder;
    protected SpannableStringBuilder attrValue;
    protected SpannableStringBuilder attrSubvalue;
    protected String labelDelimiter;
    protected String sublabelDelimiter;

    private TYPE type;

    public void setAttributedTitlesForLabel (UILabel label,UILabel sublabel) {
        if (label != null) label.setText(null);
        if (sublabel != null) sublabel.setText(null);
    }

    public int heightForWidth (int width) {
        return 0;
    }

    protected int heightForWidth (int width, String text) {
        return StringUtility.height(text, 0, width);
    }

    protected LabelAttributes() {
    }

    public static LabelAttributes init(String title, String subtitle, String value, String subvalue, String placeholder, SpannableStringBuilder attrValue, SpannableStringBuilder attrSubvalue) {
        return init(title, subtitle, value, subvalue, placeholder, attrValue, attrSubvalue, ": ", ": ");
    }

    public static LabelAttributes init(String title, String subtitle, String value, String subvalue, String placeholder, SpannableStringBuilder attrValue, SpannableStringBuilder attrSubvalue, String labelDelimiter, String sublabelDelimiter) {

        TYPE type = TYPE.NONE;
        LabelAttributes obj;

        if ((StringUtility.isNotEmpty(placeholder) || StringUtility.isNotEmpty(title)) && StringUtility.isEmpty(value) && StringUtility.isEmpty(subvalue) && StringUtility.isEmpty(attrValue) && StringUtility.isEmpty(attrSubvalue)) {
            type = 0 < placeholder.length() ? TYPE.PLACEHOLDER : type.TITLE;
        }
        else {
            if (0 < value.length()) {
                if (0 < title.length()) {
                    type = TYPE.ATTR_VALUE;
                }
                else {
                    type = TYPE.VALUE;
                }
            }
            else if (attrValue != null) {
                type = TYPE.ATTR_VALUE;
            }
            else {
                type = TYPE.NONE;
            }

            if (0 < subvalue.length()) {
                if (0 < subtitle.length()) {
                    type.value = type.value | TYPE.ATTR_SUBVALUE.value;
                }
                else {
                    type.value = type.value | TYPE.SUBVALUE.value;
                }
            }
            else if (attrSubvalue != null) {
                type.value = type.value | TYPE.ATTR_SUBVALUE.value;
            }
            else {
                type.value = type.value | TYPE.NONE.value;
            }
        }

        if ((type.value & TYPE.TITLE.value) != 0) {
            obj = new LabelAttributes_0();
        }
        else if ((type.value & TYPE.PLACEHOLDER.value) != 0) {
            obj = new LabelAttributes_1();
        }
        else if ((type.value & TYPE.VALUE.value) != 0) {
            obj = LabelAttributes_2.init(type);
        }
        else if ((type.value & TYPE.ATTR_VALUE.value) != 0) {
            obj = LabelAttributes_3.init(type);
        }
        else if ((type.value & TYPE.SUBVALUE.value) != 0) {
            obj = new LabelAttributes_4();
        }
        else if ((type.value & TYPE.ATTR_SUBVALUE.value) != 0) {
            obj = new LabelAttributes_5();
        }
        else {
            obj = new LabelAttributes();
        }

        obj.type = type;
        obj.setTitle(title, subtitle, value, subvalue, placeholder, attrValue, attrSubvalue, labelDelimiter, sublabelDelimiter);

        return obj;
    }

    public static void setAttributedTitles (LabelAttributes obj, UILabel label, UILabel sublabel) {
        obj.setAttributedTitlesForLabel(label, sublabel);
    }

    protected void setTitle (String title, String subtitle, String value, String subvalue, String placeholder, SpannableStringBuilder attrValue, SpannableStringBuilder attrSubvalue, String labelDelimiter, String sublabelDelimiter) {

        this.title = title;
        this.subtitle = subtitle;
        this.value = value;
        this.subvalue = subvalue;
        this.placeholder = placeholder;
        this.attrValue = attrValue;
        this.attrSubvalue = attrSubvalue;
        this.labelDelimiter = labelDelimiter;
        this.sublabelDelimiter = sublabelDelimiter;

        if (this.attrValue == null && ((this.type.value & TYPE.ATTR_VALUE.value) != 0)) {
            ArrayList<StringFormatting.StringAttributes> attrs = new ArrayList();
            attrs.add(new StringFormatting.StringAttributes(this.title, App.theme().Medium_Bold_Font(), UIColor.black(1.0f)));
            attrs.add(new StringFormatting.StringAttributes(this.value, App.theme().Medium_Regular_Font(), UIColor.gray(1.0f)));

            this.attrValue = StringFormatting.attributedText(attrs, this.labelDelimiter);
        }
        if (this.attrSubvalue == null && ((this.type.value & TYPE.ATTR_SUBVALUE.value) != 0)) {
            ArrayList<StringFormatting.StringAttributes> attrs = new ArrayList();
            attrs.add(new StringFormatting.StringAttributes(this.subtitle, App.theme().Small_Bold_Font(), App.theme().Text_Dark_Color()));
            attrs.add(new StringFormatting.StringAttributes(this.subvalue, App.theme().Small_Regular_Font(), App.theme().Bright_Blue_Color()));

            this.attrValue = StringFormatting.attributedText(attrs, this.sublabelDelimiter);
        }
    }

    static class LabelAttributes_0 extends LabelAttributes {

        public LabelAttributes_0() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return heightForWidth(width, title) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            if (label != null) {
                label.setFont(App.theme().Medium_Bold_Font());
                label.setText(title);
                label.setTextAlignment(TEXT_ALIGNMENT_CENTER);
            }
            if (sublabel != null) sublabel.setText(null);
        }
    }

    static class LabelAttributes_1 extends LabelAttributes {

        public LabelAttributes_1() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return heightForWidth(width, placeholder) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            if (label != null) {
                label.setFont(App.theme().Medium_Bold_Font());
                label.setText(placeholder);
                label.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }
            if (sublabel != null) sublabel.setText(null);
        }
    }

    static class LabelAttributes_2 extends LabelAttributes {

        public LabelAttributes_2() {
            super();
        }

        protected static LabelAttributes_2 init(TYPE type) {
            switch (type) {
                case SUBVALUE:
                    return new LabelAttributes_21();
                case ATTR_SUBVALUE:
                    return new LabelAttributes_22();
                default:
                    return new LabelAttributes_2();
            }
        }

        @Override
        public int heightForWidth(int width) {
            return heightForWidth(width, value) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            if (label != null) {
                label.setText(value);
                label.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }
        }
    }

    static class LabelAttributes_21 extends LabelAttributes_2 {

        public LabelAttributes_21() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return super.heightForWidth(width) + heightForWidth(width, subvalue) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
//            SpannableStringBuilder attrs = StringFormatting.attributedTitleSubtitle(this)
            super.setAttributedTitlesForLabel(label, sublabel);
            if (sublabel != null) {
                sublabel.setFont(App.theme().Medium_Bold_Font());
                sublabel.setText(subvalue);
                sublabel.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }
        }
    }

    static class LabelAttributes_22 extends LabelAttributes_2 {

        public LabelAttributes_22() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return super.heightForWidth(width) + heightForWidth(width, attrSubvalue.toString()) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
//            SpannableStringBuilder attrs = StringFormatting.attributedTitleSubtitle(this)
            super.setAttributedTitlesForLabel(label, sublabel);
            if (sublabel != null) {
                attrSubvalue.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, attrSubvalue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sublabel.setText(attrSubvalue);
            }
        }
    }

    static class LabelAttributes_3 extends LabelAttributes {

        public LabelAttributes_3() {
            super();
        }

        protected static LabelAttributes_3 init(TYPE type) {
            switch (type) {
                case SUBVALUE:
                    return new LabelAttributes_31();
                case ATTR_SUBVALUE:
                    return new LabelAttributes_32();
                default:
                    return new LabelAttributes_3();
            }
        }

        @Override
        public int heightForWidth(int width) {
            return heightForWidth(width, attrValue.toString()) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            if (label != null) {
                attrValue.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, attrValue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                label.setText(attrValue);
            }
        }
    }

    static class LabelAttributes_31 extends LabelAttributes_3 {

        public LabelAttributes_31() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return super.heightForWidth(width) + heightForWidth(width, subvalue) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            super.setAttributedTitlesForLabel(label, sublabel);
            if (sublabel != null) {
                sublabel.setFont(App.theme().Medium_Bold_Font());
                sublabel.setText(this.subvalue);
                sublabel.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }
        }
    }

    static class LabelAttributes_32 extends LabelAttributes_3 {

        public LabelAttributes_32() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return super.heightForWidth(width) + heightForWidth(width, attrSubvalue.toString()) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
//            SpannableStringBuilder attrs = StringFormatting.attributedTitleSubtitle(this)
            super.setAttributedTitlesForLabel(label, sublabel);
            if (sublabel != null) {
                attrSubvalue.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0, attrSubvalue.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sublabel.setText(attrSubvalue);
            }
        }
    }

    static class LabelAttributes_4 extends LabelAttributes {

        public LabelAttributes_4() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return heightForWidth(width, title) + heightForWidth(width, subvalue) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            if (label != null) {
                label.setFont(App.theme().Medium_Bold_Font());
                label.setText(title);
                label.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }
            if (sublabel != null) sublabel.setText(subvalue);
        }
    }

    static class LabelAttributes_5 extends LabelAttributes {

        public LabelAttributes_5() {
            super();
        }

        @Override
        public int heightForWidth(int width) {
            return heightForWidth(width, title) + heightForWidth(width, attrSubvalue.toString()) + App.constants().Table_Cell_Line_Height();
        }

        @Override
        public void setAttributedTitlesForLabel(UILabel label, UILabel sublabel) {
            if (label != null) {
                label.setFont(App.theme().Medium_Bold_Font());
                label.setText(title);
                label.setTextAlignment(TEXT_ALIGNMENT_TEXT_START);
            }
            if (sublabel != null) sublabel.setText(attrSubvalue);
        }
    }

    enum TYPE {
        NONE         (0),
        TITLE        (1 << 0),
        PLACEHOLDER  (1 << 1),
        VALUE        (1 << 2),
        SUBVALUE     (1 << 3),
        ATTR_VALUE   (1 << 4),
        ATTR_SUBVALUE(1 << 5);

        TYPE(int value) {
            this.value = value;
        }

        private int value;
        private static final Map map = new HashMap<>();

        static {
            for (TYPE type : TYPE.values()) {
                map.put(type.value, type);
            }
        }

        public static TYPE valueOf(int value) {
            return (TYPE) map.get(value);
        }
    }
}
