package com.prometheussoftware.auikit.networking;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.prometheussoftware.auikit.utility.DateUtility;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

public class DateFormatter {

    public static class DateJsonDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

            try {
                return Date.from(Instant.parse(json.getAsString()));
            } catch (DateTimeParseException e) {
                e.printStackTrace();
            }

            for (DateUtility.FORMAT format : DateUtility.FORMAT.values()) {
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
