package com.prometheussoftware.auikit.model;

import com.prometheussoftware.auikit.utility.ArrayUtility;

import java.util.ArrayList;

public class PairArray <S, T> extends BaseModel {

    private ArrayList<Pair<S, T>> array = new ArrayList<>();

    public ArrayList<Pair<S, T>> getArray() {
        return array;
    }

    public PairArray() { }

    public PairArray(ArrayList<Pair<S, T>> array) {
        this.array = array;
    }

    public int size () { return array.size(); }

    static {
        BaseModel.Register(PairArray.class);
    }

    public Pair<S, T> newPair(S first, T second) {
        return new Pair(first, second);
    }

    public void addPair (S first, T second) {
        Pair<S, T> pair = newPair(first, second);
        array.add(pair);
    }

    public void addPair (Pair<S, T> pair) {
        array.add(pair);
    }

    public Pair<S, T> pairAtIndex (int index) {
        return array.get(index);
    }

    public S keyAtIndex (int index) {
        return array.get(index).getFirst();
    }

    public T valueAtIndex (int index) {
        return array.get(index).getSecond();
    }

    public void clear () { array.clear(); }

    public void addAll (ArrayList<Pair<S, T>> arr) { array.addAll(arr); }

    public ArrayList<S> allKeys () {
        ArrayList<S> list = new ArrayList<>();
        for (Pair<S, T> pair : array) {
            list.add(pair.getFirst());
        }
        return list;
    }

    public ArrayList<T> allValues () {
        ArrayList<T> list = new ArrayList<>();
        for (Pair<S, T> pair : array) {
            list.add(pair.getSecond());
        }
        return list;
    }

    public Pair<S, T> firstObject () {
        if (array.size() == 0) return null;
        return array.get(0);
    }

    public Pair<S, T> lastObject () {
        if (array.size() == 0) return null;
        return array.get(array.size() - 1);
    }

    public static <S, T, P extends Pair<S, T>> T objectForKey(S key, ArrayList<P> array) {
        if (array == null) return null;
        for (P obj : array) {
            if (obj.getFirst().equals(key)) {
                return obj.getSecond();
            }
        }
        return null;
    }

    public static <S, T, P extends Pair<S, T>> ArrayList<S> keysForObject(T object, ArrayList<P> array) {
        if (array == null) return new ArrayList<>();
        ArrayList<S> arr = new ArrayList();
        for (P obj : array) {
            if (obj.getSecond().equals(object)) {
                arr.add(obj.getFirst());
            }
        }
        return arr;
    }

    public ArrayList<S> keysForObject(T object) {
        if (this.array == null) return new ArrayList<>();
        ArrayList<S> arr = new ArrayList<>();
        for (Pair<S, T> obj : this.array) {
            if (obj.getSecond().equals(object)) {
                arr.add(obj.getFirst());
            }
        }
        return arr;
    }

    /** Returns the first key for the object */
    public S keyForObject(T object) {
        return ArrayUtility.firstObject(keysForObject(object));
    }

    public static <S, T> ArrayList<S> keysForObject(T object, PairArray<S, T> pairs) {
        return keysForObject(object, pairs.array);
    }

    public static <S, T> T objectForKey(S key, PairArray<S, T> pairs) {
        return objectForKey(key, pairs.array);
    }

    public T objectForKey(S key) {
        for (Pair<S, T> obj : array) {
            if (obj.getFirst().equals(key)) {
                return (T) obj.getSecond();
            }
        }
        return null;
    }

    public int indexOfKey (S key) {
        for (Pair<S, T> pair : array) {
            if (pair.getFirst().equals(key)) {
                return array.indexOf(pair);
            }
        }
        return -1;
    }

    public int indexOfValue (T value) {
        for (Pair<S, T> pair : array) {
            if (pair.getSecond().equals(value)) {
                return array.indexOf(pair);
            }
        }
        return -1;
    }

    public static <S, T, P extends Pair<S, T>> ArrayList<S> allKeysInArray (ArrayList<P> pairs) {
        if (pairs == null) return new ArrayList();
        ArrayList<S> keys = new ArrayList<>();
        for (P pair : pairs) {
            keys.add((S) pair.getFirst());
        }
        return keys;
    }

    public static <S, T, P extends Pair<S, T>> ArrayList<T> allValuesInArray (ArrayList<P> pairs) {
        if (pairs == null) return new ArrayList<>();
        ArrayList<T> values = new ArrayList<>();
        for (P pair : pairs) {
            values.add((T) pair.getSecond());
        }
        return values;
    }

    public Pair<S, T> pairForKey(String key) {
        for (Pair<S, T> obj : array) {
            if (obj.getFirst().equals(key)) {
                return obj;
            }
        }
        return null;
    }

}
