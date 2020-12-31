package com.prometheussoftware.auikit.networking;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Converter;

public class ExampleConverterFactory extends ConverterFactory {

    private static final int BASE = ConverterFactory.BASE_TYPE;

    public ExampleConverterFactory(LinkedHashMap<Class<?>, Converter.Factory> map) {
        super(map);
    }

    public enum TYPE {

        EX(BASE),
        EXE(BASE+1);

        private int value;

        TYPE(int i) { value = i; }

        private static HashMap<Integer, TYPE> map = new HashMap<>();

        static {
            for (TYPE type : values()) {
                map.put(type.value, type);
            }
        }

        public int intValue() { return value; }

        public static TYPE valueOf(int i) { return map.get(i); }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface EXE {}

    public static class Builder extends ConverterFactory.Builder <ExampleConverterFactory> {

        public ExampleConverterFactory create() {
            return new ExampleConverterFactory(map);
        }

        @Override public <T extends Annotation> Builder add(Class<T> type, Converter.Factory factory) {
            return (Builder) super.add(type, factory);
        }
    }

    public static class ConverterConstructor extends GsonConverterConstructor {

        public static GsonConverter ofType(int type) {

            TYPE cType = TYPE.valueOf(type);
            switch (cType) {
                case EXE: return new GsonEXE();
                default: return new GsonDefault();
            }
        }
    }

    protected static class GsonEXE extends GsonConverter {

        GsonEXE() {
            super();
            type = TYPE.EXE.intValue();
        }

        @Override protected void createGson() {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(Date.class, new DateFormatter.DateJsonDeserializer());
            gson.registerTypeAdapter(Date.class, new DateFormatter.DateJsonSerializer());

            gson.enableComplexMapKeySerialization()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .setPrettyPrinting();
            this.gson = gson.create();
        }
    }
}
