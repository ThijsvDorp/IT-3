package nl.saxion.shared.commands.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

import java.util.Arrays;

public class UserListRequest {
    public static final String REQUEST_HEADER = "USER_LIST_REQUEST";
    public static final String RESPONSE_HEADER = "USER_LIST_RESPONSE";
    @JsonProperty("users")
    private String[] users;

    public static UserListRequest parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, UserListRequest.class);
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public String getList(){
        return Arrays.toString(users);
    }

}
