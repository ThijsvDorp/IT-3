package nl.saxion.shared.commands.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

public class WelcomeCommand {
    public static final String HEADER = "WELCOME";

    @JsonProperty("msg")
    private final String message;

    @JsonCreator
    public WelcomeCommand(@JsonProperty("msg") String message) {
        this.message = message;
    }

    public static WelcomeCommand create(String message) {
        return new WelcomeCommand(message);
    }
    public static WelcomeCommand parse(String jsonString){
        return JsonConverter.jsonToObject(jsonString, WelcomeCommand.class);
    }

    public String toJson() {
        return HEADER + " " + JsonConverter.objectToJson(this);
    }

    public String getMessage() {
        return message;
    }
}
