package com.prometheussoftware.auikit.tableview.menu;

import com.prometheussoftware.auikit.classes.UIColor;
import com.prometheussoftware.auikit.classes.UIFont;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.model.Pair;
import com.prometheussoftware.auikit.model.PairArray;
import com.prometheussoftware.auikit.tableview.TableObject;
import com.prometheussoftware.auikit.uiviewcontroller.UIViewController;
import com.prometheussoftware.auikit.utility.DEBUGLOG;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class MenuObject {

    public static class SectionData extends TableObject.SectionData {

        public UIFont font;
        public UIColor textColor;
        public UIColor backgroundColor;

        public SectionData (String title, String subTitle) {
            super(title, subTitle);
        }

        public SectionData (String title) {
            super(title);
        }
    }

    public static class Section extends TableObject.Section <SectionData> {

        public void setRows(RowData rows) {
            this.rows = rows;
        }

        public Section (SectionData sectionData, ArrayList<Item> items, Class cellClass) {
            object = sectionData;
            rows = new RowData(items, cellClass);
        }

        public Section (SectionData sectionData, ArrayList<Item> items, Class cellClass, boolean instanceId) {
            object = sectionData;
            rows = new RowData(items, cellClass, instanceId);
        }
    }

    public static class RowData extends TableObject.RowData <Item> {

        private PairArray<TableObject.CellInfo, MenuObject.Item, Pair<TableObject.CellInfo, MenuObject.Item>> menuItems = new PairArray();

        /** Will create an array of pairs of CellInfo and item */
        public RowData(ArrayList<Item> items, Class cellClass) {
            super();
            if (items == null) return;

            PairArray<TableObject.CellInfo, Item, Pair<TableObject.CellInfo, Item>> pairs = new PairArray<>();
            for (Item obj : items) {
                pairs.addPair(new TableObject.CellInfo(cellClass), obj);
            }
            setMenuItems(pairs);
        }

        /** Will create an array of pairs of CellInfo and item */
        public RowData(ArrayList<Item> items, Class cellClass, boolean instanceId) {
            super();
            if (items == null) return;

            PairArray<TableObject.CellInfo, Item, Pair<TableObject.CellInfo, Item>> pairs = new PairArray<>();
            for (Item obj : items) {
                pairs.addPair(new TableObject.CellInfo(cellClass, instanceId), obj);
            }
            setMenuItems(pairs);
        }

        public PairArray<TableObject.CellInfo, MenuObject.Item, Pair<TableObject.CellInfo, MenuObject.Item>> getMenuItems() {
            return menuItems;
        }

        public void setMenuItems(PairArray<TableObject.CellInfo, Item, Pair<TableObject.CellInfo, Item>> menuItems) {
            this.menuItems = menuItems;
            setItems(menuItems);
        }

        @Override
        public PairArray<TableObject.CellInfo, Item, Pair<TableObject.CellInfo, Item>> items() {
            return menuItems;
        }
    }

    public static class Item extends BaseModel {

        public String title;
        public UIFont font;
        public UIColor textColor;
        public boolean hidden;
        public Action action;
        private Class VCClass;

        /** In case the VC is a subclass initialized based on type
         * set trueVCClass to that class, default is VCClass.
         * This is set in viewController() */
        private Class trueVCClass;
        private int type;

        public void setTrueVCClass() {
            viewController();
        }

        public void setTrueVCClass(Class trueVCClass) {
            this.trueVCClass = trueVCClass;
        }

        public Class getTrueVCClass() {
            return trueVCClass == null ? VCClass : trueVCClass;
        }

        public void setVCClass(Class VCClass) {
            this.VCClass = VCClass;
//        setTrueVCClass();
        }

        public Class getVCClass() {
            return VCClass;
        }

        public void setType(int type) {
            this.type = type;
//        setTrueVCClass();
        }

        public int getType() {
            return type;
        }

        public <VC extends UIViewController> VC viewController() {
            //TODO: do not init if already exists
            //TODO: add support for builder using type

            UIViewController nextViewController = null;

            if (UIViewController.class.isAssignableFrom(VCClass)) {

                try {
                    Constructor constructor = VCConstructor(int.class);
                    if (constructor == null) {
                        constructor = VCConstructor();
                        if (constructor != null) {
                            constructor.setAccessible(true);
                            nextViewController = (UIViewController) constructor.newInstance();
                        }
                    }
                    else {
                        constructor.setAccessible(true);
                        nextViewController = (UIViewController) constructor.newInstance(type);
                    }
                }
                catch (IllegalAccessException e) { e.printStackTrace(); }
                catch (InstantiationException e) { e.printStackTrace(); }
                catch (InvocationTargetException e) { e.printStackTrace(); }
            }
            if (nextViewController != null) {
                trueVCClass = nextViewController.getClass();
                nextViewController.setTitle(title);
            }
            return (VC) nextViewController;
        }

        private Constructor VCConstructor(Class<?>... parameterTypes) {
            try {
                return VCClass.getDeclaredConstructor(parameterTypes);
            }
            catch (NoSuchMethodException e) {
                DEBUGLOG.s(VCClass, " --> Failed to create menu item from VCClass");
                e.printStackTrace();
                try {
                    return VCClass.getConstructor(parameterTypes);
                }
                catch (NoSuchMethodException ex) {
                    DEBUGLOG.s(VCClass, " --> Failed to create menu item from VCClass");
                    ex.printStackTrace();
                    return null;
                }
            }
        }

        public Action getAction() {
            return action;
        }

        public interface Action {
            void itemPressed(Item item);
        }
    }

    static {
        BaseModel.Register(Section.class);
        BaseModel.Register(SectionData.class);
        BaseModel.Register(RowData.class);
        BaseModel.Register(Item.class);
    }

}
