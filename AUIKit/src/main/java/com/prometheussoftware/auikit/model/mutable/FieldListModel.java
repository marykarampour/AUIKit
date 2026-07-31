package com.prometheussoftware.auikit.model.mutable;

import com.prometheussoftware.auikit.model.ArrayPropertyProtocol;
import com.prometheussoftware.auikit.model.BaseModel;
import com.prometheussoftware.auikit.uiview.protocols.ViewContentProtocol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @brief A model object that supports max generic list types.
 * For NSObject, override description and return the title to be presented in UI.
 * If conforming to MKUPlaceholderProtocol title  will be used.
 * @note If there is no items, it sets the footer to noItemAvailableTitleForListOfType: */
public class FieldListModel
               <ObjectTypeA extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
                ObjectTypeB extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
                ObjectTypeC extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol,
                ObjectTypeD extends ViewContentProtocol.Placeholder & ArrayPropertyProtocol> extends FieldModel {

    public ObjectTypeA itemsA;
    public ObjectTypeB itemsB;
    public ObjectTypeC itemsC;
    public ObjectTypeD itemsD;

    static {
        BaseModel.Register(FieldListModel.class);
    }

    /** @brief The array that itemsA returns. */
    public List<ObjectTypeA> arrayA() {
        return itemsA.array();
    }

    /** @brief The array that itemsB returns. */
    public List<ObjectTypeA> arrayB() {
        return itemsB.array();
    }

    /** @brief The array that itemsC returns. */
    public List<ObjectTypeA> arrayC() {
        return itemsC.array();
    }

    /** @brief The array that itemsD returns. */
    public List<ObjectTypeA> arrayD() {
        return itemsD.array();
    }

    public LIST_TYPE activeListTypes = LIST_TYPE.NONE;

    public int activeListsCount() {
        int count = 0;
        int value = 0;
        for (int i=LIST_TYPE.NONE.intValue(); value<LIST_TYPE.COUNT.intValue(); i++) {
            value = 1 << i;
            if ((value & activeListTypes.intValue()) == value) count++;
        }
        return count;
    }

    @Override
    public Map<Integer, String> propertyEnumDictionary() {
        Map<Integer, String> map = new HashMap();
        map.put(LIST_TYPE.A.intValue(), "itemsA");
        map.put(LIST_TYPE.B.intValue(), "itemsB");
        map.put(LIST_TYPE.C.intValue(), "itemsC");
        map.put(LIST_TYPE.D.intValue(), "itemsD");
        return map;
    }

    public enum LIST_TYPE {
        NONE  (0),
        A     (1 << 0),
        B     (1 << 1),
        C     (1 << 2),
        D     (1 << 3),
        COUNT (1 << 4);

        private final int value;
        private static final HashMap<Integer, LIST_TYPE> map = new HashMap<>();

        static {
            for (LIST_TYPE type : values()) {
                map.put(type.value, type);
            }
        }

        LIST_TYPE(int i) { value = i; }

        public int intValue() { return value; }

        public static LIST_TYPE valueOf (int i) {
            return map.get(i);
        }

        public boolean isOption (int option) {
            return (option & value) == value;
        }

        public boolean isValue (int option) {
            return (option & value) == option;
        }

        public boolean isOption (LIST_TYPE option) {
            return (option.value & value) == value;
        }

        public static LIST_TYPE option (int i) {
            return valueOf(1 << i);
        }
    }
}
