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

    public interface RowDataProtocol <T> {
        PairArray<CellInfo, T> items();
        ArrayList<Pair<TableObject.CellInfo, T>> itemsArray();
    }

    public static class RowData <T> extends BaseModel implements RowDataProtocol<T> {

        static {
            BaseModel.Register(RowData.class);
        }

        private PairArray<CellInfo, T> items = new PairArray();

        public RowData() { }

        /** Will create an array of pairs of CellInfo and item */
        public RowData(ArrayList items, Class cellClass) {
            addItems(items, cellClass);
        }

        /** Will create a pair of CellInfo and item */
        public RowData(T item, Class cellClass) {
            if (items == null) items = new PairArray();;
            this.items.addPair(new CellInfo(cellClass, true), item);
        }

        /** Will create a pair of CellInfo and item */
        public RowData(T item, Class cellClass, int estimatedHeight, boolean instanceId) {
            if (items == null) items = new PairArray();;
            this.items.addPair(new CellInfo(cellClass, estimatedHeight, instanceId), item);
        }

        /** Will create a pair of CellInfo and item */
        public RowData(T item, Class cellClass, int estimatedHeight, int identifier) {
            if (items == null) items = new PairArray();;
            this.items.addPair(new CellInfo(cellClass, estimatedHeight, identifier), item);
        }

        /** Will create an array of pairs of CellInfo and item */
        public RowData(ArrayList items, Class cellClass, int estimatedHeight) {
            addItems(items, new CellInfo(cellClass, estimatedHeight));
        }

        /** Will create a pair of CellInfo and item */
        public void addItem(T item, CellInfo info) {
            if (items == null) items = new PairArray();;
            this.items.addPair(info, item);
        }

        /** Will create an array of pairs of CellInfo and item */
        public void addItems(ArrayList<T> items, CellInfo info) {
            if (items == null) return;
            for (T obj : items) {
                this.items.addPair(info, obj);
            }
        }

        /** Will create an array of pairs of CellInfo and item */
        public void addItems(ArrayList<T> items, Class cellClass, int estimatedHeight) {
            if (items == null) return;
            for (T obj : items) {
                this.items.addPair(new CellInfo(cellClass, estimatedHeight), obj);
            }
        }

        /** Will create an array of pairs of CellInfo and item */
        private void addItems(ArrayList<T> items, Class cellClass) {
            if (items == null) return;
            for (T obj : items) {
                this.items.addPair(new CellInfo(cellClass), obj);
            }
        }

        public void setSelected (ArrayList<T> selected) {
            if (items == null || items.getArray() == null) return;
            for (Pair<CellInfo, T> obj : items.getArray()) {
                obj.getFirst().selected = selected.contains(obj.getSecond());
            }
        }

        public void setItems(PairArray items) {
            this.items = items;
        }

        @Override
        public PairArray<CellInfo, T> items() {
            return items;
        }

        public PairArray<CellInfo, T> getItems() {
            return items;
        }

        @Override
        public ArrayList<Pair<TableObject.CellInfo, T>> itemsArray() {
            return items.getArray();
        }

        public CellInfo infoAtIndex (int index) {
            if (items == null || items.size() <= index) return null;
            return items.getArray().get(index).getFirst();
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

        /** This class must be public for the data controller to automatically initialize it
         * using the no argument constructor. Otherwise you must implement header(or cell)ForViewType
         * and construct and return the appropriate view */
        public Class getCellClass() {
            return cellClass;
        }

        public int getIdentifier() {
            return identifier;
        }
    }

    public static class Section <T extends SectionData> extends BaseModel {

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
