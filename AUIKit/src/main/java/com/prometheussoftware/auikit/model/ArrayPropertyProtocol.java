package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;

import java.util.List;

public interface ArrayPropertyProtocol {
    default <O extends ViewContentProtocol.Placeholder> List<O> array() { return null; } ;
    default Object objectWithArray (List array) { return null; };
}
