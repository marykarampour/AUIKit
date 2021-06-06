package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.model.BaseModel;

public class UITabBarItem extends UIBarItem {

    private UIImage selectedImage;

    private String badgeValue;

    public static UITabBarItem build (String title, UIImage image, UIImage selectedImage) {

        UITabBarItem item = new UITabBarItem();
        item.setTitle(title);
        item.setImage(image);
        item.selectedImage = selectedImage == null ? image : selectedImage;
        return item;
    }

    public void setSelectedImage(UIImage selectedImage) {
        this.selectedImage = selectedImage;
    }

    public UIImage getSelectedImage() {
        return selectedImage;
    }

    static {
        BaseModel.Register(UITabBarItem.class, true);
    }
}
