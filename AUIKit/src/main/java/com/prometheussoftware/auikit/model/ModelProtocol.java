package com.prometheussoftware.auikit.model;

public interface ModelProtocol {

    interface Object<O extends BaseModel> {
        default void didSetObject(O obj) {};
        void setObject (O obj);
        O object();
    }
}
