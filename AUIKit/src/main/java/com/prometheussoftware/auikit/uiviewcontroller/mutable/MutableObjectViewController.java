package com.prometheussoftware.auikit.uiviewcontroller.mutable;

import android.text.SpannableStringBuilder;
import android.view.Gravity;

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
import com.prometheussoftware.auikit.model.mutable.FieldModel;
import com.prometheussoftware.auikit.model.mutable.MutableProtocol;
import com.prometheussoftware.auikit.model.mutable.MutableUpdateObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.uiview.UIScrollview;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiview.protocols.UIControlProtocol;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.tableview.BaseTableViewCell;
import com.prometheussoftware.auikit.uiviewcontroller.ItemsListProtocol;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.uiviewcontroller.ViewControllerTransition;
import com.prometheussoftware.auikit.utility.ObjectUtility;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class MutableObjectViewController <ObjectType extends BaseModel & MutableProtocol.Field, UpdateObjectType extends BaseModel & MutableProtocol.Field> extends UIViewController implements MutableProtocol.ViewController<ObjectType, UpdateObjectType, MutableUpdateObject<ObjectType, UpdateObjectType>>, ViewControllerTransition, ItemsListProtocol.VC, ItemsListProtocol.EditingListVC, ItemsListProtocol.VCTransitionDelegate, ItemsListProtocol.UpdateDelegate, ViewControllerTransition.Delegate, UITableViewProtocol.TableViewData, ItemsListProtocol.SelectionActionHandler {

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
    public void viewDidLoad() {
        super.viewDidLoad();
        scrollview = new UIScrollview();
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
        FieldModel object = (FieldModel) ObjectUtility.objectWithParams(updatedObject.getClass());
        MutableUpdateObject obj = (MutableUpdateObject) ObjectUtility.objectWithParams(classForObject(), new ObjectUtility.Params(object.getClass(), object));
        setObject(obj);
    }

    @Override
    public Object returnedInSelectObject() {
        return selectedSets;
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


    boolean isAddIndexPath (IndexPath indexPath) {
        if (typeForSection(indexPath.section) != MutableProtocol.FIELD_TYPE.LIST)
            return false;

        int count = listItemsForListInSection(indexPath.section).size();
        return count <= indexPath.row;
    }

    IndexPath indexPathForItem (ViewContentProtocol.Placeholder item) {
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
        return 1;
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
                attrs.setAttributedTitlesForLabel(cell.getTitleLabel(), null);

                return cell;
            }

            case CHECKBOX: {
                return radioButtonCellInSection(section, isEditable, false);
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

        cell.getTitleLabel().setFont(App.theme().Medium_Bold_Font());
        cell.getTitleLabel().setText(titleForSection(section));
        cell.getTitleLabel().getView().setGravity(Gravity.CENTER_VERTICAL);
        cell.checkView().setOn(boolValueForSection(section));
        cell.checkView().setEnabled(enabled);
        cell.checkView().setUserInteractionEnabled(false);
        if (!singleLine) cell.setMultiline();

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
                handleDidSelectListItemAtIndexPath(item, indexPath);
            }
            break;

            default:
                break;
        }
    }

    private void handleDidSelectListItemAtIndexPath(ViewContentProtocol.Placeholder item, IndexPath indexPath) {
        boolean selected = isSelectedRowAtIndexPath(indexPath);
        int section = indexPath.section;

        if (selectedActionHandler(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.TRANSITION_TO_DETAIL) {
            UIViewController VC = transitioningViewControllerForItemAtIndexPath(item, indexPath).VC();
            if (VC != null)
                handleTransitionForViewController(VC, this, item, indexPath);
            else
                dispatchUpdateDelegateToSetSelected(!selected, item);
        }
        else if (selectedActionHandler(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.SELECT ||
                selectedActionHandler(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.SHOW_DETAIL) {
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

            if (selectedActionHandler(section) == ItemsListProtocol.LIST_ITEM_SELECTED_ACTION.SELECT &&
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

    public ItemsListProtocol.SelectionActionHandler getSelectedActionHandler() {
        return selectedActionHandler;
    }

    public void setSelectedActionHandler(ItemsListProtocol.SelectionActionHandler selectedActionHandler) {
        this.selectedActionHandler = selectedActionHandler;
    }

    private boolean isEmailOrPhoneSectionType(int section) {
        return object().UpdatedObject.isEmailSectionType(section) || object().UpdatedObject.isPhoneSectionType(section);
    }


    //endregion

    //region layout

    protected void reload() {

        int width = view().getFrame().width();
        ArrayList views = new ArrayList();
        UIView content = scrollview.getContentView();
        content.clearAllConstraints();

        for (int i = 0; i < numberOfSectionsInTableView(); i++) {
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

                if (canSelectSection(i) || isEmailOrPhoneSectionType(i))
                    setDidSelectRowAtIndexPath(cell, indexPath);
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
