package nl.saxion.shared.commands.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.Response;

public class LeaveRequest {
    public static final String REQUEST_HEADER = "BYE";
    public static final String RESPONSE_HEADER = "BYE_RESP";

    @JsonCreator
    public LeaveRequest() {
    }

    public static LeaveRequest parse(String jsonString) {
        return new LeaveRequest();
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static String createResponse(Response response) {
        return Response.formatCommand(RESPONSE_HEADER, response);
    }
}
