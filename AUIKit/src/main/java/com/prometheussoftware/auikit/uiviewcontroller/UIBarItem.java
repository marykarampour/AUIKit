package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.model.BaseModel;

public class UIBarItem extends BaseModel {

    private UIImage image;

    private String title;

    public static UIBarItem build (String title, UIImage image) {

        UIBarItem item = new UIBarItem();
        item.title = title;
        item.image = image;
        return item;
    }

    public void setImage(UIImage image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UIImage getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    static {
        BaseModel.Register(UIBarItem.class, true);
    }
}
