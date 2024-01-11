package nl.saxion.shared.commands.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.commands.client.LoginRequest;

public class UserJoinedCommand {
    public static final String HEADER = "JOINED";

    @JsonProperty("username")
    private final String username;

    @JsonCreator
    public UserJoinedCommand(@JsonProperty("username") String username) {
        this.username = username;
    }

    public static UserJoinedCommand create(String username) {
        return new UserJoinedCommand(username);
    }

    public String toJson() {
        return HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static UserJoinedCommand parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, UserJoinedCommand.class);
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public String display(){
        return username + " joined the server";
    }
}
