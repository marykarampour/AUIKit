package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.model.BaseModel;

public class MutableUpdateObject<O extends BaseModel & MutableProtocol.Field, U extends BaseModel & MutableProtocol.Field> extends MutableObject <O, U> implements MutableProtocol.Update {

    public MutableUpdateObject (O object) {
        super(object);
    }

    @Override
    public boolean isLongValueForSectionType(int section) {
        return UpdatedObject.isLongValueForSectionType(section);
    }

    @Override
    public boolean isEditableSectionType(int section) {
        return OriginalObject.isEditableSectionType(section);
    }

    @Override
    public boolean hasValueForSectionType(int section) {
        return UpdatedObject.hasValueForSectionType(section);
    }

    @Override
    public boolean isCommentSectionType(int section) {
        return UpdatedObject.isCommentSectionType(section);
    }
}
