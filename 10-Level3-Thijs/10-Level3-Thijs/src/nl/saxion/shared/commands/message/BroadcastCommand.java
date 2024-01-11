package nl.saxion.shared.commands.message;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

public class BroadcastCommand{
    public static final String HEADER = "BROADCAST";
    public static final String DESCRIPTION = "Sends a message to all users in the server";

    @JsonProperty("username")
    private String username;
    @JsonProperty("message")
    private final String message;

    @JsonCreator
    public BroadcastCommand(@JsonProperty("username") String username, @JsonProperty("message") String message) {
        this.username = username;
        this.message = message;
    }

    public static BroadcastCommand parse(String jsonString){
        return JsonConverter.jsonToObject(jsonString, BroadcastCommand.class);
    }

    public String toJson(){
        return HEADER + " " + JsonConverter.objectToJson(this);
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String display() {
        return "[" + username + "]: " + message;
    }
}
