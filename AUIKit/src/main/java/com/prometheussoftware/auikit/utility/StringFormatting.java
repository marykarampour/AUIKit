package com.prometheussoftware.auikit.utility;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

import java.util.ArrayList;

public class StringFormatting {

    public static SpannableStringBuilder singleTextAttributes(String text, UIColor color, UIFont font) {

        ArrayList<Attributes> attrs = new ArrayList();
        attrs.add(new Attributes(StringUtility.nonNull(text), color, font));
        return attributedText(attrs, "");
    }

    public static SpannableString singleSpan(UIColor color, UIFont font, String text) {

        SpannableString span = new SpannableString(text);
        span.setSpan(new StyleSpan(font.style()), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        span.setSpan(new ForegroundColorSpan(color.get()), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return span;
    }

    public static void appendAttributedTextFromSpans(SpannableStringBuilder builder, ArrayList<SpannableString> attrs, String delimiter) {
        for (SpannableString attr : attrs) {
            builder.append(attr);
            if (attrs.indexOf(attr) != attrs.size() - 1) builder.append(delimiter);
        }
    }

    /** @apiNote Use setAllCaps(false) on buttons for API 23 or lower before setting
     *  text using this method otherwise the font and color are not going to work */
    public static SpannableStringBuilder attributedText(ArrayList<Attributes> attrs, String delimiter) {
        SpannableStringBuilder builder = new SpannableStringBuilder();

        for (Attributes attr : attrs) {
            if (!StringUtility.isNotEmpty(attr.text)) continue;

            SpannableString text1 = new SpannableString(attr.text);
            text1.setSpan(new StyleSpan(attr.font.style()), 0, attr.text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text1.setSpan(new AbsoluteSizeSpan(attr.font.pixelSize()), 0, attr.text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text1.setSpan(new ForegroundColorSpan(attr.color.get()), 0, attr.text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(text1);
            if (attrs.indexOf(attr) != attrs.size() - 1) builder.append(delimiter);
        }
        return builder;
    }

    public static SpannableStringBuilder attributedTitleSubtitle (String title, UIColor titleColor, UIFont titleFont, String subtitle, UIColor subtitleColor, UIFont subtitleFont, String delimiter) {

        ArrayList<StringFormatting.Attributes> attrs = new ArrayList<>();
        attrs.add(new StringFormatting.Attributes(
                title,
                titleColor,
                titleFont));
        attrs.add(new StringFormatting.Attributes(
                subtitle,
                subtitleColor,
                subtitleFont));

        return StringFormatting.attributedText(attrs, delimiter);
    }


    //region subclasses

    public static class Attributes {

        public String text;
        public UIFont font;
        public UIColor color;

        public Attributes(String text, UIColor color, UIFont font) {
            this.text = text;
            this.color = color;
            this.font = font;
        }
    }

    public static class Field {

        public String text;
        public boolean isMandatory;

        public Field(String text, boolean isMandatory) {
            this.text = text;
            this.isMandatory = isMandatory;
        }
    }

    //endregion
}
