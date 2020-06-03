package com.prometheussoftware.auikit.tableview;

import android.util.Size;

import com.prometheussoftware.auikit.common.App;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.Identifier;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.model.PairArray;

import java.util.ArrayList;

public class TableObject {

    /** Used to indicate view should configure height based on content */
    public static final int AutomaticDimension = -1;

    public static class RowData extends BaseModel {

        static {
            BaseModel.Register(RowData.class);
        }

        private PairArray<CellInfo, Object, Pair<CellInfo, Object>> items = new PairArray();

        public RowData() { }

        public RowData(ArrayList items, Class cellClass) {
            addItems(items, cellClass);
        }

        public RowData(Object item, Class cellClass) {
            this.items.addPair(new CellInfo(cellClass, true), item);
        }

        public RowData(Object item, Class cellClass, int estimatedHeight, boolean instanceId) {
            this.items.addPair(new CellInfo(cellClass, estimatedHeight, instanceId), item);
        }

        public RowData(Object item, Class cellClass, int estimatedHeight, int identifier) {
            this.items.addPair(new CellInfo(cellClass, estimatedHeight, identifier), item);
        }

        public RowData(ArrayList items, Class cellClass, int estimatedHeight) {
            addItems(items, new CellInfo(cellClass, estimatedHeight));
        }

        public void addItem(Object item, CellInfo info) {
            this.items.addPair(info, item);
        }

        public void addItems(ArrayList items, CellInfo info) {
            for (Object obj : items) {
                this.items.addPair(info, obj);
            }
        }

        public void addItems(ArrayList items, Class cellClass, int estimatedHeight) {
            for (Object obj : items) {
                this.items.addPair(new CellInfo(cellClass, estimatedHeight), obj);
            }
        }

        private void addItems(ArrayList items, Class cellClass) {
            for (Object obj : items) {
                this.items.addPair(new CellInfo(cellClass), obj);
            }
        }

        public void setSelected (ArrayList selected) {
            for (Pair<CellInfo, Object> obj : items.array) {
                obj.getFirst().selected = selected.contains(obj.getSecond());
            }
        }

        public void setItems(PairArray items) {
            this.items = items;
        }

        public PairArray<CellInfo, Object, Pair<CellInfo, Object>> getItems() {
            return items;
        }

        public PairArray<CellInfo, Object, Pair<CellInfo, Object>> getItemsInfo() {
            return items;
        }

        public CellInfo infoAtIndex (int index) {
            if (items.size() <= index) return null;
            return items.array.get(index).getFirst();
        }
    }

    /** Subclass to encapsulate the data that will be used by the cell */
    public static class CellData extends BaseModel { }

    public static class SectionData extends CellData {

        public String title;
        public String subTitle;
        public Size accessorySize;
        public int accessoryImage;

        static {
            BaseModel.Register(SectionData.class);
        }

        public SectionData (String title, String subTitle) {
            this.title = title;
            this.subTitle = subTitle;
        }

        public SectionData (String title) {
            this.title = title;
            this.subTitle = "";
        }

        public SectionData () {
            this.title = "";
            this.subTitle = "";
        }
    }

    public static class CellInfo extends BaseModel {

        /** It can be AutomaticDimension or
         * estimated height of view or custom value */
        public int estimatedHeight = AutomaticDimension;

        /** If true height will return 0 */
        public boolean hidden;

        /** Identifier of the view */
        private int identifier;

        /** Class of the view */
        private Class cellClass;

        public boolean selected;

        public int minHeight;

        public int maxHeight;

        /** If true and 0 < maxHeight, maxHeight is used as row height */
        public boolean expanded;

        static {
            BaseModel.Register(CellInfo.class);
        }

        /** It is the current height, can be estimatedHeight,
         * 0.0 or a suitable height set based on conditions */
        public int getHeight() {
            return hidden ? 0 : estimatedHeight;
        }

        public CellInfo(Class cellClass, int estimatedHeight) {
            setCellClass(cellClass, false);
            this.estimatedHeight = estimatedHeight;
        }

        public CellInfo(Class cellClass) {
            setCellClass(cellClass, false);
        }

        /** @param instanceId If true the id will be generated for instance
         * rather than the class */
        public CellInfo(Class cellClass, boolean instanceId) {
            setCellClass(cellClass, instanceId);
        }

        /** @param instanceId If true the id will be generated for instance
         * rather than the class */
        public CellInfo(Class cellClass, int estimatedHeight, boolean instanceId) {
            setCellClass(cellClass, instanceId);
            this.estimatedHeight = estimatedHeight;
        }

        public CellInfo(Class cellClass, int estimatedHeight, int identifier) {
            this.cellClass = cellClass;
            this.identifier = identifier;
            this.estimatedHeight = estimatedHeight;
        }

        public CellInfo() { }

        public void setCellClass(Class cellClass, boolean instanceId) {
            this.cellClass = cellClass;
            this.identifier = instanceId ? Identifier.generateIdentifier() : Identifier.getIdentifier(cellClass);
        }

        public void setCellClass(Class cellClass, int identifier) {
            this.cellClass = cellClass;
            this.identifier = identifier;
        }

        public Class getCellClass() {
            return cellClass;
        }

        public int getIdentifier() {
            return identifier;
        }
    }

    public static class Section <R extends RowData, T extends SectionData> extends BaseModel {

        private boolean isCollapsible;
        /** only used if isCollapsible is true */
        public boolean isExpanded;
        /** if false user interaction will be disabled on the section header, default is true */
        public boolean isEnabled = true;
        public boolean isHidden;

        public int collapsedHeight;
        /** Default is Constants.TableView_Section_Header_Height */
        public int expandedHeight = App.constants().TableView_Section_Header_Height();

        public RowData rows;

        /** info used for the section header */
        public CellInfo info;

        /** object containing the data consumed by the section header's view */
        public T object;

        static {
            BaseModel.Register(Section.class);
        }

        public Section () {
            object = (T) new SectionData();
        }

        public void setCollapsible(boolean collapsible) {
            isCollapsible = collapsible;
            if (!collapsible) isExpanded = true;
        }

        public boolean isCollapsible() {
            return isCollapsible;
        }

        public boolean isHidden() {
            return isHidden;
        }

        /** If isCollapsible = true returns collapsedHeight otherwise returns expandedHeight */
        public int estimatedHeight() {
            if (isCollapsible) return expandedHeight;
            return collapsedHeight;
        }
    }
}
