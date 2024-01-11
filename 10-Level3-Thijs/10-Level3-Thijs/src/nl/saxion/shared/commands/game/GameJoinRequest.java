package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.Response;

public class GameJoinRequest {
    public static final String REQUEST_HEADER = "GAME_JOIN_REQ";
    public static final String RESPONSE_HEADER = "GAME_JOIN_RESP";


    @JsonCreator
    public GameJoinRequest() {
    }

    public static GameJoinRequest parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, GameJoinRequest.class);
    }

    public String toJson() {
        return REQUEST_HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static String createResponse(Response response) {
        return Response.formatCommand(RESPONSE_HEADER, response);
    }

}

