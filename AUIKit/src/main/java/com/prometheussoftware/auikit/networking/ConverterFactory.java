package com.prometheussoftware.auikit.networking;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConverterFactory  extends Converter.Factory {

    public static final int BASE_TYPE = ConverterFactory.TYPE.DEFAULT.intValue();

    public enum TYPE {
        DEFAULT(0);

        private int value;

        TYPE(int i) { value = i; }

        private static HashMap<Integer, TYPE> map = new HashMap<>();

        static {
            for (TYPE type : values()) {
                map.put(type.value, type);
            }
        }

        public int intValue() { return value; }
    }

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Default {}

    private LinkedHashMap<Class<?>, Converter.Factory> map = new LinkedHashMap<>();

    protected ConverterFactory(LinkedHashMap<Class<?>, Converter.Factory> map) {
        this.map = map;
    }

    protected ConverterFactory() { }

    public static class Builder <C extends ConverterFactory> {

        protected LinkedHashMap<Class<?>, Converter.Factory> map = new LinkedHashMap<>();

        public  <T extends Annotation> Builder add(Class<T> type, Converter.Factory factory) {
            if (type == null || factory == null) return this;
            map.put(type, factory);
            return this;
        }

        public C create() {
            return (C)new ConverterFactory(map);
        }
    }

    @Override public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {

        for (Annotation annotation : annotations) {
            Converter.Factory factory = map.get(annotation.annotationType());
            if (factory != null) {
                return factory.responseBodyConverter(type, annotations, retrofit);
            }
        }

        Converter.Factory defaultFactory = map.get(type);
        if (defaultFactory != null) {
            return defaultFactory.responseBodyConverter(type, annotations, retrofit);
        }
        return null;
    }

    @Override public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {

        for (Annotation annotation : methodAnnotations) {
            Converter.Factory factory = map.get(annotation.annotationType());
            if (factory != null) {
                return factory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
            }
        }

        Converter.Factory defaultFactory = map.get(type);
        if (defaultFactory != null) {
            return defaultFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
        }
        return null;
    }

    public static class GsonConverterConstructor {

        public static GsonConverter ofType(int type) {
            return new GsonDefault();
        }
    }

    public static abstract class GsonConverter {

        protected Gson gson;
        protected int type;

        protected GsonConverter() {
            createGson();
        }

        protected abstract void createGson();

        public GsonConverterFactory converter() {
            return GsonConverterFactory.create(gson);
        }
    }

    protected static class GsonDefault extends GsonConverter {

        GsonDefault() {
            super();
            type = TYPE.DEFAULT.intValue();
        }

        @Override protected void createGson() {
            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(Date.class, new DateFormatter.DateJsonDeserializer());
            gson.registerTypeAdapter(Date.class, new DateFormatter.DateJsonSerializer());

            gson.enableComplexMapKeySerialization()
                    .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                    .setPrettyPrinting();
            this.gson = gson.create();
        }
    }
}
