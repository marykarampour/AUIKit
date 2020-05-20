package com.prometheussoftware.auikit.common.protocols;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

public interface ThemeProtocol {

    //spinner
    UIColor Spinner_Hud_Color();
    UIColor spinner_Frame_Color();

    //nav bar
    UIColor Nav_Bar_Tint_Color();
    UIColor Nav_Bar_Background_Color();
    UIFont Nav_Bar_Font();

    //controls
    int Picker_View_Style();
}
