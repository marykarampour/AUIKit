package com.prometheussoftware.auikit.model;

public class Pair <F, S> extends BaseModel {

    private F first;
    private S second;

    static {
        BaseModel.Register(Pair.class);
    }

    public Pair() { }

    public Pair(F first, S second) {
        setFirst(first);
        setSecond(second);
    }

    public static <A, B> Pair <A, B> create(A a, B b) {
        return new Pair<>(a, b);
    }

    public String toString () {
        if (getFirst() == null || getSecond() == null) return "";
        return getFirst().toString() + " " + getSecond().toString();
    }

    @Override
    public int hashCode() {
        return (first == null ? 0 : first.hashCode()) ^ (second == null ? 0 : second.hashCode());
    }

    public boolean hasNullValues () {
        return getFirst() == null && getSecond() == null;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public F getFirst() {
        return first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public S getSecond() {
        return second;
    }
}
