package com.prometheussoftware.auikit.model;

import java.util.ArrayList;

public class Permutation <T> extends BaseModel {

    private ArrayList<T> items = new ArrayList<>();
    private int nextIndex;
    private int lastIndex;
    private int itemCount;

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
        if (itemCount < items.size()) {
            items = new ArrayList<>(items.subList(0, itemCount));
        }
    }

    /** @return Returns true if item is successfully set, false otherwise,
     * e.g., if the list of items already contains item. */
    public boolean setItem (T item) {
        if (items.contains(item)) return false;

        if (items.size() < itemCount) {
            items.add(item);
        }
        else if (nextIndex < items.size()){
            items.set(nextIndex, item);
        }
        lastIndex = nextIndex;
        nextIndex ++;
        nextIndex =  nextIndex % itemCount;
        return true;
    }

    public T lastItem() {
        return items.get(lastIndex);
    }

    public int size() {
        return items.size();
    }

    public T getItem(int index) {
        if (index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public ArrayList<T> getItems() {
        return items;
    }
}
