package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.callback.SuccessErrorCallback;
import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.tableview.BaseTableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public interface ItemsListProtocol {

    interface VCTransitionDelegate {
        default void handleTransitionToViewControllerForListItemAtIndexPath (UIViewController VC, UIViewController sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default void handleDismissDestinationViewController (UIViewController VC) {}
    }

    interface VC {
        /** @brief Default uses textLabelAtIndexPath and detailTextLabelAtIndexPath and  attributedTextLabelAtIndexPath and attributedDetailTextLabelAtIndexPath. */
        default void setTextForRowAtIndexPath (IndexPath indexPath, BaseTableViewCell cell) {}
        /** @brief Default sets accessoryType and selectionStyle. */
        default void setStyleForRowAtIndexPath (IndexPath indexPath, BaseTableViewCell cell) {}
        default UITableViewCell.STYLE cellStyleForSubtitleRowAtIndexPath (IndexPath indexPath) { return UITableViewCell.STYLE.DEFAULT; }
        default UITableViewCell.ACCESSORY_TYPE accessoryTypeForRowAtIndexPath (IndexPath indexPath) { return UITableViewCell.ACCESSORY_TYPE.NONE; }
        /** @brief Default is UITableViewCellAccessoryCheckmark. */
        default UITableViewCell.ACCESSORY_TYPE accessoryTypeForSelectedRowForListOfType (int type) { return UITableViewCell.ACCESSORY_TYPE.NONE; }
        /** @brief Default is UITableViewCellAccessoryNone. */
        default UITableViewCell.ACCESSORY_TYPE accessoryTypeForDeselectedRowForListOfType (int type) { return UITableViewCell.ACCESSORY_TYPE.NONE; }
        /** @brief Default is UITableViewCellAccessoryDisclosureIndicator. */
        default UITableViewCell.ACCESSORY_TYPE accessoryTypeForSingleDeselectedRowForListOfType (int type) { return UITableViewCell.ACCESSORY_TYPE.NONE; }
        /** @brief If allowsMultipleSelection it is defaulted to UITableViewCellSelectionStyleNone else UITableViewCellSelectionStyleDefault. */
        default UITableViewCell.SELECTION_STYLE selectionStyleForListOfType (int type)  { return UITableViewCell.SELECTION_STYLE.NONE; }

        /** @brief Default returns YES. */
        default boolean canSelectSection (int section) { return true; }
        default String noItemAvailableTitleForListOfType (int type) { return ""; }
        default String textLabelAtIndexPath (IndexPath indexPath) { return ""; }
        default String detailTextLabelAtIndexPath (IndexPath indexPath) { return ""; }
        default AttributedString attributedTextLabelAtIndexPath (IndexPath indexPath) { return null; }
        default AttributedString attributedDetailTextLabelAtIndexPath (IndexPath indexPath) { return null; }
        default boolean isSelectedRowAtIndexPath (IndexPath indexPath) { return false; }
        default <C extends BaseTableViewCell> C cellForListItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) { return (C) new BaseTableViewCell(); }
        /** @brief Called in tableView: didSelectRowAtIndexPath or methods called by it when a section is of type list or
        tableView: commitEditingStyle: forRowAtIndexPath for UITableViewCellEditingStyleInsert.
        Corresponds to selectedActionHandler of type MKU_LIST_ITEM_SELECTED_ACTION_SELECT and MKU_LIST_ITEM_SELECTED_ACTION_SHOW_DETAIL */
        default void didSelectListItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default List<ViewContentProtocol.Placeholder> listItemsForListOfType (int type) { return new ArrayList<>(); }
        default List<ViewContentProtocol.Placeholder> listItemsForListInSection (int section) { return new ArrayList<>(); }
        default boolean canSelectItemsInListOfType (int type) { return false; }
        default int listTypeForListInSection (int section) { return section; }
        default ViewContentProtocol.Placeholder listItemAtIndexPath (IndexPath indexPath) { return null; }

        /** @brief Retun view controller to be pushed when an item is selected. It will be called in
        willAddItemToListOfType (int)type withCompletion as well. Return nil to do custom actions. */
        default ViewControllerCallback transitioningViewControllerForItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) { return null; }

        /** @brief Pushes the view controller returned by transitioningViewControllerForItem:atIndexPath when an item is selected. It will be called in willAddItemToListOfType (int)type withCompletion as well. Return nil in transitioningViewControllerForItem to do custom actions, or override this. */
        default void presentTransitioningViewControllerWithItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {}

        /** @brief By default is is self. If this view is within a container, it can be assigned the container to handle
        transitions when an item is selected. */
        default VCTransitionDelegate transitionVCDelegate() { return null; }
        default void setTransitionVCDelegate() {}

        default boolean dispatchTransitionVCDelegateToTransitionToViewController (UIViewController VC, UIViewController sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {
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
        default ViewControllerCallback createPresentingSelectionVCForItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {
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
        default String titleForAddCellInListOfType (int type) { return ""; }
        default int heightForNonEditingListRowAtIndexPath (IndexPath indexPath) { return 0; }

        /** @brief Peform any actions required to delete this item. In the completion, this ittem will be removed from the list. */
        default void willDeleteItemForRowAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath, SuccessErrorCallback completion) {}

        /** @brief Peform any actions required after deleting this item such as updating navbar. */
        default void didDeleteItemForRowAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {}

        /** @brief Peform any actions required to construct this item. In the completion, this ittem will be add to the list. */
        default void willAddItemToListOfType (int type, ViewContentProtocol.Callback completion) {}

        /** @brief Peform any actions required after adding this item such as updating navbar. */
        default void didAddItemForRowAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {}

        /** @brief If YES, the item is added on spot, if NO, presentTransitioningViewControllerWithItem is called to handle adding the new item.
        Default returns NO. */
        default boolean shouldAddItemToListOfType(ViewContentProtocol.Placeholder item, int type) { return false; }

        /** @brief Called after insert or delete actions are performed. Use to update other elements such as navbar. */
        default void didFinishCommitEditingStyleForRowAtIndexPath (UITableViewCell.EDITING_STYLE editingStyle, IndexPath indexPath) {}

        /** By default when viewController: didReturnWithResultType: object: is triggered, the object is either added or replaced if existing, or
        the object is of a different type than that of items which results in failure, in any case this method is called to handle the update's result.
        @param update YES if update succeeds and NO if it fails. */
        default void didUpdateItemAtIndexPath (boolean update, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}

        /** @brief Return a new item to be used in willAddItemToListOfType:(NSUInteger)type withCompletion. Default uses  itemsClass to initialize it. */
        default ViewContentProtocol.Placeholder newItemInListOfType (int type) { return null; }

        default Class itemsClassInListOfType (int type) { return null; }

        /** @brief The object which will be retunred to transitionDelegate when Done / Next button ia pressed, e.g., selected items. */
        default Object returnedInSelectObject() { return null; }
        /** @brief The object which will be retunred to transitionDelegate when Close button is pressed, e.g., items. */
        default Object returnedInCloseObject() { return null; }

        default void itemDidMoveFromIndexToIndexInListOfType (ViewContentProtocol.Placeholder item1, int index1, int index2,  int type) {}
    }

    interface UpdateDelegate {
        default <V extends UIViewController & EditingListVC & VC> void itemsListVCDidUpdateItemsInSection (V VC, List<ViewContentProtocol.Placeholder> items, int section) {}
        default <V extends UIViewController & EditingListVC & VC> void itemsListVCDidUpdateItemAtIndexPath (V VC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default <V extends UIViewController & EditingListVC & VC> void itemsListVCDidSetSelectedAtIndexPath (V VC, boolean selected, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
    }

    interface SelectionActionHandler {
        default LIST_ITEM_SELECTED_ACTION selectedActionHandler(int type) { return LIST_ITEM_SELECTED_ACTION.NONE; }
    }

    enum LIST_ITEM_SELECTED_ACTION {
        NONE,
        SELECT,
        SHOW_DETAIL,
        TRANSITION_TO_DETAIL
    }
}