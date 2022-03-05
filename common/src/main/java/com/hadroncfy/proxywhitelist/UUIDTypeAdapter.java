package com.hadroncfy.proxywhitelist;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDTypeAdapter implements JsonDeserializer<UUID>, JsonSerializer<UUID> {
    private static final UUIDTypeAdapter instance = new UUIDTypeAdapter();

    public static UUIDTypeAdapter getInstance() {
        return instance;
    }

    @Override
    public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        if (json.isJsonPrimitive()) {
            String s = json.getAsString();
            if (!s.contains("-")) {
                s = s.replaceFirst("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            }
            return UUID.fromString(s);
        }
        throw new JsonParseException("String expected for UUID type");
    }
}