package com.prometheussoftware.auikit.utility;

import com.google.common.primitives.Bytes;
import com.prometheussoftware.auikit.common.Constants;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayUtility {

    public static <T extends Object> ArrayList<T> arrayOf (T object) {
        ArrayList<T> array = new ArrayList<>();
        if (object !=  null) array.add(object);
        return array;
    }

    public static String componentsJoinedByString (ArrayList<String> array, String delimiter) {
        String string = "";
        for (int i = 0; i < array.size(); i++) {
            string = string + array.get(i);
            if (i + 1 < array.size()) {
                string = string + delimiter;
            }
        }
        return string;
    }

    public static <T> ArrayList<T> arrayList (T[] array) {
        return new ArrayList<T>(Arrays.asList(array));
    }

    public static ArrayList<Byte> arrayList (byte[] array) {
        return new ArrayList(Arrays.asList(array));
    }

    public static byte[] array (ArrayList<Byte> array) {
        return Bytes.toArray(array);
    }

    /** Checks for size of array, returns null if out of bound or array is null */
    public static <T> T safeGet (ArrayList<T> array, int index) {
        return (array != null && 0 <= index && index < array.size()) ? array.get(index) : null;
    }

    /** Checks for size of array, returns null if out of bound or array is null
     * otherwise returns the element index apart from the end of the array */
    public static <T> T safeGetFromEnd (ArrayList<T> array, int index) {
        return (array != null && 0 <= index && 0 <= array.size() - index -1) ? array.get(array.size() - index - 1) : null;
    }

    /** Checks for size of array, returns NOT_FOUND_ID if out of bound or array is null */
    public static <T> int safeGetIndex (ArrayList<T> array, T obj) {
        return (array != null) ? array.indexOf(obj) : Constants.NOT_FOUND_ID;
    }

    /** Returns true if size = 0 or array is null */
    public static <T> boolean isEmpty (ArrayList<T> array) {
        return array == null || array.size() == 0;
    }

    /** Checks for size of array, returns 0 if no elements or array is null */
    public static <T> int safeGetSize (ArrayList<T> array) {
        return array != null ? array.size() : 0;
    }

    public static <T> T firstObject (ArrayList<T> array) {
        return safeGet(array, 0);
    }

    public static <T> T lastObject (ArrayList<T> array) {
        if (array == null) return null;
        return safeGet(array, array.size()-1);
    }

    /** If index is beyond the bounds of array it will add the object, otherwise
     * it will set it at the given index */
    public static <T> void safeReplace (ArrayList<T> array, int index, T obj) {
        if (array == null || obj == null || index < 0) return;
        if (array.size() <= index) {
            array.add(obj);
        }
        else {
            array.set(index, obj);
        }
    }
}
