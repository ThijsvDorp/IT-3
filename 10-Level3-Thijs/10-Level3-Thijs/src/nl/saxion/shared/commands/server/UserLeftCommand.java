package nl.saxion.shared.commands.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.commands.client.WelcomeCommand;

public class UserLeftCommand {
    public static final String HEADER = "LEFT";

    @JsonProperty("username")
    private final String username;

    @JsonCreator
    public UserLeftCommand(@JsonProperty("username") String username) {
        this.username = username;
    }

    public static UserLeftCommand create(String username) {
        return new UserLeftCommand(username);
    }

    public String toJson() {
        return HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static UserLeftCommand parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, UserLeftCommand.class);
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public String display(){
        return username + " left the server";
    }
}
