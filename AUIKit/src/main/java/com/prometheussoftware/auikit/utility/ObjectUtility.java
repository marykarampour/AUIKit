package com.prometheussoftware.auikit.utility;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.UUID;

public class ObjectUtility {

    public static class Params {
        protected Class<?> cls;
        protected Object object;

        public Params(Class<?> cls, Object object) {
            this.cls = cls;
            this.object = object;
        }
    }

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

    public static Object objectWithParams(Class cls, Params... parameters) {

        ArrayList<Class<?>> arrClass = new ArrayList<>();
        ArrayList<Object> arrObject = new ArrayList<>();

        for (int i = 0; i < parameters.length; i++) {
            arrClass.add(parameters[i].cls);
            arrObject.add(parameters[i].object);
        }

        Class<?>[] clss = arrClass.toArray(new Class[0]);
        Object[] objs = arrObject.toArray();
        Constructor constructor = null;

        try {
            constructor = clss.length == 0 ? cls.getDeclaredConstructor() : cls.getDeclaredConstructor(clss);
        }
        catch (NoSuchMethodException e) {
            DEBUGLOG.s(cls, " --> Failed to create item from class");
            e.printStackTrace();
            try {
                constructor = clss.length == 0 ? cls.getConstructor() : cls.getConstructor(clss);
            }
            catch (NoSuchMethodException ex) {
                DEBUGLOG.s(cls, " --> Failed to create item from class");
                ex.printStackTrace();
            }
        }

        try {
            if (constructor != null) {
                constructor.setAccessible(true);
                if (objs.length == 0)
                    return constructor.newInstance();
                return constructor.newInstance(objs);
            }
        }
        catch (IllegalAccessException e) { e.printStackTrace(); }
        catch (InstantiationException e) { e.printStackTrace(); }
        catch (InvocationTargetException e) { e.printStackTrace(); }
        return null;
    }
}
