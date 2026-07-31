package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.model.ArrayPropertyProtocol;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.mutable.MutableObjectViewController;

public class UpdateListObject <ObjectTypeA extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
        ObjectTypeB extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
        ObjectTypeC extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
        ObjectTypeD extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol>
        extends MutableUpdateObject <FieldListModel<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD>, FieldListModel <ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD> > implements MutableProtocol.Update {

    public UpdateListObject(FieldListModel<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD> object) {
        super(object);
    }
}
