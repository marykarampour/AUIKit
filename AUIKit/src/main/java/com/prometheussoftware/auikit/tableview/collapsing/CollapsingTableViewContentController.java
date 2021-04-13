package com.prometheussoftware.auikit.tableview.collapsing;

import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewContentController;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class CollapsingTableViewContentController extends UITableViewContentController <CollapsingTableViewDataController> implements UITableViewProtocol.Content {

    public CollapsingTableViewContentController(UIView view, CollapsingTableViewDataController dataController) {
        super(view, dataController);
    }

    @Override
    public <T extends CollapsingTableViewDataController> void setDataController(T dataController) {
        super.setDataController(dataController);
        dataController.setContentDelegate(this);
    }

    @Override public void performUpdateForDidSelectSectionAtIndex(TableObject.Section item, int section, boolean selected) {

        CollapsingTableViewDataController controller = getDataController();

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

                    getAdapter().notifyItemRangeRemoved(position+1, count);
                    getAdapter().notifyItemRangeChanged(position, 1);
                    obj.isExpanded = false;
                }
            }
        }

        int position = controller.positionForIndexPath(new IndexPath(section, 0));
        int count = controller.numberOfRowsInSection(section, isExpanded);

        if (isExpanded) {
            getAdapter().notifyItemRangeInserted(position, count);
        }
        else {
            getAdapter().notifyItemRangeRemoved(position+1, count);
            getAdapter().notifyItemRangeChanged(position, 1);
        }

        item.isExpanded = isExpanded;
        if (isExpanded && getAdapter().getItemCount() > position) getTableView().getView().smoothScrollToPosition(position);
    }

    @Override public void performUpdateForDidSelectSectionAtIndex(TableObject.Section item, int section) {
        performUpdateForDidSelectSectionAtIndex(item, section, true);
    }
}
