package com.prometheussoftware.auikit.tableview.menu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.classes.UIEdgeInsets;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.genericviews.UICheckbox;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewHolder;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingSectionsMenuProtocol;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingTableViewController;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingTableViewDataController;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiviewcontroller.Navigation;
import com.prometheussoftware.auikit.uiviewcontroller.UINavigationController;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

import java.util.ArrayList;

public abstract class CollapsingSectionsMenuViewController extends CollapsingTableViewController implements CollapsingSectionsMenuProtocol {

    private ArrayList<MenuObject.Section> menuItems;

    /** Call in or after viewDidLoad. It is called in viewDidLoad by default. */
    public void setMenuSections(ArrayList<MenuObject.Section> sections) {
        this.menuItems = sections;
        contentController.setData(sections);
    }

    public ArrayList<MenuObject.Section> getMenuItems() {
        return menuItems;
    }

    //region view controller

    @Override public void viewDidLoad() {
        super.viewDidLoad();
        setMenuSections(menuSections());
    }

    @Override protected MenuDataController createDataController() {
        return new MenuDataController();
    }

    class MenuDataController extends CollapsingTableViewDataController {

        @Override public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
            return (V) new MenuCellViewHolder(cell);
        }

        @Override
        public <V extends UITableViewHolder, W extends UIView> V viewHolderForHeader(W header) {
            return (V) new MenuHeaderViewHolder(header);
        }

        class MenuCellViewHolder extends UITableViewHolder.Cell <UITableViewCell.Concrete> {

            public MenuCellViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForRow(item, indexPath, delegate);

                if (item instanceof Pair) {
                    MenuObject.Item obj = (MenuObject.Item) ((Pair) item).getSecond();

                    view.getTitleLabel().setTextColor(obj.textColor);
                    view.getTitleLabel().setText(obj.title);
                    view.getTitleLabel().setFont(obj.font);
                    view.setHeight(obj.hidden ? 0 : delegate.heightForRowAtIndexPath(item, indexPath));
                    view.setAccessoryType(UITableViewCell.ACCESSORY_TYPE.DISCLOSURE_INDICATOR);
                }
            }
        }

        class MenuHeaderViewHolder extends UITableViewHolder.Header.Checkbox <CheckboxHeader> {

            public MenuHeaderViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override
            public void bindDataForSection(TableObject.Section item, Integer section, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForSection(item, section, delegate);

                if (item instanceof MenuObject.Section) {
                    MenuObject.Section obj = (MenuObject.Section) item;

                    view.getTitleLabel().setTextColor(obj.object.textColor);
                    view.getTitleLabel().setText(obj.object.title);
                    view.getTitleLabel().setFont(obj.object.font);
                    view.getContentView().setBackgroundColor(obj.object.backgroundColor);
                    view.setHeight(obj.estimatedHeight());
                    view.checkView().setOn(obj.isExpanded);
                }
            }
        }

        @Override public void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {
            super.didSelectRowAtIndexPath(item, indexPath);

            if (item instanceof Pair) {
                MenuObject.Item obj = (MenuObject.Item) ((Pair) item).getSecond();
                if (obj.getAction() != null) {
                    obj.getAction().itemPressed(obj);
                }
                else {
                    transitionToView(obj, Navigation.TRANSITION_ANIMATION.UPDOWN);
                }
            }
        }

        @Override
        public int heightForRowAtIndexPath(Object item, IndexPath indexPath) {
            return Dimensions.Int_52();
        }
    }

    public static class CheckboxHeader extends UICheckbox.Right {

        @Override
        public void initView() {
            super.initView();
            checkView().setOnImage(App.assets().Down_Chevron_Image());
            checkView().setOffImage(App.assets().Right_Chevron_Image());
            checkView().setPadding(Dimensions.Int_4(), Dimensions.Int_4(), Dimensions.Int_4(), Dimensions.Int_4());
            setRightViewSize(Dimensions.size(Dimensions.Int_32()));
        }

        @Override
        protected UIEdgeInsets insets() {
            return new UIEdgeInsets(Dimensions.Int_8(), Dimensions.Int_16(), Dimensions.Int_8(), Dimensions.Int_8());
        }
    }

    //endregion


    //region transition

    public void transitionToView (MenuObject.Item item, Navigation.TRANSITION_ANIMATION animation) {

        UIViewController navigationVC = getNavigationController();
        boolean animated = animation != Navigation.TRANSITION_ANIMATION.NONE;

        if (navigationVC.getNavigationController() != null) {
            UIViewController nextViewController = viewControllerForObject(item);
            navigationVC.getNavigationController().pushViewController(nextViewController, animated);
        }
        else {
            UIViewController presentingVC = navigationVC.getPresentingViewController();
            UINavigationController presentingNav = null;
            if (UINavigationController.class.isInstance(presentingVC)) {
                presentingNav = (UINavigationController)presentingVC;
            }
            else if (presentingVC.getNavigationController() != null) {
                presentingNav = presentingVC.getNavigationController();
            }

            //dismiss the menu
            dismissViewController(animation, null);

            //push next VC
            if (presentingNav != null) {
                //Assuming presentingNav's VCs correspond to menu items
                Navigation.Node<UIViewController> currentVC = presentingNav.getTopViewController();
                Navigation.Tree<UIViewController> VCs = presentingNav.clonedNavigationStack();

                if (VCs != null) {
                    int navigationIndex = menuItems.indexOf(item);
                    int currentIndex = presentingNav.indexOfNavigationNode(currentVC);

                    if (currentIndex < navigationIndex) {
                        for (int i = currentIndex + 1; i <= navigationIndex; i++) {

                            if (menuItems.size() <= i) break;

                            MenuObject.Item obj = itemAtIndex(i);
                            UIViewController nextVC = viewControllerForObject(obj);
                            if (nextVC != null) {
                                Navigation.Node<UIViewController> nextVCNode = Navigation.Tree.node(nextVC, false);
                                VCs.getNodes().add(i, nextVCNode);
                            }
                            else {
                                return;
                            }
                        }

                        presentingNav.setNavigationStack(VCs, animated);
                    }
                    else if (navigationIndex < currentIndex) {
                        presentingNav.popToViewControllerAtIndex(navigationIndex, animated);
                    }
                }
            }
            else {
                UIViewController nextViewController = viewControllerForObject(item);
                if (nextViewController != null) {
                    presentingVC.presentViewController(nextViewController, animation, null);
                }
            }
        }
    }

    //endregion


    //region items and class

    public int indexOfVC (UIViewController VC) {

        for (MenuObject.Section section : menuItems) {

            ArrayList<Pair<TableObject.CellInfo, MenuObject.Item>> array = itemsArrayInSection(section);
            for (Pair<TableObject.CellInfo, MenuObject.Item> pair : array) {

                MenuObject.Item item = pair.getSecond();
                if (VCIsInMenuItemClass(VC, item.getTrueVCClass())) {
                    return menuItems.indexOf(item);
                }
            }
        }
        return Constants.NOT_FOUND_ID;
    }

    /** @param index is the index of the item in the menu, not its own section */
    protected MenuObject.Item itemAtIndex (int index) {

        int rowCount = 0;
        for (int i = 0; i < dataController().numberOfSectionsInTableView(); i++) {
            int currentRowCount = rowCount;
            rowCount += dataController().numberOfRowsInSection(i);
            //sec1 5
            //sec2 6
            //sec3 3
            //sec4 7
            //sec5 5
            //sec6 8
            //rows = 34
            //index = 23
            //5+6+3+7 = 21
            //5+6+3+7+5 = 26
            if (index <= rowCount) {
                int row = index - currentRowCount;
                MenuObject.Section section = menuItems.get(i);
                ArrayList<Pair<TableObject.CellInfo, MenuObject.Item>> array = itemsArrayInSection(section);
                return array.get(row).getSecond();
            }
        }
        return null;
    }

    /** @return Returns index is the index of the item in the menu, not its own section */
    protected int indexOfItem (MenuObject.Item item) {

        int row = 0;
        for (MenuObject.Section section : menuItems) {

            ArrayList<Pair<TableObject.CellInfo, MenuObject.Item>> array = itemsArrayInSection(section);
            for (Pair<TableObject.CellInfo, MenuObject.Item> pair : array) {

                MenuObject.Item obj = pair.getSecond();
                if (obj.equals(item))
                    return array.indexOf(pair) + row;
                else
                    row ++;
            }
        }
        return Constants.NOT_FOUND_ID;
    }

    protected ArrayList<Pair<TableObject.CellInfo, MenuObject.Item>> itemsArrayInSection (MenuObject.Section section) {
        return section.rows.itemsArray();
    }

    protected boolean VCIsInMenuItemClass (UIViewController VC, Class parentClass) {

        if (parentClass == null) return false;
        if (parentClass.isInstance(VC)) return true;

        Class VCClass = VC.getClass();
        UIViewController parent = VC;

        while (parent != null) {
            if (VCClass == parentClass) {
                return true;
            }
            parent = parent.getParentViewController();
            if (parent != null) {
                VCClass = parent.getClass();
            }
        }
        return false;
    }

    public static <VC extends UIViewController> VC viewControllerForObject (MenuObject.Item item) {
        return item.viewController();
    }

    //endregion

}
