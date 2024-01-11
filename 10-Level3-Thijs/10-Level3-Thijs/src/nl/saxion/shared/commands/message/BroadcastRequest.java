package nl.saxion.shared.commands.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.Response;


public record BroadcastRequest(@JsonProperty("message") String message) {
    public static final String REQUEST_HEADER = "BROADCAST_REQ";
    public static final String RESPONSE_HEADER = "BROADCAST_RESP";

    @JsonCreator
    public BroadcastRequest {
    }

    public static BroadcastRequest parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, BroadcastRequest.class);
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static String createResponse(Response response) {
        return Response.formatCommand(RESPONSE_HEADER, response);
    }

    @Override
    public String message() {
        return message;
    }
}
