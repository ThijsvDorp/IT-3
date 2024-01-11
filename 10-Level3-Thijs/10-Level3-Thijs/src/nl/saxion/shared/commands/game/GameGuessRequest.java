package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.Response;

public class GameGuessRequest {
    public static final String REQUEST_HEADER = "GAME_GUESS_REQ";
    public static final String RESPONSE_HEADER = "GAME_GUESS_RESP";

    @JsonProperty("number")
    private final int number;

    @JsonCreator
    public GameGuessRequest(@JsonProperty("number") int number) {
        this.number = number;
    }

    public static GameGuessRequest parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, GameGuessRequest.class);
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static String createResponse(Response response) {
        return Response.formatCommand(RESPONSE_HEADER, response);
    }

    public int getNumber() {
        return number;
    }
}
