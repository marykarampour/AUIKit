package com.prometheussoftware.auikit.uiviewcontroller;

import com.prometheussoftware.auikit.callback.ViewControllerCallback;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.tableview.BaseTableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public interface ItemsListProtocol {

    public interface VCTransitionDelegate {
        default void handleTransitionToViewControllerForListItemAtIndexPath (UIViewController VC, UIViewController sourceVC, ViewContentProtocol.Placeholder item, IndexPath indexPath) {}
        default void handleDismissDestinationViewController (UIViewController VC) {}
    }

    public interface VC {
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
        /** @brief Called in tableView: didSelectRowAtIndexPath: or methods called by it when a section is of type list or
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
    }

    public interface ListVC {
        default ViewControllerCallback createPresentingSelectionVCForItemAtIndexPath (ViewContentProtocol.Placeholder item, IndexPath indexPath) {
            return () -> { return null; };
        }
    }

    public interface EditingListVC extends ViewControllerTransition {
    }


    public interface UpdateDelegate {
    }
}
