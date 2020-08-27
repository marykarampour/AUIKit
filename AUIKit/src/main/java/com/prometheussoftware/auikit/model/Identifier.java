package com.prometheussoftware.auikit.model;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Identifier {

    private static HashMap<Class, Integer> Registrar = new HashMap<>();
    private static AtomicInteger lastIdentifier = new AtomicInteger();

    public static int generateIdentifier() {
        if (Integer.MAX_VALUE <= lastIdentifier.intValue())
            lastIdentifier = new AtomicInteger();
        return lastIdentifier.incrementAndGet();
    }

    /** Call this in static method of your class to register a unique identifier */
    public static void Register (Class cls) {
        if (Registrar.get(cls) != null) return;
        Registrar.put(cls, generateIdentifier());
    }

    public static int getIdentifier(Class cls) {
        Integer id = Registrar.get(cls);
        if (id == null) {
            Register(cls);
        }
        return Registrar.get(cls);
    }
}
