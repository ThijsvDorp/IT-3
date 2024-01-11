package nl.saxion.shared.commands.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.Response;

public class LoginRequest {
    public static final String REQUEST_HEADER = "LOGIN";
    public static final String RESPONSE_HEADER = "LOGIN_RESP";

    @JsonProperty("username")
    private final String username;

    @JsonCreator
    public LoginRequest(@JsonProperty("username") String username) {
        this.username = username;
    }

    public static LoginRequest parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, LoginRequest.class);
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static String createResponse(Response response) {
        return Response.formatCommand(RESPONSE_HEADER, response);
    }

    public String getUsername() {
        return username;
    }

    public static boolean validUsername(String username) {
        //TODO: Add username validation
        return true;
    }

}
