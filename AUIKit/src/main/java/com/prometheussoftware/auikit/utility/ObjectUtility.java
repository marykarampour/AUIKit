package com.prometheussoftware.auikit.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class ObjectUtility {

    public static String logTag (Object obj) {
        return obj.getClass().getSimpleName();
    }

    public static <T> T castTo (Object obj) {
        try {
            return (T)obj;
        }
        catch (Exception e) {
            return null;
        }
    }

    public static String GUID () {
        return UUID.randomUUID().toString();
    }

    public static Constructor constructorWithParams(Class cls, Class<?>... parameterTypes) {
        try {
            return cls.getDeclaredConstructor(parameterTypes);
        }
        catch (NoSuchMethodException e) {
            DEBUGLOG.s(cls, " --> Failed to create item from class");
            e.printStackTrace();
            try {
                return cls.getConstructor(parameterTypes);
            }
            catch (NoSuchMethodException ex) {
                DEBUGLOG.s(cls, " --> Failed to create item from class");
                ex.printStackTrace();
                return null;
            }
        }
    }

    public static Object objectWithParams(Class cls, Class<?>... parameterTypes) {
        try {
            Constructor constructor = constructorWithParams(cls, parameterTypes);
            if (constructor != null) {
                constructor.setAccessible(true);
                return constructor.newInstance();
            }
        }
        catch (IllegalAccessException e) { e.printStackTrace(); }
        catch (InstantiationException e) { e.printStackTrace(); }
        catch (InvocationTargetException e) { e.printStackTrace(); }
        return null;
    }
}
