package com.prometheussoftware.auikit.container;

import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

public interface HeaderFooterVCProtocol {

    interface Container {

        void setContentView();
        void createHeaderView();
        void createFooterView();
        default int headerHeight() { return 0; }
        default int headerWidth() { return Constants.Screen_Size().getWidth(); }
        default int headerVerticalMargin() { return 0; }
        default int footerHeight() { return 0; }
        default int footerWidth() { return Constants.Screen_Size().getWidth(); }
        default int maxFooterHeight() { return 0; }
        default int maxHeaderHeight() { return 0; }
        default int footerVerticalMargin() { return 0; }
        void createChildVC();
        default void createChildVCWithChildObject (Object object) {}
        default void setHeaderViewTitle (String title) {}
        default int animationDuration() { return 400; }
    }

    interface Child <O, C extends UIViewController & HeaderFooterVCProtocol.Container> {
        C headerDelegate();
        void setHeaderDelegate(C headerDelegate);
        O object();
        void setObject(O object);
    }
}
