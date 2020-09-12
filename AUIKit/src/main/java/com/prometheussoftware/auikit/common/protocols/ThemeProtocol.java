package com.prometheussoftware.auikit.common.protocols;

import android.util.Size;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

public interface ThemeProtocol {

    //spinner
    default UIColor Spinner_Hud_Color() { return UIColor.white(1.0f); }
    default UIColor spinner_Frame_Color() { return UIColor.black(1.0f); }

    //nav bar
    default UIColor Nav_Bar_Tint_Color() { return UIColor.black(1.0f); }
    default UIColor Nav_Bar_Background_Color() { return UIColor.white(1.0f); }
    default UIFont Nav_Bar_Font() { return UIFont.systemFont(14, UIFont.STYLE.BOLD); }

    //controls
    default int Picker_View_Style() { return 0; }

    //accessory
    default UIColor Accessory_Disabled_Color() { return UIColor.white(1.0f); }
    default UIColor Accessory_Selected_Color() { return UIColor.white(1.0f); }
    default UIColor Accessory_Deselected_Color() { return UIColor.white(1.0f); }

    //tableview
    default UIColor Tableview_Separator_Color() { return UIColor.build(0.7f, 0.6f, 0.7f, 1.0f); }

    //fonts
    default UIFont Small_Regular_Font() { return UIFont.systemFont(); }
    default UIFont XS_mall_Regular_Font() { return UIFont.systemFont(); }
    default UIFont XX_Small_Regular_Font() { return UIFont.systemFont(); }
    default UIFont XXX_Small_Regular_Font() { return UIFont.systemFont(); }

    default UIFont Large_Regular_Font() { return UIFont.systemFont(); }
    default UIFont XS_Large_Regular_Font() { return UIFont.systemFont(); }
    default UIFont XX_Large_Regular_Font() { return UIFont.systemFont(); }
    default UIFont XXX_Large_Regular_Font() { return UIFont.systemFont(); }

    default UIFont Medium_Regular_Font() { return UIFont.systemFont(); }

    default UIFont Small_Bold_Font() { return UIFont.systemFont(); }
    default UIFont XS_mall_Bold_Font() { return UIFont.systemFont(); }
    default UIFont XX_Small_Bold_Font() { return UIFont.systemFont(); }
    default UIFont XXX_Small_Bold_Font() { return UIFont.systemFont(); }

    default UIFont Large_Bold_Font() { return UIFont.systemFont(); }
    default UIFont XS_Large_Bold_Font() { return UIFont.systemFont(); }
    default UIFont XX_Large_Bold_Font() { return UIFont.systemFont(); }
    default UIFont XXX_Large_Bold_Font() { return UIFont.systemFont(); }

    default UIFont Medium_Bold_Font() { return UIFont.systemFont(); }

}
