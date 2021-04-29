package com.prometheussoftware.auikit.model;

import com.google.gson.Gson;
import com.prometheussoftware.auikit.utility.DEBUGLOG;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class BaseModel implements Serializable, Cloneable {

    public BaseModel () {}

    private static HashMap<Class, Reflect> Registrar = new HashMap<>();

    public static class Reflect {
        private boolean usingAncestors;
        private Class base = BaseModel.class;
        private Class self;
        private HashMap<String, Class> propertyTypeNames;

        public Class getBase() {
            return base;
        }

        public Class getSelf() {
            return self;
        }

        public HashMap<String, Class> getPropertyTypeNames() {
            return propertyTypeNames;
        }

        public boolean getUsingAncestors() {
            return usingAncestors;
        }
    }

    /** Call this method to enable reflection on the class.
     * @apiNote This must be called early on in static methods.
     * One option is to have a model registrar class called early
     * on to register all the required classes. */
    public static void Register (Class subclass) {
        Register(subclass, true);
    }

    /** Call this method to enable reflection on the class.
     * @apiNote This must be called early on in static methods.
     * One option is to have a model registrar class called early
     * on to register all the required classes.
     * @param usingAncestors Pass true if you want fields of the ancestors be included */
    public static void Register (Class subclass, boolean usingAncestors) {

        Reflect reflect = reflectForClass(subclass);
        if (reflect != null) return;

        reflect = new Reflect();
        reflect.usingAncestors = usingAncestors;
        reflect.self = subclass;
        reflect.propertyTypeNames = initializePropertyTypeNames(reflect);
        Registrar.put(subclass, reflect);
    }

    public static boolean classHasRegisteredField (Class objectClass, String field) {
        Set<String> names = propertyNamesForClass(objectClass);
        if (names == null) return false;
        return names.contains(field);
    }

    public static Reflect reflectForClass(Class objectClass) {
        return Registrar.get(objectClass);
    }

    public static Set<String> propertyNamesForClass (Class objectClass) {
        Reflect reflect = reflectForClass(objectClass);
        return reflect != null ? reflect.getPropertyTypeNames().keySet() : null;
    }

    public static HashMap<String, Class> initializePropertyTypeNames (Reflect object) {
        if (object.self == null) return null;
        if (object.usingAncestors) {

            HashMap<String, Class> map =  new HashMap<>();
            Class currentClass = object.self;

            while (currentClass != object.base && currentClass != Object.class) {
                map.putAll(attributePropertyNamesOfClass(currentClass));
                currentClass = currentClass.getSuperclass();
            }
            return map;
        }
        else {
            return attributePropertyNamesOfClass(object.self);
        }
    }

    public static HashMap<String, Class> attributePropertyNamesOfClass (Class objectClass) {

        HashMap<String, Class> map =  new HashMap<>();
        for (Field field : objectClass.getDeclaredFields()) {
            if ((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) continue;
            map.put(field.getName(), field.getType());
        }
        return map;
    }

    @Override
    public int hashCode() {
        int hashPrime = 179;
        int hashEven = 178;
        int hashResult = 1;
        int hashObjects = 1;

        Set<String> properties = BaseModel.propertyNamesForClass(getClass());

        if (properties == null) return hashPrime;

        for (String name : properties) {

            try {
                Field field = getClass().getField(name);
                if (field != null) {
                    Object object = field.get(this);
                    if (Object.class.isInstance(object)) {
                        hashObjects = hashObjects ^ object.hashCode();
                    }
                    else {
                        hashResult = hashResult*hashPrime + (object != null ? hashPrime : hashEven);
                    }
                }

            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) { }
        }

        return hashResult + hashObjects;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || (object.getClass() != getClass())) {
            return false;
        }

        Set<String> properties = BaseModel.propertyNamesForClass(getClass());

        if (properties == null) return true;

        for (String name : properties) {

            try {
                Field field = fieldOrDeclared(name);

                if (field != null) {
                    field.setAccessible(true);
                    Object valueObject = field.get(object);
                    Object valueSelf = field.get(this);

                    if (valueObject != null || valueSelf != null) {
                        if (Object.class.isInstance(valueObject) && Object.class.isInstance(valueSelf)) {
                            if (valueObject != null && valueSelf != null) {
                                if (!valueObject.equals(valueSelf)) {
                                    return false;
                                }
                            }
                            else {
                                return false;
                            }
                        }
                        else if (valueObject != valueSelf) {
                            return false;
                        }
                    }
                }
            } catch (IllegalAccessException e) { }
        }
        return true;
    }

    public BaseModel (BaseModel obj) {
        super();
        resetWithObject(obj);
    }

    public static BaseModel newInstance(BaseModel obj) {
        return new BaseModel(obj);
    }

    public void resetWithObject (BaseModel obj) {

        if (obj == null) {
            nullify();
            return;
        }

        Set<String> properties = BaseModel.propertyNamesForClass(getClass());

        if (properties == null) return;

        for (String name : properties) {

            try {
                Field field = fieldOrDeclared(name);
                Field objField = obj.fieldOrDeclared(name);

                if (field != null && objField != null) {

                    field.setAccessible(true);
                    objField.setAccessible(true);

                    Object value = objField.get(obj);
                    if (value != null) {
                        field.set(this, value);
                    }
                }

            } catch (IllegalAccessException e) {
                DEBUGLOG.s(e);
            }
        }
    }

    /** This method only works for public fields of a class and its ancestors */
    public void setValueForKey (Object value, String key) {

        try {
            Field field = getClass().getField(key);

            if (field != null) {

                Method setter = setter(field);
                if (setter != null) setter.invoke(this, value);
                else field.set(this, value);
            }
        } catch (NoSuchFieldException | InvocationTargetException e) {
        } catch (IllegalAccessException e) { }
    }

    /** This method iterates through all fields of a class and its ancestors
     * even private and protected, and sets the value for the field if found.
     * @apiNote This method is more demanding than setValueForKey, use
     * setValueForKey instead unless necessary */
    public void setValueForKeyForAllAccessLevels (Object value, String key) {

        try {
            Field field = fieldOrDeclared(key);
            if (field != null) {

                Method setter = setter(field);
                if (setter != null) setter.invoke(this, value);
                else field.set(this, value);
                return;
            }
        } catch (InvocationTargetException | IllegalAccessException e) { }
    }

    public Object valueForKey (String key) {

        try {
            Field field = fieldOrDeclared(key);

            if (field != null) {
                field.setAccessible(true);
                return field.get(this);
            }
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    /** This method looks for the field in all fields of a class and its ancestors
     * even private and protected. */
    private Field fieldOrDeclared (String key) {

        Class cls = getClass();
        while (cls != BaseModel.class) {
            try {
                Field field = cls.getDeclaredField(key);
                if (field != null) {
                    return field;
                }
            }
            catch (NoSuchFieldException e) { }
            cls = cls.getSuperclass();
        }
        return null;
    }

    public static boolean isSetter(Method method) {

        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }

    public static boolean isGetter(Method method) {

        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {

            if (method.getName().matches("^get[A-Z].*") &&
                    !method.getReturnType().equals(void.class))
                return true;
            if (method.getName().matches("^is[A-Z].*") &&
                    method.getReturnType().equals(boolean.class))
                return true;
        }
        return false;
    }

    public Method setter(Field field) {

        Method[] methods = this.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName()
                    .equalsIgnoreCase("set" + field.getName()))
                return method;
        }
        return null;
    }

    public void nullify() {

        Set<String> properties = BaseModel.propertyNamesForClass(getClass());

        if (properties == null) return;

        for (String name : properties) {
            setValueForKeyForAllAccessLevels(null, name);
        }
    }

    public void setDefaults() {

        Set<String> properties = BaseModel.propertyNamesForClass(getClass());

        if (properties == null) return;

        for (String name : properties) {

            try {
                Field field = fieldOrDeclared(name);
                if (field != null) {

                    if (field.getType().isAssignableFrom(Number.class)) {
                        field.set(this, 0);
                    }
                    else if (field.getType().isAssignableFrom(String.class)) {
                        field.set(this, "");
                    }
                    else {
                        Method setter = setter(field);
                        if (setter != null) {
                            setter.invoke(this, (Object) null);
                        }
                        else field.set(this, null);
                    }
                    return;
                }
            } catch (InvocationTargetException | IllegalAccessException e) { }
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object obj = super.clone();

        Set<String> properties = BaseModel.propertyNamesForClass(getClass());

        if (properties == null) return null;

        for (String name : properties) {

            try {
                Field field = fieldOrDeclared(name);
                if (field != null) {
                    field.setAccessible(true);
                    Object value = field.get(this);
                    if (value != null) {

                        if (ArrayList.class.isAssignableFrom(value.getClass())) {

                            ArrayList<BaseModel> array = (ArrayList)value;
                            ArrayList clonedArray = new ArrayList();

                            for (Object object : array) {
                                Object cloned = clonedValue(object);
                                if (cloned != null) clonedArray.add(cloned);
                            }
                            field.set(obj, clonedArray);
                        }
                        else {
                            field.set(obj, clonedValue(value));
                        }
                    }
                }

            } catch (IllegalAccessException e) { }
        }
        return obj;
    }

    private Object clonedValue (Object value) {
        Method method = null;

        try {
            if (hasMethod(value.getClass(), "clone", true)) {
                method = value.getClass().getDeclaredMethod("clone");
            }
            else if (hasMethod(value.getClass(), "clone", false)) {
                method = value.getClass().getMethod("clone");
            }
        }
        catch (NoSuchMethodException e) { }

        try {
            if (method != null) {
                method.setAccessible(true);
                Object clonedValue = method.invoke(value);
                return clonedValue;
            }
            else {
                return value;
            }
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) { }
        return null;
    }

    public static boolean hasMethod (Class cls, String name, boolean isDeclared) {
        Method[] methods = isDeclared ? cls.getDeclaredMethods() : cls.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static <T extends BaseModel> ArrayList<T> cloneArray (ArrayList<T> array) {
        ArrayList<T> arr = new ArrayList();
        for (T obj : array) {
            try {
                arr.add((T)obj.clone());
            } catch (CloneNotSupportedException e) { }
        }
        return arr;
    }

    /** If clone is supported returns a cloned object from obj
     * else returns null */
    public static <T extends BaseModel> T copy(T obj) {
        try {
            return (T) obj.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public static <T extends Object> T elementInArray (ArrayList<T> arr, Class<T> objectClass, String name, Object value) {

        try {
            Field field = objectClass.getField(name);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return arr.stream().filter(q -> {
                    try {
                        return value.equals(field.get(q));
                    } catch (IllegalAccessException e) { }
                    return false;
                }).findAny().orElse(null);
            }
            else {
                for (T item : arr) {
                    try {
                        if (value.equals(field.get(item))) return item;
                    } catch (IllegalAccessException e) { }
                }
            }
        } catch (NoSuchFieldException e) { }
        return null;
    }

    public void setWithObject(BaseModel object) {

        if (!object.getClass().isInstance(this)) return;

        Set<String> properties = BaseModel.propertyNamesForClass(object.getClass());

        if (properties == null) return;

        for (String name : properties) {

            try {
                Field objField = object.fieldOrDeclared(name);
                if (objField != null) {
                    objField.setAccessible(true);
                    Object value = objField.get(object);
                    setValueForKeyForAllAccessLevels(value, name);
                }
            } catch (IllegalAccessException e) { }
        }
    }

    public static String stringJSON(BaseModel obj) {
        return obj.stringJSON();
    }

    public String stringJSON() {
        return new Gson().toJson(this);
    }
}
