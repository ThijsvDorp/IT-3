package nl.saxion.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonConverter {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void setMapper(ObjectMapper mapper) {
        objectMapper = mapper;
    }

    public static <T> String objectToJson(T object) {
        try {
            if (objectMapper == null) {
                throw new IllegalStateException("ObjectMapper is not set. Call setMapper() first.");
            }
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            Logger.getGlobal().log(Level.WARNING, "Fout bij het converteren van object naar JSON: " + e.getMessage());
            return null;
        }
    }

    public static <T> T jsonToObject(String json, Class<T> valueType) {
        try {
            if (objectMapper == null) {
                throw new IllegalStateException("ObjectMapper is not set. Call setMapper() first.");
            }
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            Logger.getGlobal().log(Level.WARNING, "Fout bij het converteren van JSON naar object: " + e.getMessage());
            return null;
        }
    }
}
