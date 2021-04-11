package com.prometheussoftware.auikit.networking;

import androidx.annotation.Nullable;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.prometheussoftware.auikit.utility.StringUtility;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class NetworkManager {

    private static HashMap<Integer, Retrofit> retrofits = new HashMap<>();

    public static NetworkManager getInstance() { return NetworkManagerInitializer.instance; }

    private NetworkManager () {}

    private static class NetworkManagerInitializer {
        private static final NetworkManager instance = new NetworkManager();
    }

    public static <T> T createService(Class <T> T) {
        if (retrofits.size() == 0) return null;
        return T.cast(retrofits.get(0).create(T));
    }

    public static <T> T createService(Class <T> T, Integer type) {
        if (retrofits.get(type) == null) return null;
        return T.cast(retrofits.get(type).create(T));
    }

    public String preparedURL (String URL) {

        if (StringUtility.isEmpty(URL)) return "";
        if (!URL.endsWith("/")) {
            URL = URL + "/";
        }
        return URL;
    }

    public void setBaseURL (String URL) {
        addBaseURL(URL, 0);
    }

    public <C extends ConverterFactory> void setBaseURL (String URL, C converter) {
        addBaseURL(URL, 0, converter);
    }

    public void setBaseURL (String URL, @Nullable JsonDeserializer<Date> deserializer, @Nullable JsonSerializer<Date> serializer) {
        addBaseURL(URL, 0, deserializer, serializer);
    }

    public void addBaseURL (String URL, Integer type) {

        if (StringUtility.isEmpty(URL)) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preparedURL(URL))
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofits.put(type, retrofit);
    }

    public <C extends ConverterFactory> void addBaseURL (String URL, Integer type, C converter) {

        if (StringUtility.isEmpty(URL)) return;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preparedURL(URL))
                .client(httpClient())
                .addConverterFactory(converter)
                .build();

        retrofits.put(type, retrofit);
    }

    public void addBaseURL (String URL, Integer type, @Nullable JsonDeserializer<Date> deserializer, @Nullable JsonSerializer<Date> serializer) {

        if (StringUtility.isEmpty(URL)) return;

        GsonBuilder gson = new GsonBuilder();
        if (deserializer != null) gson.registerTypeAdapter(Date.class, deserializer);
        if (serializer != null) gson.registerTypeAdapter(Date.class, serializer);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(preparedURL(URL))
                .client(httpClient())
                .addConverterFactory(GsonConverterFactory.create(gson.create()))
                .build();

        retrofits.put(type, retrofit);
    }

    private OkHttpClient httpClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        return client;
    }
}
