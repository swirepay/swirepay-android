package com.swirepay.android_sdk.retrofit;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateDeSerializer implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
        String date = element.getAsString();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss" , Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));

        try {
            return format.parse(date);
        } catch (ParseException exp) {
            System.err.println(exp.getMessage());
            return null;
        }
    }
}