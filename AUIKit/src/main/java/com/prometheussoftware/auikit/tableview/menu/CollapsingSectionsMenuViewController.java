package com.prometheussoftware.auikit.tableview.menu;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.common.Constants;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;
import com.prometheussoftware.auikit.tableview.UITableViewHolder;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingTableViewController;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingTableViewDataController;
import com.prometheussoftware.auikit.uiview.UIView;
import com.prometheussoftware.auikit.uiviewcontroller.Navigation;
import com.prometheussoftware.auikit.uiviewcontroller.UINavigationController;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;

import java.util.ArrayList;

public class CollapsingSectionsMenuViewController extends CollapsingTableViewController {

    //TODO: Add sections
    private ArrayList<MenuObject> menuItems;

    /** Call in or after viewDidLoad */
    public void setMenuItems(ArrayList<MenuObject> menuItems) {
        this.menuItems = menuItems;
        createMenu();
    }

    public ArrayList<MenuObject> getMenuItems() {
        return menuItems;
    }

    //region view controller

    @Override public void viewDidLoad() {
        super.viewDidLoad();
    }

    protected void createMenu() {

        ArrayList<TableObject.Section> sections = new ArrayList();

        TableObject.Section section = new TableObject.Section();
        section.setCollapsible(false);

        TableObject.RowData data = new TableObject.RowData(menuItems, UITableViewCell.class);
        section.rows = data;
        sections.add(section);

        contentController.setData(sections);
    }

    @Override protected UITableViewDataController createDataController() {
        return new MenuDataController();
    }

    class MenuDataController extends CollapsingTableViewDataController {

        @Override public <V extends UITableViewCell> V cellForViewType(ViewGroup parent, int viewType) {
            UITableViewCell cell = new UITableViewCell.Concrete();
            return (V) cell;
        }

        @Override public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
            return (V) new MenuCellViewHolder(cell);
        }

        class MenuCellViewHolder extends UITableViewHolder.Cell <UITableViewCell> {

            public MenuCellViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForRow(item, indexPath, delegate);

                if (item instanceof Pair) {
                    MenuObject obj = (MenuObject) ((Pair) item).getSecond();

                    view.getTitleLabel().setTextColor(obj.textColor);
                    view.getTitleLabel().setText(obj.title);
                    view.getTitleLabel().setFont(obj.font);
                    view.setHeight(obj.hidden ? 0 : view.estimatedSize().getHeight());
                }
            }
        }

        @Override public void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {
            super.didSelectRowAtIndexPath(item, indexPath);

            if (item instanceof Pair) {
                MenuObject obj = (MenuObject) ((Pair) item).getSecond();
                if (obj.getAction() != null) {
                    obj.getAction().itemPressed(obj);
                }
                else {
                    transitionToView(obj, Navigation.TRANSITION_ANIMATION.UPDOWN);
                }
            }
        }
    }

    //endregion


    //region transition

    public void transitionToView (MenuObject item, Navigation.TRANSITION_ANIMATION animation) {

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

                            MenuObject obj = menuItems.get(i);
                            UIViewController nextVC = viewControllerForObject(obj);
                            if (nextVC != null) {
                                Navigation.Node<UIViewController> nextVCNode = UINavigationController.navigationNode(nextVC, false);
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
        for (MenuObject item : menuItems) {
            if (VCIsInMenuItemClass(VC, item.getTrueVCClass())) {
                return menuItems.indexOf(item);
            }
        }
        return Constants.NOT_FOUND_ID;
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

    public static <VC extends UIViewController> VC viewControllerForObject (MenuObject item) {
        return item.viewController();
    }

    //endregion

}
