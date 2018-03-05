package com.comp.lexi.skydot.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true);
    private static final JsonFactory factory = objectMapper.getFactory();

    public static String stringify(Object object) throws Exception {
        if (object == null) {
            return null;
        }
        StringWriter sw = new StringWriter();
        JsonGenerator jgen = factory.createGenerator(sw);
        jgen.writeObject(object);
        jgen.close();
        return sw.toString();
    }

    public static <O> O parse(String json, Class<O> objectClass) throws JsonParseException, IOException {
        if (json == null) {
            return null;
        }
        JsonParser jp = factory.createParser(json);
        return jp.readValueAs(objectClass);
    }

    public static String makeJson(String key, Object value) throws Exception {
        HashMap<String, Object> message = new HashMap<>();
        message.put(key, value);
        return stringify(message);
    }

}