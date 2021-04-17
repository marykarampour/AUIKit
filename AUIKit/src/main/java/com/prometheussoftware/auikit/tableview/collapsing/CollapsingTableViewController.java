package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewController;

import java.util.ArrayList;

public class CollapsingTableViewController extends UITableViewController <CollapsingTableViewDataController, CollapsingTableViewContentController> {

    @Override
    protected CollapsingTableViewContentController createContentController(CollapsingTableViewDataController dataController) {
        return new CollapsingTableViewContentController(view(), dataController);
    }

    @Override
    protected CollapsingTableViewDataController createDataController() {
        return new CollapsingTableViewDataController();
    }

    @Override public void viewWillAppear(boolean animated) {
        super.viewWillAppear(animated);
        contentController.reloadData();
    }


    @Override public void performUpdateForDidSelectSectionAtIndex(TableObject.Section item, int section, boolean selected) {

        CollapsingTableViewDataController controller = contentController.getDataController();

        boolean isExpanded = item.isExpanded;

        if (controller.exclusiveExpandForSelected) {
            if (selected && !item.isExpanded) {
                isExpanded = true;
            }
            else if (!selected && item.isExpanded) {
                isExpanded = false;
            }
        }
        else {
            isExpanded = !item.isExpanded;
        }

        if (!controller.multiExpandedSectionEnabled) {

            ArrayList<TableObject.Section> sections = controller.getSections();

            for (TableObject.Section obj : sections) {
                if (obj != item && obj.isExpanded) {
                    int index = sections.indexOf(obj);

                    int position = controller.positionForIndexPath(new IndexPath(index, 0));
                    int count = controller.numberOfRowsInSection(index, obj.isExpanded);

                    contentController.getAdapter().notifyItemRangeRemoved(position+1, count);
                    contentController.getAdapter().notifyItemRangeChanged(position, 1);
                    obj.isExpanded = false;
                }
            }
        }

        int position = controller.positionForIndexPath(new IndexPath(section, 0));
        int count = controller.numberOfRowsInSection(section, isExpanded);

        if (isExpanded) {
            contentController.getAdapter().notifyItemRangeInserted(position, count);
        }
        else {
            contentController.getAdapter().notifyItemRangeRemoved(position+1, count);
            contentController.getAdapter().notifyItemRangeChanged(position, 1);
        }

        item.isExpanded = isExpanded;
        if (isExpanded && contentController.getAdapter().getItemCount() > position) contentController.getTableView().getView().smoothScrollToPosition(position);
    }
}

