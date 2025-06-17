package com.prometheussoftware.auikit.model;

import java.util.HashMap;
import java.util.Map;

public enum DATE_FORMAT {

    FULL_STYLE(0, "yyyy-MM-dd'T'HH:mm:ss.SSS");

    DATE_FORMAT(int i, String s) {
        this.index = i;
        this.name = s;
    }

    private final int index;
    private final String name;

    public int getIndex() {
        return this.index;
    }

    public String getName() {
        return name;
    }

    private static final Map map = new HashMap<>();

    static {
        for (DATE_FORMAT format : DATE_FORMAT.values()) {
            map.put(format.index, format.name);
        }
    }

    public static DATE_FORMAT valueOf(int index) {
        return (DATE_FORMAT) map.get(index);
    }
}
