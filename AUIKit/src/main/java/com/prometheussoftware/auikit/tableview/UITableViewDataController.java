package com.prometheussoftware.auikit.tableview;

import android.view.ViewGroup;

import com.prometheussoftware.auikit.model.IndexPath;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.uiview.UIView;

import java.util.ArrayList;

public class UITableViewDataController implements UITableViewProtocol.Data {

    public boolean multiSelectEnabled = true;
    public boolean exclusiveExpandForSelected;
    protected ArrayList<TableObject.Section> sections = new ArrayList();

    /** Responsible for performing updates when a section header is tapped */
    protected UITableViewProtocol.UpdateDelegate updateDelegate;
    private UITableViewProtocol.TableView viewDelegate;
    private ArrayList<TableObject.CellInfo> cellInfos = new ArrayList();
    private ArrayList<TableObject.CellInfo> sectionInfos = new ArrayList();

    //region adapter

    private <V extends UIView> V viewForViewType(int viewType, ArrayList<TableObject.CellInfo> infos) {
        for (TableObject.CellInfo info : infos) {
            if (info.getIdentifier() == viewType) {
                try {
                    return (V) info.getCellClass().newInstance();
                }
                catch (IllegalAccessException e) { }
                catch (InstantiationException e) { }
            }
        }
        return null;
    }

    @Override public <V extends UITableViewCell> V cellForViewType(ViewGroup parent, int viewType) {
        return viewForViewType(viewType, cellInfos);
    }

    @Override public <V extends UIView> V headerForViewType(ViewGroup parent, int viewType) {
        return viewForViewType(viewType, sectionInfos);
    }

    @Override public <V extends UITableViewHolder> V viewHolderForViewType(ViewGroup parent, int viewType) {

        UITableViewCell cell = cellForViewType(parent, viewType);

        if (cell != null && UITableViewCell.class.isInstance(cell)) {
            return viewHolderForCell(cell);
        }

        UIView header = headerForViewType(parent, viewType);
        //Settings like set delegate, listener etc.
        customizeHeaderViewForViewType(header, viewType);

        if (header != null && UIView.class.isInstance(header)) {
            return viewHolderForHeader(header);
        }
        return null;
    }

    @Override public <V extends UITableViewHolder, C extends UITableViewCell> V viewHolderForCell(C cell) {
        return (V) new UITableViewHolder.Cell(cell);
    }

    @Override public <V extends UITableViewHolder, W extends UIView> V viewHolderForHeader(W header) {
        return (V) new UITableViewHolder.Header(header);
    }

    @Override public <V extends UIView> void customizeHeaderViewForViewType(V header, int viewType) { }

    @Override public <V extends UITableViewHolder> void bindData(V holder, int position) {

        IndexPath indexPath = indexPathForPosition(position);

        //section
        if (indexPath.isSection()) {
            if (heightForHeaderInSection(indexPath.section) > 0) {
                TableObject.Section section = getSection(indexPath.section);
                if (section != null) holder.bindDataForSection(section, indexPath.section, this);
            }
        }//row
        else {
            Object item = getItemAtPosition(position);
            if (item != null) holder.bindDataForRow(item, indexPath, this);
        }
    }

    @Override public <V extends UITableViewHolder> void bindData(UITableView tableView, V holder, int position) {

    }

    //endregion

    //region rows and sections

    public int numberOfVisibleViews() {

        int count = 0;
        for (int i = 0; i < numberOfSectionsInTableView(); i++) {

            if (sectionIsExpanded(i)) {
                count += numberOfRowsInSection(i);
            }
            if (heightForHeaderInSection(i) > 0) count ++;
        }
        return count;
    }

    public int positionForIndexPath (IndexPath indexPath) {
        int count = 0;
        for (int i = 0; i < numberOfSectionsInTableView(); i++) {

            if (!sectionIsCollapsable(i) || sectionIsExpanded(i)) {
                if (indexPath.section == i) {
                    if (indexPath.row != null) count += indexPath.row;
                }
                else {
                    count += numberOfRowsInSection(i);
                }
            }
            if (indexPath.section <= i) break;
            else if (heightForHeaderInSection(i) > 0) count ++;
        }
        return count;
    }

    public IndexPath indexPathForPosition (int position) {
        int section = -1;
        int count = -1;
        int row = -1;
        boolean terminate = false;

        for (int i = 0; i < numberOfSectionsInTableView(); i++) {

            int rowInSection = -1;
            section++;

            if (heightForHeaderInSection(i) > 0) {
                count++;
            }

            if (position == count) {
                break;
            }

            for (int j = 0; j < numberOfRowsInSection(i); j++) {

                rowInSection ++;
                count ++;

                if (position == count) {
                    row = rowInSection;
                    terminate = true;
                    break;
                }
            }

            if (terminate) break;
        }

        return new IndexPath(section, row);
    }

    public boolean sectionIsCollapsable (int section) {
        return false;
    }

    public boolean sectionIsExpanded (int section) {
        return true;
    }

    public ArrayList<TableObject.Section> getSections() {
        return sections;
    }

    public void setSections (ArrayList<TableObject.Section> sections) {

        this.sections.clear();
        if (sections == null) return;

        this.sections.addAll(sections);

        for (TableObject.Section sect : sections) {
            if (sect.info != null && !sectionInfos.contains(sect.info)) {
                sectionInfos.add(sect.info);
            }
            if (sect.rows != null) {
                for (Pair<TableObject.CellInfo, Object> info : sect.rows.getItemsInfo().array) {
                    if (info.getFirst() != null && !cellInfos.contains(info.getFirst())) {
                        cellInfos.add(info.getFirst());
                    }
                }
            }
        }

        reloadData();
    }

    public TableObject.Section getSection (int position) {
        if (sections.size() <= position) return null;
        return sections.get(position);
    }

    public Object getItemAtPosition (int position) {
        IndexPath indexPath = indexPathForPosition(position);
        return getItemAtIndexPath(indexPath);
    }

    public int indexOfSection (TableObject.Section item) {
        return (item != null) ? sections.indexOf(item) : -1;
    }

    public Object getItemAtIndexPath (IndexPath indexPath) {
        if (indexPath.section != null && sections.size() <= indexPath.section) return null;

        if (indexPath.row == null) return sections.get(indexPath.section);

        ArrayList values = sections.get(indexPath.section).rows.getItems().array;
        if (values.size() <= indexPath.row) return null;

        return values.get(indexPath.row);
    }

    protected TableObject.CellInfo infoForRowAtIndexPath (IndexPath indexPath) {
        if (indexPath.section != null && sections.size() <= indexPath.section || indexPath.row == null) return null;

        ArrayList<Pair<TableObject.CellInfo, Object>> values = sections.get(indexPath.section).rows.getItems().array;
        if (values.size() <= indexPath.row) return null;

        return values.get(indexPath.row).getFirst();
    }

    protected TableObject.Section sectionAtIndex (Integer section) {
        if (sections.size() <= section) return null;
        return sections.get(section);
    }

    protected int selectedCellsForSection (Integer section) {

        TableObject.Section sect = sectionAtIndex(section);
        int count = 0;

        if (sect != null) {
            for (Pair<TableObject.CellInfo, Object> info : sect.rows.getItems().array) {
                if (info.getFirst().selected) {
                    count ++;
                }
            }
        }
        return count;
    }

    protected int selectedCellsForSection (TableObject.Section sect) {

        int count = 0;

        if (sect != null) {
            for (Pair<TableObject.CellInfo, Object> info : sect.rows.getItems().array) {
                if (info.getFirst().selected) {
                    count ++;
                }
            }
        }
        return count;
    }

    //endregion

    //region UITableViewDataProtocol

    @Override public int numberOfSectionsInTableView() {
        return sections.size();
    }

    @Override public int numberOfRowsInSection(int section) {
        if (sections.size() <= section) return 0;
        if (sections.get(section).rows == null) return 0;
        return sections.get(section).rows.getItems().size();
    }

    @Override public int viewTypeForPosition(int position) {

        IndexPath indexPath = indexPathForPosition(position);
        if (indexPath.section <= sections.size()) {

            TableObject.Section sect = sections.get(indexPath.section);
            if (indexPath.isSection()) {
                return sect.info.getIdentifier();
            }
            else {
                TableObject.CellInfo info = sect.rows.infoAtIndex(indexPath.row);
                if (info != null) {
                    return info.getIdentifier();
                }
            }
        }
        return 0;
    }

    @Override public void didSelectRowAtIndexPath(Object item, IndexPath indexPath) {

        TableObject.CellInfo cell = infoForRowAtIndexPath(indexPath);
        if (cell == null) return;
        cell.selected = !cell.selected;

        if (!multiSelectEnabled) {
            for (TableObject.Section sect : sections) {
                for (Pair<TableObject.CellInfo, Object> info : sect.rows.getItemsInfo().array) {
                    if (info.getFirst() != cell) {
                        info.getFirst().selected = false;
                    }
                }
            }
        }

        if (updateDelegate != null) updateDelegate.performUpdateForDidSelectRowAtIndexPath(item, indexPath);
    }

    //endregion

    //region view protocol

    public void setViewDelegate(UITableViewProtocol.TableView viewDelegate) {
        this.viewDelegate = viewDelegate;
    }

    public UITableViewProtocol.TableView getViewDelegate() {
        return viewDelegate;
    }

    protected void reloadData() {
        if (viewDelegate != null) viewDelegate.reloadData();
    }

    //endregion


    //region update protocol

    @Override public void didSelectSectionAtIndex(TableObject.Section item, int section, boolean selected) {

        if (!item.isEnabled || !item.isCollapsible()) return;
        if (updateDelegate != null) {
            updateDelegate.performUpdateForDidSelectSectionAtIndex(item, section, selected);
        }
    }

    @Override public void didSelectSectionAtIndex(TableObject.Section item, int section) {

        if (!item.isEnabled || !item.isCollapsible()) return;
        if (updateDelegate != null) {
            updateDelegate.performUpdateForDidSelectSectionAtIndex(item, section);
        }
    }

    public void setUpdateDelegate(UITableViewProtocol.UpdateDelegate updateDelegate) {
        this.updateDelegate = updateDelegate;
    }

    //endregion


    //helpers and fields

    public void setMultiSelectEnabled(boolean multiSelectEnabled) {
        this.multiSelectEnabled = multiSelectEnabled;
    }


    //endregion
}
