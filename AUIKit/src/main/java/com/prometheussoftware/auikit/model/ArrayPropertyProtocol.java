package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;

import java.util.List;

public interface ArrayPropertyProtocol {
    default <O extends ViewContentProtocol.Placeholder> List<O> array() { return (this instanceof List) ? (List<O>) this : null; } ;
    default Object objectWithArray (List array) { return null; };
}
