package com.prometheussoftware.auikit.uiviewcontroller.mutable;

import android.text.SpannableStringBuilder;
import android.view.Gravity;

import com.prometheussoftware.auikit.callback.ObjectCallback;
import com.prometheussoftware.auikit.callback.SuccessErrorCallback;
import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.classes.LabelAttributes;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.classes.UITargetDelegate;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.genericviews.UICheckbox;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.mutable.MutableProtocol;
import com.prometheussoftware.auikit.model.mutable.MutableUpdateObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.uiview.UIScrollview;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.UIControlProtocol;
import com.prometheussoftware.auikit.uiview.protocols.UIEditingAccessoryProtocol;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.tableview.BaseTableViewCell;
import com.prometheussoftware.auikit.uiviewcontroller.ItemsListProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.uiviewcontroller.ViewControllerTransition;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.ObjectUtility;
import com.prometheussoftware.auikit.utility.StringUtility;
import com.prometheussoftware.auikit.utility.UIAlert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MutableObjectViewController <ObjectType extends BaseModel & MutableProtocol.Field, UpdateObjectType extends BaseModel & MutableProtocol.Field> extends UIViewController implements MutableProtocol.ViewController<ObjectType, UpdateObjectType, MutableUpdateObject<ObjectType, UpdateObjectType>>, ViewControllerTransition, ItemsListProtocol.VC, ItemsListProtocol.EditingListVC, ItemsListProtocol.VCTransitionDelegate, ItemsListProtocol.UpdateDelegate, ViewControllerTransition.Delegate, UITableViewProtocol.TableViewData {

    private UIScrollview scrollview;
    protected HashMap<Integer, Set> selectedSets = new HashMap<>();
    private boolean isEditable;
    private IndexPath selectedIndexPath;
    private ItemsListProtocol.SelectionActionHandler selectedActionHandler;
    private ItemsListProtocol.UpdateDelegate updateDelegate;
    public boolean allowsMultipleSelection;

    public MutableObjectViewController() {
        super();
        init();
    }

    @Override
    public UIViewController init() {
        UIViewController self = super.init();
        initSelectedActionHandler();
        return self;
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        scrollview = new UIScrollview();
        view().addSubview(scrollview);
        constraintViews();
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setIsEditable(boolean editable) {
        isEditable = editable;;

        if (isEditable && canEditListsByDefault()) setEditing(true);

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
        initIsEditable();
        resetSelectedSets();
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
    public <T extends UpdateObjectType> void didResetUpdateObject(T object) {
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
    public <T extends UpdateObjectType> void setUpdatedObject(T updatedObject) {
        MutableUpdateObject obj = (MutableUpdateObject) ObjectUtility.objectWithParams(classForObject(), new ObjectUtility.Params(updatedObject.getClass(), updatedObject));
        setObject(obj);
    }

    @Override
    public Object returnedInSelectObject() {
        return selectedSets;
    }

    @Override
    public void initIsEditable() {
        setIsEditable(true);
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

    private boolean shouldHideCommentSection(int section) {
        return !isEditableSectionType(section) && valueForSection(section).isEmpty();
    }

    @Override
    public boolean canTransitionToPresentingSelectionVCInSection(int section) {
        return false;
    }

    @Override
    public String titleForSection(int section) {
        return object().UpdatedObject.titleForSectionType(section);
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
        return object().UpdatedObject.stringValueForSectionType(section);
    }

    @Override
    public SpannableStringBuilder attributedValueForSection(int section) {
        return null;
    }

    @Override
    public SpannableStringBuilder attributedSubvalueForSection(int section) {
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
    public int checkboxButtonHeightForSection(int section) {
        return 0;
    }

    @Override
    public int checkboxButtonRowHeightForSection(int section) {
        return 0;
    }

    @Override
    public int checkboxButtonWidthForSection(int section) {
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
    public int heightForRowAtIndexPath(IndexPath indexPath) {

        int section = indexPath.section;
        String value = valueForSection(section);
        boolean isEditable = canEditSection(section);

        switch (typeForSection(section)) {
            case VERTICAL_FIELD:
                return Dimensions.Int_80();

            case FIELD:
            case CHECKBOX:
            case STEPPER_FIELD:
                return App.constants().Extended_Row_Height();

            case CHECKBOX_BUTTON:
                return checkboxButtonRowHeightForSection(section);

            case LIST: {
                if (isAddIndexPath(indexPath))
                    return heightForStandardSelectionCell();
                return heightForNonEditingListRowAtIndexPath(indexPath);
            }

            case TITLE_FIELD: {
                if (isEditable)
                    return heightForTextFieldCellAtIndexPath(indexPath);
                return adjustHeight(heightForTitle(titleForSection(section)) + heightForTitle(value));
            }

            case COMMENT: {
                if (isEditable)
                    return heightForTextViewCellAtIndexPath(indexPath);
                return adjustHeight(heightForTitle(titleForSection(section)) + heightForTitle(value));
            }

            case CHECKBOX_COMMENT: {
                int titleHeight = adjustHeight(heightForTitle(titleForSection(section)) + App.constants().Table_Cell_Line_Height());

                if (isEditable)
                    return indexPath.row == UITableViewProtocol.TEXTVIEW_CELL_ROW.TITLE.intValue() ? titleHeight : App.constants().TextView_Medium_Height();
                return adjustHeight(heightForTitle(titleForSection(section)) + heightForTitle(value));
            }

            case SINGLE_CELL:
                return heightForSingleCellRowAtIndexPath(indexPath);

            case BLANK: {
                if (hasNoValueOrAattributedValueInSection(section))
                    return 0;
            }

            default: {
                if (hideSection(section) || shouldHideSelectionSection(section))
                    return 0;
                return attributedHeightForRowAtIndexPath(indexPath);
            }
        }
    }

    private boolean hasNoValueOrAattributedValueInSection(int section) {
        return valueForSection(section).length() == 0 && attributedValueForSection(section) == null &&
           subvalueForSection(section).length() == 0 && attributedSubvalueForSection(section) == null;
    }

    private int adjustHeight(int height) {
        return Math.max(height, heightForStandardSelectionCell());
    }

    private int heightForTitle(String title) {
        if (title.isEmpty()) return 0;

        int height = StringUtility.height(title, 0, view().getFrame().width()) + App.constants().Table_Cell_Line_Height();
        return height;
    }

    @Override
    public int heightForSingleCellRowAtIndexPath(IndexPath indexPath) {
        return 0;
    }

    @Override
    public int attributedHeightForRowAtIndexPath(IndexPath indexPath) {
        LabelAttributes attrs = labelAttributesForSection(indexPath.section);
        return adjustHeight(attrs.heightForWidth(view().getFrame().width()));
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
    public <T extends ViewContentProtocol.Placeholder> UITableViewCell.ACCESSORY_TYPE accessoryTypeForSelectedListItemInListOfType(T item, int type) {

        int section = listSectionForListType(type);
        ItemsListProtocol.LIST_ITEM_SELECTED_ACTION action = selectedActionHandler.action(section);
        switch (action) {
            case TRANSITION_TO_DETAIL:
                return UITableViewCell.ACCESSORY_TYPE.DISCLOSURE_INDICATOR;
            case SELECT:
                return UITableViewCell.ACCESSORY_TYPE.CHECKMARK;
            default:
                return UITableViewCell.ACCESSORY_TYPE.NONE;
        }
    }

    public boolean isAddIndexPath (IndexPath indexPath) {
        if (typeForSection(indexPath.section) != MutableProtocol.FIELD_TYPE.LIST)
            return false;

        int count = listItemsForListInSection(indexPath.section).size();
        return count <= indexPath.row;
    }

    public IndexPath indexPathForItem (ViewContentProtocol.Placeholder item) {
        for (int i=0; i<numberOfSectionsInTableView(); i++) {
            MutableProtocol.FIELD_TYPE type = typeForSection(i);
            if (type != MutableProtocol.FIELD_TYPE.LIST) continue;

            List arr = listItemsForListInSection(i);
            if (arr.contains(item)) {
                return new IndexPath(i, arr.indexOf(item));
            }
        }
        return null;
    }

    @Override
    public boolean isSelectedRowAtIndexPath(IndexPath indexPath) {
        int type = listTypeForListInSection(indexPath.section);
        if (!canSelectItemsInListOfType(type)) return false;
        ViewContentProtocol.Placeholder object = listItemAtIndexPath(indexPath);
        Set set = selectedSetsInListOfType(type);
        return set.contains(object);
    }

    protected Set selectedSetsInListOfType (int type) {
        return selectedSets.get(type);
    }

    @Override
    public void didSelectListItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        ItemsListProtocol.VC.super.didSelectListItemAtIndexPath(item, indexPath);
    }

    public <T extends ViewContentProtocol.Placeholder> void setItemsForListOfType(List<T> items, int type) {
        ArrayList arr = listItemsForListOfType(type);
        arr.clear();
        ArrayUtility.addUniqueObjectsFromArray(arr, items);
        object().UpdatedObject.setValueForSectionType(arr, type);

        HashMap<Integer, Set> selectedSets = this.selectedSets;
        resetSelectedSets();
        this.selectedSets = selectedSets;
        reload();
        didFinishUpdatesInListOfType(type);
    }

    public <T extends ViewContentProtocol.Placeholder> boolean addItemToListOfType (T item, int type) {
        if (item == null) return false;
        return addItemsToListOfType(List.of(item), type).size() == 0;
    }

    public <T extends ViewContentProtocol.Placeholder> void deleteItemFromListOfType (T item, int type) {
        if (item == null) return;
        deleteItemsFromListOfType(List.of(item), type);
    }

    public <T extends ViewContentProtocol.Placeholder> List<T> addItemsToListOfType (List<T> items,int type) {
        resetSelectedSets();
        List<IndexPath> addIndexPaths = new ArrayList<>();
        List<IndexPath> replaceIndexPaths = new ArrayList<>();

        ArrayList arr = listItemsForListOfType(type);
        List existing = ArrayUtility.addOrReplaceUniqueObjectsFromArray(arr, items);
        object().UpdatedObject.setValueForSectionType(arr, type);

        for (T item : items) {
            IndexPath path = indexPathForItem(item);
            if (path != null) {
                if (existing.contains(item))
                    replaceIndexPaths.add(path);
                else
                    addIndexPaths.add(path);
            }
        }

        if (canAddItemToListOfType(type)) {
            insertRowsAtIndexPaths(addIndexPaths);
            reloadIndexPaths(replaceIndexPaths);
        }
        else {
            reloadIndexPaths(addIndexPaths);
        }

        didFinishUpdatesInListOfType(type);

        return existing;
    }

    public <T extends ViewContentProtocol.Placeholder> void deleteItemsFromListOfType (List<T> items, int type) {
        List<IndexPath> indexPaths = new ArrayList<>();
        boolean canAdd = canAddItemToListOfType(type);

        for (T item : items) {
            IndexPath path = indexPathForItem(item);
            if (path != null)
                indexPaths.add(path);
        }

        listItemsForListOfType(type).removeAll(items);
        resetSelectedSets();
        if (canAdd) {
            removeRowsAtIndexPaths(indexPaths);
        }
        else {
            reload();
        }
        didFinishUpdatesInListOfType(type);
    }

    public void deleteAllItemsFromListOfType (int type) {
        ArrayList arr = listItemsForListOfType(type);
        arr.clear();
        object().UpdatedObject.setValueForSectionType(arr, type);
        reload();
        didFinishUpdatesInListOfType(type);
    }

    private void didFinishUpdatesInListOfType(int type) {
        if (updateDelegate != null) updateDelegate.itemsListVCDidUpdateItemsInListOfType(this, listItemsForListOfType(type), type);
    }

    @Override
    public <T extends ViewContentProtocol.Placeholder> ArrayList<T> listItemsForListOfType(int type) {
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
    public ViewControllerCallback transitioningViewControllerForItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        return createPresentingSelectionVCForItemAtIndexPath(item, indexPath);
    }

    @Override
    public void presentTransitioningViewControllerWithItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        UIViewController VC = transitioningViewControllerForItemAtIndexPath(item, indexPath).VC();
        handleTransitionToViewControllerForListItemAtIndexPath(VC, item, indexPath);
    }

    protected boolean shouldSelectItemsInListOfType (int type) {
        int max = maxMultipleSelectionForListOfType(type);
        return !allowsMultipleSelection || max == 1 || selectedSetsInListOfType(type).size() < max;
    }

    protected void setSelectedObject (ViewContentProtocol.Placeholder obj) {
        setSelectedObject(obj, false);
    }

    protected void setSelectedObject (ViewContentProtocol.Placeholder obj, boolean reload) {
        if (obj == null) return;

        IndexPath indexPath = indexPathForItem(obj);
        int type = listTypeForListInSection(indexPath.section);

        if (!canSelectItemsInListOfType(type)) return;

        Set set = selectedSetsInListOfType(type);
        Set selectedObjects = new HashSet<>(set);

        if (selectedObjects.isEmpty())
            selectedObjects = new HashSet<>();//TODO: was null
        if (!allowsMultipleSelection || maxMultipleSelectionForListOfType(type) == 1)
            selectedObjects.clear();
        if (shouldSelectItemsInListOfType(type))
            selectedObjects.add(obj);

        setSelectedObjectsWithSetInListOfType(selectedObjects, type, reload);
    }
    private void setDeselectedObject(ViewContentProtocol.Placeholder item) {
        setDeselectedObject(item, true);
    }

    private void setDeselectedObject(ViewContentProtocol.Placeholder obj, boolean reload) {
        if (obj == null) return;

        IndexPath indexPath = indexPathForItem(obj);
        int type = listTypeForListInSection(indexPath.section);
        Set set = selectedSetsInListOfType(type);
        Set selectedObjects = new HashSet<>(set);

        selectedObjects.remove(obj);
        setSelectedObjectsWithSetInListOfType(selectedObjects, type, reload);
    }


    protected void resetSelectedSets() {
        this.selectedSets = new HashMap<>();
    }

    protected void resetSelectedSetsReload() {
        resetSelectedSets();
        reload();
    }

    protected void resetSelectedSetsInListOfType (int type) {
        setSelectedObjectsWithSetInListOfType(new HashSet(), type, true);
    }

    protected void setAllSelectedSetsInListOfType (int type) {
        List arr = listItemsForListOfType(type);
        setSelectedObjectsWithSetInListOfType(new HashSet<>(arr), type, true);
    }

    protected void setSelectedObjectsWithSetInListOfType (Set selectedObjects, int type) {
        setSelectedObjectsWithSetInListOfType(selectedObjects, type, true);
    }

    protected void setSelectedObjectsWithSetInListOfType (Set selectedObjects, int type, boolean reload) {
        if (!canSelectItemsInListOfType(type)) return;

        if (selectedObjects == null)
            selectedSets.remove(type);
        else
            selectedSets.put(type, selectedObjects);
        if (reload) reload();
    }


    //region table view

    @Override
    public int numberOfRowsInSection(int section) {
        if (!hasValueForSection(section) || hideSection(section)) return 0;

        MutableProtocol.FIELD_TYPE type = typeForSection(section);

        if ((type == MutableProtocol.FIELD_TYPE.SELECTION || type == MutableProtocol.FIELD_TYPE.LABEL) &&
                shouldHideSelectionSection(section)) return 0;

        if (    type == MutableProtocol.FIELD_TYPE.TITLE_FIELD ||
                type == MutableProtocol.FIELD_TYPE.STEPPER_FIELD ||
                type == MutableProtocol.FIELD_TYPE.CHECKBOX_COMMENT ||
                type == MutableProtocol.FIELD_TYPE.COMMENT) {

            if (shouldHideCommentSection(section)) return 0;

            boolean isEditable = canEditSection(section);
            return isEditable && type != MutableProtocol.FIELD_TYPE.STEPPER_FIELD ? 2 : 1;
        }

        if (type == MutableProtocol.FIELD_TYPE.LIST)
            return numberOfRowsInListSection(section);

        return 1;
    }

    private int numberOfRowsInListSection(int section) {
        int count = ArrayUtility.nonnullArrayList(listItemsForListInSection(section)).size();
        int type = listTypeForListInSection(section);

        return isEditing() && canAddItemToListOfType(type) ? count + 1 : count;
    }


    @Override
    public UIView cellForRowAtIndexPath(IndexPath indexPath) {

        int section = indexPath.section;
        MutableProtocol.FIELD_TYPE type = typeForSection(section);
        boolean isEditable = canEditSection(section);

        switch (type) {
            case SELECTION:
            case LABEL: {
                BaseTableViewCell cell = new BaseTableViewCell();
                cell.setSelectionStyle(UITableViewCell.SELECTION_STYLE.NONE);
                cell.setAccessoryType(hasAccessoryForSection(section) ? UITableViewCell.ACCESSORY_TYPE.DISCLOSURE_INDICATOR : UITableViewCell.ACCESSORY_TYPE.NONE);

                LabelAttributes attrs = labelAttributesForSection(section);
                attrs.setAttributedTitlesForLabel(cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()), cell.getLabel(UITableViewCell.LABEL_TYPE.DETAIL_TEXT.intValue()));

                return cell;
            }

            case CHECKBOX: {
                return radioButtonCellInSection(section, isEditable, false);
            }

            case LIST: {
                int listType = listTypeForListInSection(section);

                if (isAddIndexPath(indexPath) && canAddItemToListOfType(listType)) {
                    BaseTableViewCell.Editing cell = new BaseTableViewCell.Editing();
                    cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()).setText(titleForAddCellInListOfType(listType));
                    cell.setEditingStyle(UITableViewCell.EDITING_STYLE.INSERT);
                    return cell;
                }

                ViewContentProtocol.Placeholder item = listItemAtIndexPath(indexPath);
                return cellForListItemAtIndexPath(item, indexPath);
            }

            default: {
                UIView cell = singleCellForRowAtIndexPath(indexPath);
                cell.setUserInteractionEnabled(userInteractionEnabledForSingleCellAtIndexPath(indexPath));
                return cell;
            }
        }
    }

    protected UICheckbox radioButtonCellInSection(int section, boolean enabled, boolean singleLine) {
        UIEdgeInsets insets = radioButtonCellInsetsForSection(section);
        UICheckbox cell = (UICheckbox) ObjectUtility.objectWithParams(radioButtonCellClassForSection(section), new ObjectUtility.Params(insets.getClass(), insets));

        cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()).setFont(App.theme().Medium_Bold_Font());
        cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()).setText(titleForSection(section));
        cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()).getView().setGravity(Gravity.CENTER_VERTICAL);
        cell.checkView().setOn(boolValueForSection(section));
        cell.checkView().setEnabled(enabled);
        cell.checkView().setUserInteractionEnabled(false);
        if (!singleLine) cell.setMultiline(UITableViewCell.LABEL_TYPE.TEXT.intValue());

        return cell;
    }

    private LabelAttributes labelAttributesForSection(int section) {
        return LabelAttributes.init(
        titleForSection(section),
        subtitleForSection(section),
        valueForSection(section),
        subvalueForSection(section),
        placeholderTitleForSection(section),
        attributedValueForSection(section),
        attributedSubvalueForSection(section),
        labelDelimiterForSection(section),
        sublabelDelimiterForSection(section));
    }

    @Override
    public void setMultiSelectEnabled(boolean multiSelectEnabled) {
        this.allowsMultipleSelection = multiSelectEnabled;
    }

    @Override
    public boolean allowsMultipleSelection() {
        return this.allowsMultipleSelection;
    }

    @Override
    public void didSelectRowAtIndexPath(IndexPath indexPath) {
        MutableProtocol.FIELD_TYPE type = typeForSection(indexPath.section);
        int section = indexPath.section;

        switch (type) {
            case CHECKBOX:
            case CHECKBOX_BUTTON:
            case CHECKBOX_COMMENT: {
                if (canEditSection(section)) return;
                switchBoolValueAtIndexPath(indexPath);
            }
            break;

            case LABEL:
            case SELECTION:
            case SINGLE_CELL: {
                handleSelectionAtIndexPath(indexPath);
            }
            break;

            case LIST: {
                this.selectedIndexPath = indexPath;
                ViewContentProtocol.Placeholder item = listItemAtIndexPath(indexPath);
                if (item != null)
                    handleDidSelectListItemAtIndexPath(item, indexPath);
//                else

            }
            break;

            default:
                break;
        }
    }

    private void handleDidSelectListItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        boolean selected = isSelectedRowAtIndexPath(indexPath);
        int section = indexPath.section;

        if (selectedActionHandler.action(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.TRANSITION_TO_DETAIL) {
            UIViewController VC = transitioningViewControllerForItemAtIndexPath(item, indexPath).VC();
            if (VC != null)
                handleTransitionForViewController(VC, this, item, indexPath);
            else
                dispatchUpdateDelegateToSetSelected(!selected, item);
        }
        else if (selectedActionHandler.action(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.SELECT ||
                selectedActionHandler.action(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.SHOW_DETAIL) {
            if (selected) {
                setDeselectedObject(item, false);
            }
            else {
                int type = listTypeForListInSection(section);
                if (shouldSelectItemsInListOfType(type)) {
                    setSelectedObject(item, false);
                    didSelectListItemAtIndexPath(item, indexPath);
                }
            }

            dispatchUpdateDelegateToSetSelected(!selected, item);
            reload();

            if (selectedActionHandler.action(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.SELECT &&
                    !allowsMultipleSelection)
                dispathTransitionDelegateToReturnWithObject(returnedInSelectObject());
        }
        else {
            setSelectedObject(item, false);
            didSelectListItemAtIndexPath(item, indexPath);
            dispatchUpdateDelegateToSetSelected(!selected, item);
            reload();
        }
    }

    private void dispatchUpdateDelegateToSetSelected(boolean selected, ViewContentProtocol.Placeholder item) {
        if (updateDelegate != null) {
            updateDelegate.itemsListVCDidSetSelectedAtIndexPath(this, selected, item, indexPathForItem(item));
        }
    }

    private void setDidSelectRowAtIndexPath(UIView cell, IndexPath indexPath) {
        if (!(cell instanceof UIControlProtocol)) return;

        UIControlProtocol control = (UIControlProtocol)cell;
        control.addTarget(this, (UITargetDelegate.TouchUp) sender -> {
            didSelectRowAtIndexPath(indexPath);
        });
    }

    private void setEditingForRowAtIndexPath(UIView cell, IndexPath indexPath) {
        if (!(cell instanceof UIEditingAccessoryProtocol)) return;

        UIEditingAccessoryProtocol control = (UIEditingAccessoryProtocol)cell;
        control.addEditingTarget(this, (UITargetDelegate.TouchUp) sender -> {
            tableViewCommitEditingStyleForRowAtIndexPath(tableViewEditingStyleForRowAtIndexPath(indexPath), indexPath);
        });
    }

    private void setAccessoryForRowAtIndexPath(UIView cell, IndexPath indexPath) {
        if (!(cell instanceof UIEditingAccessoryProtocol)) return;

        UIEditingAccessoryProtocol control = (UIEditingAccessoryProtocol)cell;
        control.addAccessoryTarget(this, (UITargetDelegate.TouchUp) sender -> {
            didSelectRowAtIndexPath(indexPath);
        });
    }

    @Override
    public boolean tableViewCanEditRowAtIndexPath(IndexPath indexPath) {
        return typeForSection(indexPath.section) == MutableProtocol.FIELD_TYPE.LIST && canEditSection(indexPath.section);
    }

    @Override
    public UITableViewCell.EDITING_STYLE tableViewEditingStyleForRowAtIndexPath(IndexPath indexPath) {
        if (!isEditing())
            return UITableViewCell.EDITING_STYLE.NONE;
        if (isAddIndexPath(indexPath) && canAddItemToListOfType(indexPath.section))
            return UITableViewCell.EDITING_STYLE.INSERT;
        else if (canDeleteFromListOfType(indexPath.section))
            return UITableViewCell.EDITING_STYLE.DELETE;
        return UITableViewCell.EDITING_STYLE.NONE;
    }

    @Override
    public void tableViewCommitEditingStyleForRowAtIndexPath(UITableViewCell.EDITING_STYLE editingStyle, IndexPath indexPath) {

        selectedIndexPath = indexPath;
        int section = indexPath.section;
        int type = listTypeForListInSection(section);

        if (editingStyle == UITableViewCell.EDITING_STYLE.INSERT) {
            performInsertToListOfTypeAtIndexPath(type, indexPath, obj -> {
                didFinishCommitEditingStyleForRowAtIndexPath(editingStyle, indexPath);
            });
        }
        else if (editingStyle == UITableViewCell.EDITING_STYLE.DELETE) {
            ViewContentProtocol.Placeholder item = listItemAtIndexPath(indexPath);
            willDeleteItemForRowAtIndexPath(item, indexPath, new SuccessErrorCallback() {
                @Override
                public void done(boolean success, Error error) {
                    if (success && error == null) {
                        deleteItemFromListOfType(item, type);
                        didDeleteItemForRowAtIndexPath(item, indexPath);
                    }
                    else if (error != null) {
                        UIAlert.OKAlert(App.constants().Delete_Failed_STR(), error.getLocalizedMessage());
                    }
                    didFinishCommitEditingStyleForRowAtIndexPath(editingStyle, indexPath);
                }
            });
        }
    }

    protected <T extends ViewContentProtocol.Placeholder> void performInsertToListOfTypeAtIndexPath (int type, IndexPath indexPath, ObjectCallback<T> callback) {

        willAddItemToListOfType(type, obj -> {
            if (obj == null || !shouldAddItemToListOfType(obj, type)) {
                handleDidSelectListItemAtIndexPath(obj, indexPath);
            } else {
                addItemToListOfType(obj, type);
                didAddItemToListOfType(obj, type);
            }
            if (callback != null) callback.returns((T) obj);
        });
    }

    @Override
    public ItemsListProtocol.SelectionActionHandler selectedActionHandler() {
        return selectedActionHandler;
    }

    public void setSelectedActionHandler(ItemsListProtocol.SelectionActionHandler selectedActionHandler) {
        this.selectedActionHandler = selectedActionHandler;
    }

    protected void initSelectedActionHandler() {
        setSelectedActionHandler(new ItemsListProtocol.SelectionActionHandler() {
            @Override
            public ItemsListProtocol.LIST_ITEM_SELECTED_ACTION action(int section) {
                MutableProtocol.FIELD_TYPE type = typeForSection(section);
                return type == MutableProtocol.FIELD_TYPE.LIST ? ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.TRANSITION_TO_DETAIL : ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.NONE;
            }
        });
    }

    private boolean isEmailOrPhoneSectionType(int section) {
        return object().UpdatedObject.isEmailSectionType(section) || object().UpdatedObject.isPhoneSectionType(section);
    }

    @Override
    public int listSectionForListType(int type) {
        for (int i=0; i<numberOfSectionsInTableView(); i++) {
            if (listTypeForListInSection(i) == type) return i;
        }
        return type;
    }

    //endregion

    //region layout

    protected void reload() {

        int width = view().getFrame().width();
        ArrayList views = new ArrayList();
        UIView content = scrollview.getContentView();
        content.clearAllConstraints();

        for (int i = 0; i < numberOfSectionsInTableView(); i++) {

            MutableProtocol.FIELD_TYPE type = typeForSection(i);
            int sec = listTypeForListInSection(i);
            boolean canSelect = canSelectSection(i);
            boolean isEmail = isEmailOrPhoneSectionType(i);
            boolean hasAccessory = hasAccessoryForSection(i);
            boolean isEditing = type == MutableProtocol.FIELD_TYPE.LIST && (canAddItemToListOfType(sec) || canDeleteFromListOfType(sec));

            if (isHeaderSection(i)) {
                UIView section = viewForHeaderInSection(i);
                views.add(section);

                content.addSubview(section);
                content.constraintHeightForView(section, heightForHeaderInSection(i));
                content.constraintWidthForView(section, width);
            }
            for (int j = 0; j < numberOfRowsInSection(i); j++) {

                IndexPath indexPath = new IndexPath(i, j);
                int height = heightForRowAtIndexPath(indexPath);
                UIView cell = cellForRowAtIndexPath(indexPath);
                views.add(cell);

                content.addSubview(cell);
                content.constraintHeightForView(cell, height);
                content.constraintWidthForView(cell, width);

                if (canSelect || isEmail)
                    setDidSelectRowAtIndexPath(cell, indexPath);
                if (hasAccessory)
                    setAccessoryForRowAtIndexPath(cell, indexPath);
                if (isEditing)
                    setEditingForRowAtIndexPath(cell, indexPath);
            }
        }

        content.constraintVerticallyAllSides(views, 0, false);
        content.applyConstraints();
    }

    protected void constraintViews() {
        view().constraintSidesForView(scrollview);
        view().applyConstraints();
    }

    protected void reloadIndexPaths(List<IndexPath> replaceIndexPaths) {
        reload();
    }

    protected void insertRowsAtIndexPaths(List<IndexPath> addIndexPaths) {
        reload();
    }

    protected void removeRowsAtIndexPaths(List<IndexPath> indexPaths) {
        reload();
    }

    //endregion
}
