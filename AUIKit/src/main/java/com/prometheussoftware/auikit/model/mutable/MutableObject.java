package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.utility.ObjectUtility;

import java.util.ArrayList;
import java.util.List;

/** @note Don't clone. It will reset the UpdatedObject to OriginalObject. */
public class MutableObject <O extends MutableProtocol.Field, U extends MutableProtocol.Field> extends BaseModel implements MutableProtocol.Mutable {

    public O OriginalObject;
    public U UpdatedObject;

    transient Class classForOriginalObject;
    transient Class classForUpdatedObject;

    /** @brief This initialization copies object.
    @note If using generics, and if object is nil, OriginalObject wlll be nil. In that case defaultClassForUpdatedObject must be provided and will be used to initialize both
    OriginalObject and UpdatedObject. Otherwise, don't use the generic types, instead redefine these properties explicitly and use @dynamic.
    The reason is that the generic type is errased in runtime and some NSObject subclasses such as NSString, when handled by OS have actual type constant, such as
    NSCFString, and might not recognize selectors such as allocWithZone otherwise used to copy these properties in case of MKUModel. */
    public MutableObject (O object) {
        super();
        setupWithObject(object);
    }

    public MutableObject() {
        this(null);
    }

    void setupWithObject (O object) {

        Object obj = object;//TODO: should be copy, but clone is broken BaseModel.copy(object);

        if (obj == null) {
            Class cls = getClassForOriginalObject();
            if (!Cloneable.class.isAssignableFrom(cls))
                cls = classOfPropertyForObjectClass("OriginalObject", getClass());
            obj = ObjectUtility.objectWithParams(cls);
        }

        setOriginalObject((O) obj);
    }

    /** @brief Reinitializes both OriginalObject and UpdatedObject. */
    public void reset() {
        if (getClassForUpdatedObject().isAssignableFrom(OriginalObject.getClass())) {
            setUpdatedObject((U) OriginalObject);//TODO: should be copy, but clone is broken BaseModel.copy((U) OriginalObject);
        }
        else {
            setUpdatedObject((U) ObjectUtility.objectWithParams(getClassForUpdatedObject()));
        }
    }

    /** @brief Only sets OriginalObject = nil. */
    public void resetOriginalObject() {
        OriginalObject = null;
    }

    public void setOriginalObject(O originalObject) {
        OriginalObject = originalObject;
        if (originalObject != null && BaseModel.class.isAssignableFrom(originalObject.getClass()))
            classForOriginalObject = originalObject.getClass();
        reset();
    }

    public void setUpdatedObject(U updatedObject) {
        if (updatedObject != null) {
            UpdatedObject = updatedObject;
            if (BaseModel.class.isAssignableFrom(updatedObject.getClass()))
                classForUpdatedObject = updatedObject.getClass();
        }
        else {
            reset();
        }
    }

    /** @brief Creates a new instance with OriginalObject = nil. Other fields are set as is, not copied. */
    public MutableObject duplicateUpdateObject() {
        MutableObject obj = (MutableObject) ObjectUtility.objectWithParams(getClass());
        obj.UpdatedObject = UpdatedObject;//TODO: should be copy, but clone is broken BaseModel.copy(UpdatedObject);
        obj.resetOriginalObject();
        return obj;
    }

    public boolean isModified() {
        return !OriginalObject.equals(UpdatedObject);
    }

    public static <O extends BaseModel & MutableProtocol.Field> List<MutableObject<O, O>> updateObjectsWithObjects (Class cls, List<O> objects) {
        ArrayList<MutableObject<O, O>> arr = new ArrayList<>();

        for (O obj : objects) {
            MutableObject update = (MutableObject) ObjectUtility.objectWithParams(cls, new ObjectUtility.Params(cls, obj));
            arr.add(update);
        }
        return arr;
    }

    public Class getClassForOriginalObject() {
        if (classForOriginalObject == null)
            classForOriginalObject = defaultClassForOriginalObject();
        return classForOriginalObject;
    }

    public Class getClassForUpdatedObject() {
        if (classForUpdatedObject == null)
            classForUpdatedObject = defaultClassForUpdatedObject();
        return classForUpdatedObject;
    }

    @Override
    public Class defaultClassForUpdatedObject() {
        return Object.class;
    }

    @Override
    public Class defaultClassForOriginalObject() {
        return defaultClassForUpdatedObject();
    }

    @Override
    public String nameForOriginalObject() {
        return "OriginalObject";
    }

    @Override
    public String nameForUpdatedObject() {
        return "UpdatedObject";
    }
}
