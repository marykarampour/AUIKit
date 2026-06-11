package com.prometheussoftware.auikit.uiviewcontroller.mutable;

import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.mutable.MutableProtocol;
import com.prometheussoftware.auikit.model.mutable.MutableUpdateObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.uiview.UIScrollview;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.tableview.BaseTableViewCell;
import com.prometheussoftware.auikit.uiviewcontroller.ItemsListProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.uiviewcontroller.ViewControllerTransition;
import com.prometheussoftware.auikit.utility.ObjectUtility;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public abstract class MutableObjectViewController <ObjectType extends BaseModel & MutableProtocol.Field, UpdateObjectType extends BaseModel & MutableProtocol.Field> extends UIViewController implements MutableProtocol.ViewController<ObjectType, UpdateObjectType, MutableUpdateObject<ObjectType, UpdateObjectType>>, ViewControllerTransition, ItemsListProtocol.VC, ItemsListProtocol.EditingListVC, ItemsListProtocol.VCTransitionDelegate, ItemsListProtocol.UpdateDelegate, ViewControllerTransition.Delegate, UITableViewProtocol.TableViewData {

    private UIScrollview scrollview;
    protected HashMap<Integer, Set> selectedSets = new HashMap<>();
    private boolean isEditable;

    public MutableObjectViewController() {
        super();
        init();
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        scrollview = new UIScrollview();
        scrollview.setBackgroundColor(UIColor.yellow(1.0f));
        scrollview.getContentView().setBackgroundColor(UIColor.black(1.0f));
        view().addSubview(scrollview);
        constraintViews();
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;

        updateDatesWithUpdateObject(object().UpdatedObject);
        if (hasMutableNavbar()) setAsNavBarTarget();
        reload();
    }

    private void setAsNavBarTarget() {
    }

    private boolean hasMutableNavbar() {
        return true;
    }

    @Override
    public void didSetObject(MutableUpdateObject<ObjectType, UpdateObjectType> obj) {
        resetSelectedSets();
    }

    protected void resetSelectedSets() {
        selectedSets = new HashMap<>();
    }

    @Override
    public void setObject(MutableUpdateObject<ObjectType, UpdateObjectType> obj) {
        didSetMutableObject(obj);
    }

    @Override
    public MutableUpdateObject<ObjectType, UpdateObjectType> object() {
        return null;
    }

    /** @brief Call in setObject after setting the object. */
    protected void didSetMutableObject(MutableUpdateObject<ObjectType, UpdateObjectType> obj) {
        didSetObject(obj);
        if (!(obj instanceof MutableUpdateObject)) return;
        obj.UpdatedObject.setUpdateDelegate(this);
        didResetUpdateObject(obj.UpdatedObject);
    }

    @Override
    public void updateObjectWithCompletion(MutableProtocol.UpdateCallback completion) {
        completion.onSuccess(false);
        completion.onFailure(null);
    }

    @Override
    public void saveObjectWithCompletion(MutableProtocol.SaveCallback completion) {
        completion.onSuccess(null);
        completion.onFailure(null);
    }

    @Override
    public boolean canUpdate() {
        return false;
    }

    @Override
    public void prepareDataForUpdate() {
        //TODO: date pickers
    }

    @Override
    public void didResetUpdateObject(UpdateObjectType object) {
        updateDatesWithUpdateObject(object);
//        registerKVO();
        reload();
    }

    private void updateDatesWithUpdateObject(MutableProtocol.Field object) {
    }

    @Override
    public void dispatchDelegateForSaveDone() {
        defaultDispatchDelegateForSaveDone();
    }

    protected void defaultDispatchDelegateForSaveDone() {
        dispathTransitionDelegateToReturnWithObject(object().UpdatedObject);
    }

    @Override
    public MutableProtocol.UpdateCallback performSaveOrUpdateObjectCompletionHandler() {
        return null;
    }

    @Override
    public void setUpdatedObject(UpdateObjectType updatedObject) {
        MutableUpdateObject object = (MutableUpdateObject) ObjectUtility.objectWithParams(updatedObject.getClass());
        setObject(object);
    }

    @Override
    public void initIsEditable() {
        MutableProtocol.ViewController.super.initIsEditable();
    }

    @Override
    public boolean userInteractionEnabledForSingleCellAtIndexPath(IndexPath indexPath) {
        return isEditable;
    }

    @Override
    public boolean hasAccessoryForSection(int section) {
        MutableProtocol.FIELD_TYPE type = typeForSection(section);
        return  (type != MutableProtocol.FIELD_TYPE.LABEL) &&
                (canSelectSection(section) || hasTypesForSection(section)) &&
                canTransitionToPresentingSelectionVCInSection(section);
    }

    @Override
    public void didSelectSection(int section) {
        MutableProtocol.ViewController.super.didSelectSection(section);
    }

    @Override
    public boolean canEditSection(int section) {
        return MutableProtocol.ViewController.super.canEditSection(section);
    }

    @Override
    public boolean hideSection(int section) {
        return false;
    }

    @Override
    public boolean shouldHideSelectionSection(int section) {
        return false;
    }

    @Override
    public boolean canTransitionToPresentingSelectionVCInSection(int section) {
        return false;
    }

    @Override
    public String titleForSection(int section) {
        return "";
    }

    @Override
    public String subtitleForSection(int section) {
        return "";
    }

    @Override
    public String placeholderTitleForSection(int section) {
        return "";
    }

    @Override
    public String valueForSection(int section) {
        return "";
    }

    @Override
    public AttributedString attributedValueForSection(int section) {
        return null;
    }

    @Override
    public AttributedString attributedSubvalueForSection(int section) {
        return null;
    }

    @Override
    public String labelDelimiterForSection(int section) {
        return "";
    }

    @Override
    public String sublabelDelimiterForSection(int section) {
        return "";
    }

    @Override
    public String subvalueForSection(int section) {
        return "";
    }

    @Override
    public boolean boolValueForSection(int section) {
        return false;
    }

    @Override
    public int rowForFieldAtIndexInSection(int index, int section) {
        return 0;
    }

    @Override
    public boolean isHiddenFieldAtIndexInSection(int index, int section) {
        return false;
    }

    @Override
    public UITargetDelegate.TouchUp actionForSection(int section) {
        return MutableProtocol.ViewController.super.actionForSection(section);
    }

    @Override
    public UIView.ALIGNMENT checkboxButtonPositionForSection(int section) {
        return null;
    }

    @Override
    public float checkboxButtonHeightForSection(int section) {
        return 0;
    }

    @Override
    public float checkboxButtonRowHeightForSection(int section) {
        return 0;
    }

    @Override
    public float checkboxButtonWidthForSection(int section) {
        return 0;
    }

    @Override
    public void switchBoolValueAtIndexPath(IndexPath indexPath) {

    }

    @Override
    public boolean isEditableSectionType(int section) {
        return false;
    }

    @Override
    public void didSwitchBoolValueAtIndexPath(IndexPath indexPath) {

    }

    @Override
    public void handleSelectionAtIndexPath(IndexPath indexPath) {

    }

    @Override
    public void presentSelectionVCAtIndexPath(UIViewController VC, IndexPath indexPath) {

    }

    @Override
    public float heightForStandardSelectionCell() {
        return 0;
    }

    @Override
    public float heightForSingleCellRowAtIndexPath(IndexPath indexPath) {
        return 0;
    }

    @Override
    public float attributedHeightForRowAtIndexPath(IndexPath indexPath) {
        return 0;
    }

    @Override
    public UIImage buttonImageForFieldAtIndexPath(IndexPath indexPath) {
        return null;
    }

    @Override
    public UITargetDelegate.TouchUp actionForFieldButtonAtIndexPath(IndexPath indexPath) {
        return MutableProtocol.ViewController.super.actionForFieldButtonAtIndexPath(indexPath);
    }

    @Override
    public void updateObjectDidUpdateKey(MutableProtocol.Field object, String key) {
        MutableProtocol.ViewController.super.updateObjectDidUpdateKey(object, key);
    }

    @Override
    public void handleTransitionToViewControllerForListItemAtIndexPath(UIViewController VC, UIViewController sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        pushViewController(VC, true);
    }

    public void handleTransitionToViewControllerForListItemAtIndexPath(UIViewController VC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        if (VC.transitionDelegate() == null) VC.setTransitionDelegate(this);
        if (!dispatchTransitionVCDelegateToTransitionToViewController(VC, this, item, indexPath))
            pushViewController(VC, true);
    }

    public boolean dispatchTransitionVCDelegateToTransitionToViewController (UIViewController VC, UIViewController sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        if (transitionVCDelegate() != null) {
            transitionVCDelegate().handleTransitionToViewControllerForListItemAtIndexPath(VC, sourceVC, item, indexPath);
            return true;
        }
        return false;
    }

    public void dispatchTransitionVCDelegateToDismissDestinationViewController (UIViewController VC) {
        if (transitionVCDelegate() != null) transitionVCDelegate().handleDismissDestinationViewController(VC);
    }

    @Override
    public void handleDismissDestinationViewController(UIViewController VC) {
        popViewControllerAnimated(true);
    }

    @Override
    public void setTextForRowAtIndexPath(IndexPath indexPath, BaseTableViewCell cell) {
//        [self defaultSetTextForRowAtIndexPath:indexPath inCell:cell];
    }

    @Override
    public void setStyleForRowAtIndexPath(IndexPath indexPath, BaseTableViewCell cell) {
//            [self defaultSetStyleForRowAtIndexPath:indexPath inCell:cell];
    }

    @Override
    public UITableViewCell.STYLE cellStyleForSubtitleRowAtIndexPath(IndexPath indexPath) {
        return UITableViewCell.STYLE.SUBTITLE;
    }

    @Override
    public UITableViewCell.ACCESSORY_TYPE accessoryTypeForRowAtIndexPath(IndexPath indexPath) {
        return UITableViewCell.ACCESSORY_TYPE.NONE;
//            return [self defaultAccessoryTypeForRowAtIndexPath:indexPath];
    }

    @Override
    public UITableViewCell.ACCESSORY_TYPE accessoryTypeForSelectedRowForListOfType(int type) {
        return UITableViewCell.ACCESSORY_TYPE.NONE;
    }

    @Override
    public UITableViewCell.ACCESSORY_TYPE accessoryTypeForDeselectedRowForListOfType(int type) {
        return UITableViewCell.ACCESSORY_TYPE.NONE;
    }

    @Override
    public UITableViewCell.ACCESSORY_TYPE accessoryTypeForSingleDeselectedRowForListOfType(int type) {
        return UITableViewCell.ACCESSORY_TYPE.NONE;
    }

    @Override
    public UITableViewCell.SELECTION_STYLE selectionStyleForListOfType(int type) {
        return UITableViewCell.SELECTION_STYLE.NONE;
    }

    @Override
    public boolean isSelectedRowAtIndexPath(IndexPath indexPath) {
        int type = listTypeForListInSection(indexPath.section);
        if (!canSelectItemsInListOfType(type)) return false;
        ViewContentProtocol.Placeholder object = listItemAtIndexPath(indexPath);
        Set set = selectedSetsInListOfType(type);
        return set.contains(object);
    }

    private Set selectedSetsInListOfType(int type) {
        return selectedSets.get(type);
    }

    @Override
    public <C extends BaseTableViewCell> C cellForListItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        return ItemsListProtocol.VC.super.cellForListItemAtIndexPath(item, indexPath);
    }

    @Override
    public void didSelectListItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        ItemsListProtocol.VC.super.didSelectListItemAtIndexPath(item, indexPath);
    }

    @Override
    public List<ViewContentProtocol.Placeholder> listItemsForListOfType(int type) {
        return object().UpdatedObject.arrayForSectionType(type);
    }

    @Override
    public List<ViewContentProtocol.Placeholder> listItemsForListInSection(int section) {
        return ItemsListProtocol.VC.super.listItemsForListInSection(section);
    }

    @Override
    public boolean canSelectItemsInListOfType(int type) {
        return ItemsListProtocol.VC.super.canSelectItemsInListOfType(type);
    }

    @Override
    public ViewContentProtocol.Placeholder listItemAtIndexPath(IndexPath indexPath) {
        return ItemsListProtocol.VC.super.listItemAtIndexPath(indexPath);
    }

    @Override
    public ViewControllerCallback transitioningViewControllerForItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        return createPresentingSelectionVCForItemAtIndexPath(item, indexPath);
    }

    @Override
    public void presentTransitioningViewControllerWithItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        UIViewController VC = transitioningViewControllerForItemAtIndexPath(item, indexPath).VC();
        handleTransitionToViewControllerForListItemAtIndexPath(VC, item, indexPath);
    }

    //region table view

    @Override
    public int numberOfRowsInSection(int section) {
        return 1;
    }

    @Override
    public UIView cellForRowAtIndexPath(IndexPath indexPath) {
        return singleCellForRowAtIndexPath(indexPath);
    }

    //endregion

    //region layout

    protected void reload() {
        ArrayList views = new ArrayList();
        UIView content = scrollview.getContentView();
        content.clearAllConstraints();

        for (int i = 0; i < numberOfSectionsInTableView(); i++) {
            if (isHeaderSection(i)) {
                UIView section = viewForHeaderInSection(i);
                views.add(section);
                content.addSubview(section);
                content.constraintHeightForView(section, heightForHeaderInSection(i));
            }
            for (int j = 0; j < numberOfRowsInSection(i); j++) {
                IndexPath indexPath = new IndexPath(i, j);
                UIView cell = cellForRowAtIndexPath(indexPath);
                views.add(cell);
                content.addSubview(cell);
                content.constraintHeightForView(cell, heightForRowAtIndexPath(indexPath));
            }
        }

        content.constraintVerticallyAllSides(views, 0, false);
        content.applyConstraints();
    }

    protected void constraintViews() {
        view().constraintSidesForView(scrollview);
        view().applyConstraints();
    }

    //endregion
}
