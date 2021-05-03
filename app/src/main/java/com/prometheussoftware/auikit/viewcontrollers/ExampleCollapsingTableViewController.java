package com.prometheussoftware.auikit.viewcontrollers;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.common.Dimensions;
import com.prometheussoftware.auikit.genericviews.UICheckbox;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.tableview.UITableViewCell;
import com.prometheussoftware.auikit.tableview.UITableViewDataController;
import com.prometheussoftware.auikit.tableview.UITableViewHolder;
import com.prometheussoftware.auikit.tableview.UITableViewProtocol;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingTableViewController;
import com.prometheussoftware.auikit.tableview.collapsing.CollapsingTableViewDataController;
import com.prometheussoftware.auikit.uiview.UIControl;
import com.prometheussoftware.auikit.uiview.UILabel;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class ExampleCollapsingTableViewController extends CollapsingTableViewController {

    ArrayList<String> titles = new ArrayList<>();

    static {
        Identifier.Register(CheckboxView.class);
    }

    @Override
    public void viewDidLoad() {
        super.viewDidLoad();

        setTitle("Collapsing");
        createTitles();
        loadData();
    }

    @Override
    protected UITableViewDataController createDataController() {
        return new DataController();
    }

    private void loadData() {

        ArrayList<TableObject.Section> sections = new ArrayList();

        for (int i = 0; i < 1; i++) {

            TableObject.Section section = new TableObject.Section();
            section.info = new TableObject.CellInfo(CheckboxView.class);
            section.object.title = "Section";
            section.object.subTitle = Integer.toString(i);
            section.expandedHeight = App.constants().TableView_Section_Header_Height();
            section.setCollapsible(true);

            TableObject.RowData data = new TableObject.RowData(titles, ExampleTableViewCell.class);
            section.rows = data;
            sections.add(section);

            contentController.disableRecycling(section.info.getIdentifier());
            contentController.disableRecycling(data.getItemsInfo().array.get(0).getFirst().getIdentifier());
        }

        contentController.setData(sections);
    }

    private void createTitles() {
        titles.add("First Cell label");
        titles.add("Second Cell label");
        titles.add("Third Cell label");
        titles.add("Forth Cell label");
        titles.add("Fifth Cell label");
        titles.add("Last Cell label");
    }

    class DataController extends CollapsingTableViewDataController {

        @Override
        public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
            return (V) new DataController.CellViewHolder(cell);
        }

        @Override
        public <V extends UITableViewHolder, W extends UIView> V viewHolderForHeader(W header) {
            return (V) new HeaderViewHolder(header);
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
        public int heightForHeaderInSection(int section) {
            return Dimensions.Int_44();
        }

        class HeaderViewHolder extends UITableViewHolder.Header <CheckboxView> {

            public HeaderViewHolder(@NonNull UIView itemView) {
                super(itemView);
            }

            @Override
            public void bindDataForSection(TableObject.Section item, Integer section, @Nullable UITableViewProtocol.Data delegate) {
                super.bindDataForSection(item, section, delegate);

                UILabel label = view.getTitleLabel();
                label.setText(item.object.title + "\n" + item.object.subTitle);
                view.getRightView().setDelegate((UIControl view, boolean selected) -> {
                    if (delegate != null) {
                        delegate.didSelectSectionAtIndex(item, section, selected);
                    }
                });
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
                    pair.setSecond("Selected " + title);
                }
            }
            reloadData();
        }
    }

    public static class CheckboxView extends UICheckbox.Right {

        @Override
        public void initView() {
            super.initView();

            setBackgroundColor(UIColor.build(220, 220, 240, 1.0f));
            getRightView().setBackgroundColor(UIColor.red(1.0f));
            getRightView().setSelectedColor(UIColor.black(1.0f));
            getRightView().setDeselectedColor(UIColor.black(1.0f));
            getRightView().setOnImage(App.assets().Right_Chevron_Image());
            getRightView().setOffImage(App.assets().Down_Chevron_Image());
            getRightView().setEnabled(true);

            UILabel label = getTitleLabel();
            label.setPadding(Dimensions.Int_4(), 0, 0, 0);
            label.setFont(UIFont.systemFont(12, UIFont.STYLE.BOLD));
        }
    }
}
