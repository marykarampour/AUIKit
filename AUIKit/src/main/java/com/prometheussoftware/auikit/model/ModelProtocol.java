package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.uiview.UITextField;

public interface ModelProtocol {

    public interface Object<O extends BaseModel> {
        default void didSetObject(O obj) {};
        void setObject (O obj);
        O object();
    }
}
