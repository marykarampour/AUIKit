package com.prometheussoftware.auikit.tableview.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewController;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;
import com.prometheussoftware.auikit.tableview.UITableViewHolder;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class SearchTableViewController <T extends BaseModel & SearchTableViewController.DataSource> extends UITableViewController <SearchTableViewContentController> {

    private ArrayList<T> items;
    private ArrayList<T> resultItems;
    private T selectedItem;

    public void setItems(ArrayList<T> items) {
        this.items = items;
        loadData();
    }

    public ArrayList<T> getItems() {
        return items;
    }

    protected ArrayList<T> preSelected() {
        return new ArrayList<>();
    }

    public T getSelectedItem() {
        return selectedItem;
    }

    protected void loadData() {

        ArrayList<TableObject.Section> sections = new ArrayList();
        TableObject.Section section = new TableObject.Section();

        TableObject.RowData data = new TableObject.RowData(items, UITableViewCell.Concrete.class);
        ArrayList<T> selected = preSelected();
        data.setSelected(selected);

        section.expandedHeight = 0;
        section.setCollapsible(false);
        section.rows = data;
        sections.add(section);

        contentController.getDataController().setMultiSelectEnabled(false);
        contentController.disableRecycling(data.getItemsInfo().array.get(0).getFirst().getIdentifier());
        contentController.setData(sections);
    }

    @Override
    protected UITableViewDataController createDataController() {
        return new DataController();
    }

    @Override
    protected void createContentController() {
        setContentController(new SearchTableViewContentController(view()));
    }

    class DataController extends UITableViewDataController {

        @Override
        public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
            return (V) new CellViewHolder(cell);
        }

        class CellViewHolder extends UITableViewHolder.Cell <UITableViewCell.Concrete> {

            public CellViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override
            public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForRow(item, indexPath, delegate);

                if (item instanceof Pair) {
                    Pair<TableObject.CellInfo, T> obj = (Pair<TableObject.CellInfo, T>)item;
                    view.getTitleLabel().setText(obj.getSecond().title());
                    view.setAccessoryType(obj.getFirst().selected ? UITableViewCell.ACCESSORY_TYPE.CHECKMARK : UITableViewCell.ACCESSORY_TYPE.NONE);
                }
            }
        }

        @Override
        public void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {
            super.didSelectRowAtIndexPath(item, indexPath);

            if (item instanceof Pair) {
                Pair<TableObject.CellInfo, T> obj = (Pair<TableObject.CellInfo, T>)item;
                selectedItem = obj.getSecond();
            }
            reloadData();
        }
    }

    public interface DataSource {
        String title();
    }
}
