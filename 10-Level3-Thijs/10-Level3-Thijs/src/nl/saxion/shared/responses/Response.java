package nl.saxion.shared.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.saxion.shared.CommandDataParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    @JsonProperty("status")
    private String status;
    @JsonProperty("code")
    private int code;

    public Response(@JsonProperty("status") String status, @JsonProperty("code") int code) {
        this.status = status;
        this.code = code;
    }

    public static String format(Response response) {
        Map<String, Object> payloadMap = new HashMap<>();
        if (response.status.equals("OK")) {
            payloadMap.put("status", response.status);
            return CommandDataParser.formatCommandData(payloadMap);
        } else {
            payloadMap.put("status", response.status);
            payloadMap.put("code", response.code);
            return CommandDataParser.formatCommandData(payloadMap);
        }
    }

    public static Response parse(String payload) {
        Map<String, Object> payloadMap = CommandDataParser.parseCommandData(payload);
        if (payloadMap.get("status").equals("OK")) {
            return ok();
        } else {
            int code = (int) payloadMap.get("code");
            return error(code);
        }
    }

    public static Response ok() {
        return new Response("OK", 0);
    }

    public static Response error(int errorCode) {
        return new Response("ERROR", errorCode);
    }

    public static String formatCommand(String responseHeader, Response response) {
        return responseHeader + " " + format(response);
    }


    public String getErrorMessage() {
        return this.code == 0 ? "Error " + this.getCode() + ": " + ErrorMessage.get(this.getCode()) : "Geen foutmelding";
    }

    private int getCode() {
        return this.code;
    }
    public boolean isOk(){
        return this.status.equals("OK");
    }

    public String getStatus() {
        return this.status;
    }
}
