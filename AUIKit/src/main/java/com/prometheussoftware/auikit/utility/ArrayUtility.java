package com.prometheussoftware.auikit.utility;

import com.google.common.primitives.Bytes;
import com.prometheussoftware.auikit.common.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ArrayUtility {

    public static <T extends Object> List<T> arrayOf (T object) {
        List<T> array = new ArrayList<>();
        if (object !=  null) array.add(object);
        return array;
    }

    public static <T extends Object> List<T> arrayOf (T ...objects) {

        List<T> array = new ArrayList<>();
        if (objects == null) return array;

        Collections.addAll(array, objects);
        return array;
    }

    public static String componentsJoinedByString (List<String> array, String delimiter) {
        String string = "";
        for (int i = 0; i < array.size(); i++) {
            string = string + array.get(i);
            if (i + 1 < array.size()) {
                string = string + delimiter;
            }
        }
        return string;
    }

    public static int sum (List<Integer> array) {
        int total = 0;
        for (int i : array) {
            total += i;
        }
        return total;
    }

    public static <T> List<T> arrayList (T[] array) {
        return new ArrayList<T>(Arrays.asList(array));
    }

    public static List<Byte> arrayList (byte[] array) {
        return new ArrayList(Arrays.asList(array));
    }

    public static byte[] array (List<Byte> array) {
        return Bytes.toArray(array);
    }

    public static <T> List<T> nonnullArrayList (List<T> array) {
        return array == null ? new ArrayList() : array;
    }

    /** Checks for size of array, returns null if out of bound or array is null */
    public static <T> T safeGet (List<T> array, int index) {
        return (array != null && 0 <= index && index < array.size()) ? array.get(index) : null;
    }

    /** Checks for size of array, returns null if out of bound or array is null
     * otherwise returns the element index apart from the end of the array */
    public static <T> T safeGetFromEnd (List<T> array, int index) {
        return (array != null && 0 <= index && 0 <= array.size() - index -1) ? array.get(array.size() - index - 1) : null;
    }

    /** Checks for size of array, returns NOT_FOUND_ID if out of bound or array is null */
    public static <T> int safeGetIndex (List<T> array, T obj) {
        return (array != null) ? array.indexOf(obj) : Constants.NOT_FOUND_ID;
    }

    /** Returns true if size = 0 or array is null */
    public static <T> boolean isEmpty (List<T> array) {
        return array == null || array.size() == 0;
    }

    /** Checks for size of array, returns 0 if no elements or array is null */
    public static <T> int safeGetSize (List<T> array) {
        return array != null ? array.size() : 0;
    }

    public static <T> T firstObject (List<T> array) {
        return safeGet(array, 0);
    }

    public static <T> T lastObject (List<T> array) {
        if (array == null) return null;
        return safeGet(array, array.size()-1);
    }

    /** If index is beyond the bounds of array it will add the object, otherwise
     * it will set it at the given index */
    public static <T> void safeReplace (List<T> array, int index, T obj) {
        if (array == null || obj == null || index < 0) return;
        if (array.size() <= index) {
            array.add(obj);
        }
        else {
            array.set(index, obj);
        }
    }

    /** It will remove the item if it exists in the array */
    public static <T> void safeRemove (List<T> array, T obj) {
        if (array == null || obj == null) return;
        array.remove(obj);
    }

    /** It will remove the item at index if the index is within the bound of the array */
    public static <T> void safeRemove (List<T> array, int index) {
        if (array == null || index < 0 || array.size() <= index) return;
        array.remove(index);
    }

    /** If object doesn't exist in the array it will add the object.
     * If index is beyond the bounds of array it will move the object to the end.
     * Otherwise, it will move it to the given index
     * */
    public static <T> void safeMove (List<T> array, int index, T obj) {
        if (array == null || obj == null || index < 0) return;
        if (array.contains(obj)) {

            array.remove(obj);

            if (array.size() <= index) {
                array.add(obj);
            }
            else {
                array.add(index, obj);
            }
        }
        else if (array.size() <= index) {
            array.add(obj);
        }
        else {
            array.set(index, obj);
        }
    }

    /** @brief If object is already in the array, nothing will happen.
     @return A BOOL indicating if the object was added to the array. */
    public static <T> boolean addUniqueObject (List<T> otherArray, T anObject) {
        if (otherArray.contains(anObject)) return false;
        otherArray.add(anObject);
        return true;
    }

    /** @brief If object is already in the array, nothing will happen.
    @return Objects that failed to be added to the array. */
    public static <T> List<T> addUniqueObjectsFromArray (List<T> array, List<T> otherArray) {
        List<T> existing = new ArrayList<>();
        for (T obj : otherArray) {
            if (!addUniqueObject(array, obj))
                existing.add(obj);
        }
        return existing;
    }

    /** @brief If object is already in the array, it will be replaced by the one from otherArray.
    @return Objects that were replaced. */
    public static <T> List<T> addOrReplaceUniqueObjectsFromArray (List<T> array, List<T> otherArray) {
        List<T> existing = new ArrayList<>();
        for (T obj : otherArray) {
            if (!addUniqueObject(array, obj)) {
                existing.add(obj);
                array.set(array.indexOf(obj), obj);
            }
        }
        return existing;
    }
}
