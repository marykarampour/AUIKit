package com.prometheussoftware.auikit.model;

public class Range extends BaseModel implements Comparable<Range> {

    public int location;
    public int length;

    public Range (int location, int length) {
        this.location = location;
        this.length = length;
    }

    public Range () {
        this.location = 0;
        this.length = 0;
    }

    @Override public int compareTo(Range o) {
        if (this.location > o.location) return 1;
        if (this.location < o.location) return -1;
        return 0;
    }
}
