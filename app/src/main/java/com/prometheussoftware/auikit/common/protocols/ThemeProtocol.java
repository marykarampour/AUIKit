package com.prometheussoftware.auikit.common.protocols;

import android.util.Size;

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

    //accessory
    UIColor Accessory_Disabled_Color();
    UIColor Accessory_Selected_Color();
    UIColor Accessory_Deselected_Color();
    Size Accessory_Size();

    //fonts
    UIFont Small_Regular_Font();
    UIFont XS_mall_Regular_Font();
    UIFont XX_Small_Regular_Font();
    UIFont XXX_Small_Regular_Font();

    UIFont Large_Regular_Font();
    UIFont XS_Large_Regular_Font();
    UIFont XX_Large_Regular_Font();
    UIFont XXX_Large_Regular_Font();

    UIFont Medium_Regular_Font();

    UIFont Small_Bold_Font();
    UIFont XS_mall_Bold_Font();
    UIFont XX_Small_Bold_Font();
    UIFont XXX_Small_Bold_Font();

    UIFont Large_Bold_Font();
    UIFont XS_Large_Bold_Font();
    UIFont XX_Large_Bold_Font();
    UIFont XXX_Large_Bold_Font();

    UIFont Medium_Bold_Font();

}
