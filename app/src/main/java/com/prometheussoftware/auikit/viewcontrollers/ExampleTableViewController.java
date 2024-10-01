package com.prometheussoftware.auikit.viewcontrollers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

public class ExampleTableViewController extends UITableViewController {

    ArrayList<String> titles = new ArrayList<>();

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();
        setTitle("ExampleTable");
        createTitles();
        loadData();
    }

    @Override
    protected UITableViewDataController createDataController() {
        return new DataController();
    }

    private void loadData() {

        ArrayList<TableObject.Section> sections = new ArrayList();
        TableObject.Section section = new TableObject.Section();
        TableObject.RowData data = new TableObject.RowData(titles, ExampleTableViewCell.class);
        section.rows = data;
        sections.add(section);
        contentController.setData(sections);

        ArrayList<Pair<TableObject.CellInfo, Object>> array = data.itemsArray();
        contentController.disableRecycling(array.get(0).getFirst().getIdentifier());
    }

    private void createTitles() {
        titles.add("First Cell label");
        titles.add("Second Cell label");
        titles.add("Third Cell label");
        titles.add("Forth Cell label");
        titles.add("Fifth Cell label");
        titles.add("Last Cell label");
    }

    class DataController extends UITableViewDataController {

        @Override
        public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
            return (V) new CellViewHolder(cell);
        }

        class CellViewHolder extends UITableViewHolder.Cell <ExampleTableViewCell> {

            public CellViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override
            public void bindDataForRow(Object item, IndexPath indexPath, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForRow(item, indexPath, delegate);

                if (item instanceof Pair) {
                    Pair pair = (Pair)item;
                    String title = (String) pair.getSecond();
                    if (title instanceof String) {
                        view.getTitleLabel().setText(title);
                    }
                }
            }
        }

        @Override
        public void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {
            super.didSelectRowAtIndexPath(item, indexPath);
            titles.set(indexPath.row, "Selected");
            if (item instanceof Pair) {
                Pair pair = (Pair)item;
                String title = (String) pair.getSecond();
                if (title instanceof String) {
                    transitionToCollapsingScreen();
                }
            }
            reloadData();
        }
    }

    private void transitionToCollapsingScreen() {

        ExampleCollapsingTableViewController vc = new ExampleCollapsingTableViewController();
        pushViewController(vc, true);
    }
}
