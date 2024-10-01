package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.common.App;

public class UIBarButton extends UIButton {

    public UIBarButton() {
        super();
        setTintColor(App.theme().Nav_Bar_Tint_Color());
        getTitleLabel().setTextColor(App.theme().Nav_Bar_Tint_Color());
        getTitleLabel().setFont(App.theme().Nav_Bar_Font());
    }
}
