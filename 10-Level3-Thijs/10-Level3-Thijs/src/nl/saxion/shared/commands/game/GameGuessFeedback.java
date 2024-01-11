package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

public class GameGuessFeedback {
    public static final String HEADER = "GAME_GUESS_FEEDBACK";

    @JsonProperty
    public final String feedback;

    @JsonCreator
    public GameGuessFeedback(@JsonProperty String feedback) {
        this.feedback = feedback;
    }

    private String toJson() {
        return JsonConverter.objectToJson(this);
    }

    private static GameGuessFeedback parse(String jsonString){
        return JsonConverter.jsonToObject(jsonString, GameGuessFeedback.class);
    }

    public static String tooLow() {
        return new GameGuessFeedback("LOW").toJson();
    }

    public static String tooHigh() {
        return new GameGuessFeedback("HIGH").toJson();
    }

    public static String correct() {
        return new GameGuessFeedback("OK").toJson();
    }
}
