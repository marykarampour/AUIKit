package com.prometheussoftware.auikit.common.protocols;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;

public interface ThemeProtocol {

    //spinner
    default UIColor Spinner_Hud_Color() { return UIColor.white(1.0f); }
    default UIColor spinner_Frame_Color() { return UIColor.black(0.7f); }

    //nav bar
    default UIColor Nav_Bar_Tint_Color() { return UIColor.black(1.0f); }
    default UIColor Nav_Bar_Background_Color() { return UIColor.white(1.0f); }
    default UIFont Nav_Bar_Font() { return UIFont.systemFont(14, UIFont.STYLE.BOLD); }

    //tab bar
    default UIColor Tab_Bar_Tint_Color() { return UIColor.black(1.0f); }
    default UIColor Tab_Bar_Selected_Item_Tint_Color() { return UIColor.blue(1.0f); }
    default UIColor Tab_Bar_Background_Color() { return UIColor.white(1.0f); }
    default UIFont Tab_Bar_Font() { return UIFont.systemFont(12, UIFont.STYLE.REGULAR); }

    //controls
    default int Picker_View_Style() { return 0; }
    default UIColor TextField_Background_Color() { return UIColor.white(1.0f); }
    default UIColor TextField_Placeholder_Color() { return UIColor.gray(1.0f); }
    default UIColor TextField_Text_Color() { return UIColor.black(1.0f); }

    //accessory
    default UIColor Accessory_Disabled_Color() { return UIColor.white(1.0f); }
    default UIColor Accessory_Selected_Color() { return UIColor.white(1.0f); }
    default UIColor Accessory_Deselected_Color() { return UIColor.white(1.0f); }

    //tableview
    default UIColor Tableview_Separator_Color() { return UIColor.build(0.7f, 0.6f, 0.7f, 1.0f); }

    //fonts
    default UIFont Small_Regular_Font() { return UIFont.systemFont(14, UIFont.STYLE.REGULAR); }
    default UIFont X_Small_Regular_Font() { return UIFont.systemFont(12, UIFont.STYLE.REGULAR); }
    default UIFont XX_Small_Regular_Font() { return UIFont.systemFont(10, UIFont.STYLE.REGULAR); }
    default UIFont XXX_Small_Regular_Font() { return UIFont.systemFont(8, UIFont.STYLE.REGULAR); }

    default UIFont Large_Regular_Font() { return UIFont.systemFont(18, UIFont.STYLE.REGULAR); }
    default UIFont XS_Large_Regular_Font() { return UIFont.systemFont(20, UIFont.STYLE.REGULAR); }
    default UIFont XX_Large_Regular_Font() { return UIFont.systemFont(22, UIFont.STYLE.REGULAR); }
    default UIFont XXX_Large_Regular_Font() { return UIFont.systemFont(24, UIFont.STYLE.REGULAR); }

    default UIFont Medium_Regular_Font() { return UIFont.systemFont(16, UIFont.STYLE.REGULAR); }

    default UIFont Small_Bold_Font() { return UIFont.systemFont(14, UIFont.STYLE.BOLD); }
    default UIFont X_Small_Bold_Font() { return UIFont.systemFont(12, UIFont.STYLE.BOLD); }
    default UIFont XX_Small_Bold_Font() { return UIFont.systemFont(10, UIFont.STYLE.BOLD); }
    default UIFont XXX_Small_Bold_Font() { return UIFont.systemFont(8, UIFont.STYLE.BOLD); }

    default UIFont Large_Bold_Font() { return UIFont.systemFont(18, UIFont.STYLE.BOLD); }
    default UIFont XS_Large_Bold_Font() { return UIFont.systemFont(20, UIFont.STYLE.BOLD); }
    default UIFont XX_Large_Bold_Font() { return UIFont.systemFont(22, UIFont.STYLE.BOLD); }
    default UIFont XXX_Large_Bold_Font() { return UIFont.systemFont(24, UIFont.STYLE.BOLD); }

    default UIFont Medium_Bold_Font() { return UIFont.systemFont(16, UIFont.STYLE.BOLD); }

    //colors

    default UIColor Black_Blue_Color() { return UIColor.black(1.0f); }
    default UIColor Dark_Blue_Color() { return UIColor.black(1.0f); }
    default UIColor Medium_Blue_Color() { return UIColor.blue(1.0f); }
    default UIColor Bright_Blue_Color() { return UIColor.blue(1.0f); }
    default UIColor Light_Blue_Color() { return UIColor.white(1.0f); }
    default UIColor Mist_Blue_Color() { return UIColor.white(1.0f); }
    default UIColor White_Blue_Color() { return UIColor.white(1.0f); }

    default UIColor Black_Red_Color() { return UIColor.black(1.0f); }
    default UIColor Dark_Red_Color() { return UIColor.black(1.0f); }
    default UIColor Medium_Red_Color() { return UIColor.red(1.0f); }
    default UIColor Bright_Red_Color() { return UIColor.red(1.0f); }
    default UIColor Light_Red_Color() { return UIColor.white(1.0f); }
    default UIColor Mist_Red_Color() { return UIColor.white(1.0f); }
    default UIColor White_Red_Color() { return UIColor.white(1.0f); }

    default UIColor Black_Yellow_Color() { return UIColor.black(1.0f); }
    default UIColor Dark_Yellow_Color() { return UIColor.black(1.0f); }
    default UIColor Medium_Yellow_Color() { return UIColor.yellow(1.0f); }
    default UIColor Bright_Yellow_Color() { return UIColor.yellow(1.0f); }
    default UIColor Light_Yellow_Color() { return UIColor.white(1.0f); }
    default UIColor Mist_Yellow_Color() { return UIColor.white(1.0f); }
    default UIColor White_Yellow_Color() { return UIColor.white(1.0f); }

    default UIColor Black_Green_Color() { return UIColor.black(1.0f); }
    default UIColor Dark_Green_Color() { return UIColor.black(1.0f); }
    default UIColor Medium_Green_Color() { return UIColor.green(1.0f); }
    default UIColor Bright_Green_Color() { return UIColor.green(1.0f); }
    default UIColor Light_Green_Color() { return UIColor.white(1.0f); }
    default UIColor Mist_Green_Color() { return UIColor.white(1.0f); }
    default UIColor White_Green_Color() { return UIColor.white(1.0f); }

    default UIColor Black_Gold_Color() { return UIColor.black(1.0f); }
    default UIColor Dark_Gold_Color() { return UIColor.black(1.0f); }
    default UIColor Medium_Gold_Color() { return UIColor.yellow(1.0f); }
    default UIColor Bright_Gold_Color() { return UIColor.yellow(1.0f); }
    default UIColor Light_Gold_Color() { return UIColor.white(1.0f); }
    default UIColor Mist_Gold_Color() { return UIColor.white(1.0f); }
    default UIColor White_Gold_Color() { return UIColor.white(1.0f); }

    default UIColor Black_Silver_Color() { return UIColor.black(1.0f); }
    default UIColor Dark_Silver_Color() { return UIColor.black(1.0f); }
    default UIColor Medium_Silver_Color() { return UIColor.gray(1.0f); }
    default UIColor Bright_Silver_Color() { return UIColor.gray(1.0f); }
    default UIColor Light_Silver_Color() { return UIColor.white(1.0f); }
    default UIColor Mist_Silver_Color() { return UIColor.white(1.0f); }
    default UIColor White_Silver_Color() { return UIColor.white(1.0f); }
}
