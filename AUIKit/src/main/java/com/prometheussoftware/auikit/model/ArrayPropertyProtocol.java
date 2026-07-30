package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;

import java.util.ArrayList;

public interface ArrayPropertyProtocol <O extends ViewContentProtocol.Placeholder> {
    ArrayList<O> array();
    void setArray(ArrayList<O> array);
    default Object objectWithArray (ArrayList<O> array) { return null; };
}
