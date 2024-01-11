package nl.saxion.shared.commands.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

public record ServerShutdownCommand(@JsonProperty("message") String message) {
    public static final String HEADER = "SERVER_SHUTDOWN";

    @JsonCreator
    public ServerShutdownCommand {
    }

    public static ServerShutdownCommand create(String message) {
        return new ServerShutdownCommand(message);
    }

    public String toJson() {
        return HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static ServerShutdownCommand parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, ServerShutdownCommand.class);
    }
}
