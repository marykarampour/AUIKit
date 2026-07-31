package com.prometheussoftware.auikit.uiviewcontroller.mutable;

import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.model.ArrayPropertyProtocol;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.mutable.FieldListModel;
import com.prometheussoftware.auikit.model.mutable.MutableProtocol;
import com.prometheussoftware.auikit.model.mutable.MutableUpdateObject;
import com.prometheussoftware.auikit.model.mutable.UpdateListObject;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.utility.ObjectUtility;

import java.util.List;

public class EditingListsViewController
               <ObjectTypeA extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
                ObjectTypeB extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
                ObjectTypeC extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
                ObjectTypeD extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol>
        extends MutableObjectViewController <FieldListModel<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD>, FieldListModel <ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD> > {

    protected UpdateListObject<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD> object;

    static {
        BaseModel.Register(UpdateListObject.class, true);
    }

    public EditingListsViewController() {
        this(FieldListModel.LIST_TYPE.A);
    }

    public EditingListsViewController(FieldListModel.LIST_TYPE types) {
        super();

        FieldListModel obj = (FieldListModel) ObjectUtility.objectWithParams(classForListObject());
        obj.activeListTypes = types;
        initSelectedActionHandler();
        setUpdatedObject(obj);
        resetSelectedSets();
    }

    /** @brief A subclass of FieldListModel used to initialize object. Default is FieldListModel. */
    protected Class classForListObject() {
        return FieldListModel.class;
    }

    @Override
    public Class classForObject() {
        return UpdateListObject.class;
    }

    @Override
    public void setObject(MutableUpdateObject<FieldListModel<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD>, FieldListModel<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD>> obj) {
        this.object = (UpdateListObject<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD>) obj;
        didSetMutableObject(object);
    }

    @Override
    public UpdateListObject<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD> object() {
        return this.object;
    }

    @Override
    public <T extends FieldListModel<ObjectTypeA, ObjectTypeB, ObjectTypeC, ObjectTypeD>> void didResetUpdateObject(T object) {
        super.didResetUpdateObject(object);
    }

    @Override
    public int numberOfSectionsInTableView() {
        return object.UpdatedObject.activeListsCount();
    }

    @Override
    public int heightForRowAtIndexPath(IndexPath indexPath) {
        return isAddIndexPath(indexPath) ? heightForEditingListRowAtIndexPath(indexPath) : heightForNonEditingListRowAtIndexPath(indexPath);
    }

    private int heightForEditingListRowAtIndexPath(IndexPath indexPath) {
        return Dimensions.Int_52();
    }

    @Override
    public MutableProtocol.FIELD_TYPE typeForSection(int section) {
        return MutableProtocol.FIELD_TYPE.LIST;
    }

    @Override
    public int listTypeForListInSection(int section) {
        return FieldListModel.LIST_TYPE.option(section).intValue();
    }

    public <T extends ViewContentProtocol.Placeholder> void setItemsWithArrayForListOfType (List<T> items, FieldListModel.LIST_TYPE type) {
        setItemsForListOfType(items, type.intValue());
    }

    public <T extends ViewContentProtocol.Placeholder> void setItemsWithArray (List<T> items, int type) {
        setItemsForListOfType(items, type);
    }

    private void updateHeader() {
        //no items title
    }

    public <T extends ViewContentProtocol.Placeholder> List<T> addItemsToListOfType (List<T> items,int type) {
        List<T> existing = addItemsToListOfType(items, type);
        updateHeader();
        return existing;
    }

    @Override
    public <T extends ViewContentProtocol.Placeholder> boolean addItemToListOfType(T item, int type) {
        boolean existing = super.addItemToListOfType(item, type);
        updateHeader();
        return existing;
    }

    @Override
    public <T extends ViewContentProtocol.Placeholder> void deleteItemsFromListOfType(List<T> items, int type) {
        super.deleteItemsFromListOfType(items, type);
        updateHeader();
    }

    @Override
    public <T extends ViewContentProtocol.Placeholder> void deleteItemFromListOfType(T item, int type) {
        super.deleteItemFromListOfType(item, type);
        updateHeader();
    }

    public static class Single <T extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol> extends EditingListsViewController <T, T, T, T> {

        public Single() {
            super(FieldListModel.LIST_TYPE.A);
        }

        @Override
        public void setObject(MutableUpdateObject<FieldListModel<T, T, T, T>, FieldListModel<T, T, T, T>> obj) {
            super.setObject(obj);
        }

        @Override
        public UpdateListObject<T, T, T, T> object() {
            return super.object();
        }

        @Override
        public <L extends FieldListModel<T, T, T, T>> void didResetUpdateObject(L object) {
            super.didResetUpdateObject(object);
        }
    }
}
