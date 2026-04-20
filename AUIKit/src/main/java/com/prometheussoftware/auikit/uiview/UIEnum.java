package com.prometheussoftware.auikit.uiview;

import java.util.HashMap;

public class UIEnum {

    public enum DIRECTION {
        VERTICAL,
        HORIZONTAL
    }

    public enum ALIGNMENT {
        TOP(0),
        BOTTOM(1),
        LEFT(1 << 1),
        RIGHT(1 << 2),
        CENTER_X(1 << 3),
        CENTER_Y(1 << 4);

        private final int value;
        private static final HashMap<Integer, ALIGNMENT> map = new HashMap<>();

        static {
            for (ALIGNMENT al : values()) {
                map.put(al.value, al);
            }
        }

        ALIGNMENT(int i) { value = i; }

        public int intValue() { return value; }

        public static ALIGNMENT valueOf (int i) {
            return map.get(i);
        }

        public boolean isOption (int option) {
            return (option & value) == value;
        }

        public boolean isValue (int option) {
            return (option & value) == option;
        }

        public boolean isOption (ALIGNMENT option) {
            return (option.value & value) == value;
        }
    }

}
