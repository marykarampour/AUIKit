package com.prometheussoftware.auikit.uiviewcontroller;

import android.text.SpannableStringBuilder;

import com.prometheussoftware.auikit.callback.ObjectCallback;
import com.prometheussoftware.auikit.callback.SuccessErrorCallback;
import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.classes.UIImage;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.tableview.BaseTableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;
import com.prometheussoftware.auikit.utility.ArrayUtility;
import com.prometheussoftware.auikit.utility.ObjectUtility;

import java.util.ArrayList;
import java.util.List;

public interface ItemsListProtocol {

    interface VCTransitionDelegate {
        default void handleTransitionToViewControllerForListItemAtIndexPath (UIViewController VC, UIViewController sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default void handleDismissDestinationViewController (UIViewController VC) {}
    }

    interface VC {
        /** @brief Default uses textLabelAtIndexPath and detailTextLabelAtIndexPath and  attributedTextLabelAtIndexPath and attributedDetailTextLabelAtIndexPath. */
        default <T extends ViewContentProtocol.Placeholder> void setTextForListItemAtIndexPath (T item, IndexPath indexPath, BaseTableViewCell cell) {
            SpannableStringBuilder attrTitle = attributedTextLabelForListItemAtIndexPath(item, indexPath);
            SpannableStringBuilder attrSubtitle = attributedDetailTextLabelForListItemAtIndexPath(item, indexPath);

            if (attrTitle != null) {
                cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()).setText(attrTitle);
            }
            else {
                cell.getLabel(UITableViewCell.LABEL_TYPE.TEXT.intValue()).setText(textLabelAtIndexPath(indexPath));
            }

            if (attrSubtitle != null) {
                cell.getLabel(UITableViewCell.LABEL_TYPE.DETAIL_TEXT.intValue()).setText(attrSubtitle);
            }
            else {
                cell.getLabel(UITableViewCell.LABEL_TYPE.DETAIL_TEXT.intValue()).setText(detailTextLabelAtIndexPath(indexPath));
            }
        }

        /** @brief Default sets accessoryType and selectionStyle. */
        default <T extends ViewContentProtocol.Placeholder> void setStyleForListItemAtIndexPath (T item, IndexPath indexPath, BaseTableViewCell cell) {

            int sectionType = sectionTypeForIndexPath(indexPath);
            cell.setSelectionStyle(selectionStyleForListOfType(sectionType));

            UITableViewCell.ACCESSORY_TYPE type = accessoryTypeForListItemAtIndexPath(item, indexPath);

            if (type == UITableViewCell.ACCESSORY_TYPE.CUSTOM) {
                cell.getRightView().setOnImage(accessoryCustomOnImageForListItemAtIndexPath(item, indexPath));
                cell.getRightView().setOffImage(accessoryCustomOffImageForListItemAtIndexPath(item, indexPath));
            }

            cell.setAccessoryType(type);
        }

        default <T extends ViewContentProtocol.Placeholder> UITableViewCell.ACCESSORY_TYPE accessoryTypeForListItemAtIndexPath(T item, IndexPath indexPath) {

            int sectionType = sectionTypeForIndexPath(indexPath);

            if (!allowsMultipleSelection()) {
                return isSelectedRowAtIndexPath(indexPath) ? accessoryTypeForSelectedListItemInListOfType(item, sectionType) : accessoryTypeForSingleDeselectedListItemInListOfType(item, sectionType);
            }
            else {
                return isSelectedRowAtIndexPath(indexPath) ? accessoryTypeForSelectedListItemInListOfType(item, sectionType) :  accessoryTypeForDeselectedListItemInListOfType(item, sectionType);
            }
        }

        default <T extends ViewContentProtocol.Placeholder> UIImage accessoryCustomOnImageForListItemAtIndexPath(T item, IndexPath indexPath) {
            return null;
        }

        default <T extends ViewContentProtocol.Placeholder> UIImage accessoryCustomOffImageForListItemAtIndexPath(T item, IndexPath indexPath) {
            return null;
        }

        default boolean allowsMultipleSelection() { return false; }
        default int sectionTypeForIndexPath(IndexPath indexPath) { return listTypeForListInSection(indexPath.section); }

        /** @brief Default is UITableViewCellAccessoryCheckmark. */
        default <T extends ViewContentProtocol.Placeholder> UITableViewCell.ACCESSORY_TYPE accessoryTypeForSelectedListItemInListOfType (T item, int type) {
            return UITableViewCell.ACCESSORY_TYPE.NONE; }
        /** @brief Default is UITableViewCellAccessoryNone. */
        default <T extends ViewContentProtocol.Placeholder> UITableViewCell.ACCESSORY_TYPE accessoryTypeForDeselectedListItemInListOfType(T item, int sectionType) { return UITableViewCell.ACCESSORY_TYPE.NONE; }
        /** @brief Default is UITableViewCellAccessoryDisclosureIndicator. */
        default <T extends ViewContentProtocol.Placeholder> UITableViewCell.ACCESSORY_TYPE accessoryTypeForSingleDeselectedListItemInListOfType (T item, int type) { return UITableViewCell.ACCESSORY_TYPE.NONE; }

        /** @brief If allowsMultipleSelection it is defaulted to UITableViewCellSelectionStyleNone else UITableViewCellSelectionStyleDefault. */
        default UITableViewCell.SELECTION_STYLE selectionStyleForListOfType (int type)  {
            if (!allowsMultipleSelection())
                return UITableViewCell.SELECTION_STYLE.DEFAULT;
            return UITableViewCell.SELECTION_STYLE.NONE;
        }

        /** @brief Default returns YES. */
        default boolean canSelectSection (int section) { return true; }
        default String noItemAvailableTitleForListOfType (int type) { return ""; }
        default <T extends ViewContentProtocol.Placeholder> String textLabelAtIndexPath (IndexPath indexPath) { return listItemAtIndexPath(indexPath).title(); }
        default <T extends ViewContentProtocol.Placeholder> String detailTextLabelAtIndexPath (IndexPath indexPath) { return listItemAtIndexPath(indexPath).subtitle(); }
        default <T extends ViewContentProtocol.Placeholder> SpannableStringBuilder attributedTextLabelForListItemAtIndexPath (T item, IndexPath indexPath) {
            if (item == null) return null;
            return item.attributedTitle();
        }
        default <T extends ViewContentProtocol.Placeholder> SpannableStringBuilder attributedDetailTextLabelForListItemAtIndexPath (T item, IndexPath indexPath) {
            if (item == null) return null;
            return item.attributedSubtitle();
        }
        default boolean isSelectedRowAtIndexPath (IndexPath indexPath) { return false; }
        default <T extends ViewContentProtocol.Placeholder, C extends BaseTableViewCell> C cellForListItemAtIndexPath (T item, IndexPath indexPath) {
            UITableViewCell.STYLE style = cellStyleForSubtitleListItemAtIndexPath(item, indexPath);
            BaseTableViewCell cell = new BaseTableViewCell.Editing(style);
            setTextForListItemAtIndexPath(item, indexPath, cell);
            setStyleForListItemAtIndexPath(item, indexPath, cell);
            return (C) cell;
        }

        default <T extends ViewContentProtocol.Placeholder> UITableViewCell.STYLE cellStyleForSubtitleListItemAtIndexPath(T item, IndexPath indexPath) { return UITableViewCell.STYLE.SUBTITLE; }

        /** @brief Called in tableView: didSelectRowAtIndexPath or methods called by it when a section is of type list or
        tableView: commitEditingStyle: forRowAtIndexPath for UITableViewCellEditingStyleInsert.
        Corresponds to selectedActionHandler of type MKU_LIST_ITEM_SELECTED_ACTION_SELECT and MKU_LIST_ITEM_SELECTED_ACTION_SHOW_DETAIL */
        default void didSelectListItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default <T extends ViewContentProtocol.Placeholder> List<T> listItemsForListOfType (int type) { return new ArrayList<>(); }
        default <T extends ViewContentProtocol.Placeholder> List<T> listItemsForListInSection (int section) { return listItemsForListOfType(listTypeForListInSection(section)); }
        default boolean canSelectItemsInListOfType (int type) { return false; }
        default int listTypeForListInSection (int section) { return section; }
        default int listSectionForListType (int type) { return type; }
        default <T extends ViewContentProtocol.Placeholder> T listItemAtIndexPath (IndexPath indexPath) {
            int section = indexPath.section;
            int type = sectionTypeForIndexPath(indexPath);
            T item = ArrayUtility.safeGet(listItemsForListOfType(type), indexPath.row);
            if (item != null) return item;
            return ArrayUtility.safeGet(listItemsForListInSection(section), indexPath.row);
        }

        /** @brief Retun view controller to be pushed when an item is selected. It will be called in
        willAddItemToListOfType (int)type withCompletion as well. Return nil to do custom actions. */
        default ViewControllerCallback transitioningViewControllerForItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) { return null; }

        /** @brief Pushes the view controller returned by transitioningViewControllerForItem:atIndexPath when an item is selected. It will be called in willAddItemToListOfType (int)type withCompletion as well. Return nil in transitioningViewControllerForItem to do custom actions, or override this. */
        default void presentTransitioningViewControllerWithItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {}

        /** @brief By default is is self. If this view is within a container, it can be assigned the container to handle
        transitions when an item is selected. */
        default VCTransitionDelegate transitionVCDelegate() { return null; }
        default void setTransitionVCDelegate() {}

        default <T extends ViewContentProtocol.Placeholder> boolean dispatchTransitionVCDelegateToTransitionToViewController (UIViewController VC, UIViewController sourceVC, T item, IndexPath indexPath) {
            if (transitionVCDelegate() != null) {
                transitionVCDelegate().handleTransitionToViewControllerForListItemAtIndexPath(VC, sourceVC, item, indexPath);
                return true;
            }
            return false;
        }

        default <V extends UIViewController & ViewControllerTransition.Delegate> void handleTransitionForViewController (UIViewController VC, V sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {
            if (VC.transitionDelegate() == null) VC.setTransitionDelegate(sourceVC);

            if (!dispatchTransitionVCDelegateToTransitionToViewController(VC, sourceVC, item, indexPath)) {
                sourceVC.pushViewController(VC, true);
            }
        }
    }

    interface ListVC {
        default <T extends ViewContentProtocol.Placeholder> ViewControllerCallback createPresentingSelectionVCForItemAtIndexPath (T item, IndexPath indexPath) {
            return () -> { return null; };
        }
    }

    interface EditingListVC extends ViewControllerTransition {

        default boolean canAddItemToListOfType (int type) { return false; }
        default boolean canDeleteFromListOfType (int type) { return false; }
        default boolean canMoveItemsInListOfType (int type) { return false; }
        default int maxMultipleSelectionForListOfType (int type) { return 1; }
        /** @brief Return YES if self.editing should be always YES. The navbar will not have the edit button in this case. Default is NO. */
        default boolean canEditListsByDefault() { return false; }
        default String titleForAddCellInListOfType (int type) { return App.constants().Add_New_Item_STR(); }
        default int heightForNonEditingListRowAtIndexPath (IndexPath indexPath) { return 0; }

        /** @brief Peform any actions required to delete this item. In the completion, this ittem will be removed from the list. */
        default <T extends ViewContentProtocol.Placeholder> void willDeleteItemForRowAtIndexPath (T item, IndexPath indexPath, SuccessErrorCallback completion) {}

        /** @brief Peform any actions required after deleting this item such as updating navbar. */
        default <T extends ViewContentProtocol.Placeholder> void didDeleteItemForRowAtIndexPath (T item, IndexPath indexPath) {}

        /** @brief Peform any actions required to construct this item. In the completion, this ittem will be add to the list. */
        default <T extends ViewContentProtocol.Placeholder> void willAddItemToListOfType (int type, ObjectCallback<T> callback) {
            T item = newItemInListOfType(type);
            callback.returns(item);
        }

        /** @brief Peform any actions required after adding this item such as updating navbar. */
        default <T extends ViewContentProtocol.Placeholder> void didAddItemToListOfType (T item, int type) {}

        /** @brief If YES, the item is added on spot, if NO, presentTransitioningViewControllerWithItem is called to handle adding the new item.
        Default returns NO. */
        default <T extends ViewContentProtocol.Placeholder> boolean shouldAddItemToListOfType(T item, int type) { return false; }

        /** @brief Called after insert or delete actions are performed. Use to update other elements such as navbar. */
        default void didFinishCommitEditingStyleForRowAtIndexPath (UITableViewCell.EDITING_STYLE editingStyle, IndexPath indexPath) {}

        /** By default when viewController: didReturnWithResultType: object: is triggered, the object is either added or replaced if existing, or
        the object is of a different type than that of items which results in failure, in any case this method is called to handle the update's result.
        @param update YES if update succeeds and NO if it fails. */
        default <T extends ViewContentProtocol.Placeholder> void didUpdateItemAtIndexPath (boolean update, T item, IndexPath indexPath) {}

        /** @brief Return a new item to be used in willAddItemToListOfType:(NSUInteger)type withCompletion. Default uses  itemsClass to initialize it. */
        default <T extends ViewContentProtocol.Placeholder> T newItemInListOfType (int type) {
            return (T) ObjectUtility.objectWithParams(itemsClassInListOfType(type));
        }

        default Class itemsClassInListOfType (int type) { return null; }

        /** @brief The object which will be retunred to transitionDelegate when Done / Next button ia pressed, e.g., selected items. */
        default Object returnedInSelectObject() { return null; }
        /** @brief The object which will be retunred to transitionDelegate when Close button is pressed, e.g., items. */
        default Object returnedInCloseObject() { return null; }

        default <T extends ViewContentProtocol.Placeholder> void itemDidMoveFromIndexToIndexInListOfType (T item1, int index1, int index2,  int type) {}

        default ItemsListProtocol.SelectionActionHandler selectedActionHandler() {
            return new SelectionActionHandler() {
                @Override
                public LIST_ITEM_SELECTED_ACTION action(int section) {
                    return LIST_ITEM_SELECTED_ACTION.NONE;
                }
            };
        }
    }

    interface UpdateDelegate {
        default <V extends UIViewController & EditingListVC & VC> void itemsListVCDidUpdateItemsInListOfType(V VC, List<ViewContentProtocol.Placeholder> items, int type) {}
        default <V extends UIViewController & EditingListVC & VC> void itemsListVCDidUpdateItemAtIndexPath (V VC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default <V extends UIViewController & EditingListVC & VC> void itemsListVCDidSetSelectedAtIndexPath (V VC, boolean selected, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
    }

    interface SelectionActionHandler {
        default LIST_ITEM_SELECTED_ACTION action(int section) { return LIST_ITEM_SELECTED_ACTION.NONE; }
    }

    enum LIST_ITEM_SELECTED_ACTION {
        NONE,
        SELECT,
        SHOW_DETAIL,
        TRANSITION_TO_DETAIL
    }
}