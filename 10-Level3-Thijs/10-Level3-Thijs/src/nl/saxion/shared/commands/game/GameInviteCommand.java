package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

public class GameInviteCommand {
    public static final String HEADER = "GAME_INVITE";

    @JsonProperty("creator")
    private final String creator;

    @JsonCreator
    public GameInviteCommand(@JsonProperty("creator") String creator) {
        this.creator = creator;
    }

    public static GameInviteCommand parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, GameInviteCommand.class);
    }

    public String toJson() {
        return JsonConverter.objectToJson(this);
    }

    public String getCreator() {
        return creator;
    }

    public String display(){
        return "[System]: " + creator + " has started the guessing game! You have 10 seconds to join by typing /join";
    }
}
