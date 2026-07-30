package com.prometheussoftware.auikit.model;

import java.io.Serializable;

public interface ModelProtocol {
    interface Object<O extends Serializable> {
        default void didSetObject(O obj) {};
        void setObject (O obj);
        O object();
    }
}
