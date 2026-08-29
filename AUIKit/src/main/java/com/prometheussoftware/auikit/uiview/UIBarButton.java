package com.prometheussoftware.auikit.uiview;

import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Assets;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.HashMap;

public class UIBarButton extends UIButton {

    public TYPE type = TYPE.NONE;
    public POSITION position;
    private boolean enabled = true;

    public UIBarButton() {
        super();
    }

    public UIBarButton(String title, UITargetDelegate.TouchUp target) {
        super();
        getTitleLabel().setText(title);
        setTarget(target);
    }

    public UIBarButton(TYPE type, UITargetDelegate.TouchUp target) {
        super();
        setImage(Assets.imageFromName(type.stringValue()));
        setTarget(target);
    }

    public UIBarButton(UIImage image, UITargetDelegate.TouchUp target) {
        super();
        setImage(image);
        setTarget(target);
    }

    @Override
    public void initView() {
        super.initView();
        setTintColor(App.theme().Nav_Bar_Tint_Color());
        getTitleLabel().setTextColor(App.theme().Nav_Bar_Tint_Color());
        getTitleLabel().setFont(App.theme().Nav_Bar_Font());
    }

    /** @brief Creates a blank object, the buttonItem is not set, it will be set only when added to navbar in UIViewController (BarButtonContainer). */
    public static UIBarButton editNavBarButtonObject() {
        UIBarButton obj = new UIBarButton();
        obj.type = TYPE.SYSTEM_EDIT;
        obj.position = POSITION.RIGHT;
        return obj;
    }

    public static UIBarButton editNavBarButtonObjectWithEditButton (UIBarButton edit) {
        edit.type = TYPE.SYSTEM_EDIT;
        edit.position = POSITION.RIGHT;
        return edit;
    }

    /** @brief Creates a button using initWithTitle it title is given, if title is empty it uses initWithBarButtonSystemItem. */
    public static UIBarButton navBarButtonWithTitle (String title, TYPE type, UITargetDelegate.TouchUp target, boolean enabled) {
        UIBarButton obj = navBarButtonWithTitle(title, type, target);
        obj.setEnabled(enabled);
        return obj;
    }

    /** @brief Creates a button using initWithTitle it title is given, if title is empty it uses initWithBarButtonSystemItem. */
    public static UIBarButton navBarButtonWithTitle (String title, TYPE type, UITargetDelegate.TouchUp target) {
        UIBarButton obj;
        if (StringUtility.isNotEmpty(title))
            obj = new UIBarButton(title, target);
        else
            obj = new UIBarButton(type, target);
        obj.type = type;
        return obj;
    }

    public static UIBarButton spacer() {
        return new UIBarButton(TYPE.SYSTEM_SPACER, null);
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        setUserInteractionEnabled(enabled);
    }

    public enum POSITION {
        RIGHT(0),
        LEFT(1);

        private final int value;
        private static final HashMap<Integer, POSITION> map = new HashMap<>();

        static  {
            for (POSITION row : values()) {
                map.put(row.value, row);
            }
        }

        POSITION(int i) {
            value = i;
        }

        public int intValue() { return value; }
    }

    public enum TYPE {
        NONE            (-1, null),
        SYSTEM_DONE     (0,  Constants.Done_STR()), /** Used in ItemsListViewController. Mututally exclusive with EDIT and SAVE. */
        SYSTEM_EDIT     (1,  Constants.Edit_STR()), /** Used in EditingListsViewController. Mututally exclusive with DONE and SAVE. */
        SYSTEM_SAVE     (2,  Constants.Save_STR()), /** USed in MutableObjectTableViewController. Mututally exclusive with EDIT and DONE. */
        SYSTEM_ADD      (3,  App.assets().Add_Image_Name()),
        SYSTEM_SEARCH   (4,  App.assets().Search_Image_Name()),
        SYSTEM_REFRESH  (5,  App.assets().Refresh_Image_Name()),
        SYSTEM_SPACER   (6,  null),
        SYSTEM_CAMERA   (7,  App.assets().Camera_Image_Name()),
        SYSTEM_TRASH    (8,  App.assets().Trash_Image_Name()),
        SYSTEM_CLOSE    (9,  App.assets().Close_Image_Name()),
        SYSTEM_COUNT    (10, null),//No item, marks the end of system items
        RESET           (11, Constants.Reset_STR()),
        CLEAR           (12, Constants.Clear_STR()),
        CLOSE           (13, Constants.Close_STR()),
        ALL             (14, Constants.All_STR()),
        EMAIL           (15, App.assets().Email_Image_Name()),
        PRINT           (16, App.assets().Print_Image_Name()),
        SHARE           (17, App.assets().Share_Image_Name()),
        COUN            (18, null);

        private final int value;
        private final String string;
        private static final HashMap<Integer, TYPE> map = new HashMap<>();

        static  {
            for (TYPE row : values()) {
                map.put(row.value, row);
            }
        }

        TYPE(int i, String str) {
            value = i;
            string = str;
        }

        public int intValue() { return value; }
        public String stringValue() { return string; }
    }
}
