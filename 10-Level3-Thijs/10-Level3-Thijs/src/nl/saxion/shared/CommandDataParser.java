package nl.saxion.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandDataParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Map<String, Object> parseCommandData(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
            return null;
        }
    }

    public static String formatCommandData(Map<String, Object> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
            return null;
        }
    }
}