package com.prometheussoftware.auikit.tableview;

import android.text.SpannableStringBuilder;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.utility.StringFormatting;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;

public interface BaseCellDataSource {
    String title();
    default String plainTitle() { return title(); }
    default String subtitle() { return null; }

    default SpannableStringBuilder attributedTitle() {

        String title = title();
        String subtitle = subtitle();

        if (StringUtility.isEmpty(title) || StringUtility.isEmpty(subtitle)) return null;

        SpannableStringBuilder builder = new SpannableStringBuilder();

        ArrayList<StringFormatting.StringAttributes> attrs = new ArrayList<>();
        attrs.add(new StringFormatting.StringAttributes(
                title,
                App.theme().XX_Small_Bold_Font(),
                App.theme().Black_Blue_Color()));
        attrs.add(new StringFormatting.StringAttributes(
                subtitle,
                App.theme().X_Small_Regular_Font(),
                App.theme().Medium_Silver_Color()));

        SpannableStringBuilder titleBuilder = StringFormatting.attributedText(attrs, "\n");
        builder.append(titleBuilder);
        return builder;
    }
}
