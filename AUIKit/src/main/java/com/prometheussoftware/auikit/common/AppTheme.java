package com.prometheussoftware.auikit.common;

import android.util.Size;

import com.prometheussoftware.auikit.common.protocols.ThemeProtocol;
import com.prometheussoftware.auikit.utility.StringUtility;

public class AppTheme implements ThemeProtocol {

    public static Size Bar_Button_Size (String title) {
        int width = StringUtility.width(title, App.theme().Nav_Bar_Font().pixelSize());
        return new Size(width, App.constants().Nav_Bar_Height());
    }

}
