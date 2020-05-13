package com.prometheussoftware.auikit.networking;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prometheussoftware.auikit.model.DATE_FORMAT;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static class DateJsonDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            for (DATE_FORMAT format : DATE_FORMAT.values()) {

                try {
                    return new SimpleDateFormat(format.getName(), Locale.US).parse(json.getAsString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public static class DateJsonSerializer implements JsonSerializer<Date> {

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
