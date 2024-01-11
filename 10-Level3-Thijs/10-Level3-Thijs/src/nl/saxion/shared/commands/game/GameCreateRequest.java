package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.Response;

public class GameCreateRequest {
    public static final String REQUEST_HEADER = "GAME_START_REQ";
    public static final String RESPONSE_HEADER = "GAME_START_RESP";


    @JsonCreator
    public GameCreateRequest() {
    }

    public static GameCreateRequest parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, GameCreateRequest.class);
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static String createResponse(Response response) {
        return Response.formatCommand(RESPONSE_HEADER, response);
    }

}

