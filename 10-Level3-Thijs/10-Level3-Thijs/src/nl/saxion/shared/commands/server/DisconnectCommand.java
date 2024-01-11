package nl.saxion.shared.commands.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;
import nl.saxion.shared.responses.ErrorMessage;

public record DisconnectCommand(@JsonProperty("reason") int reasonCode) {
    public static final String HEADER = "DSCN";


    @JsonCreator
    public DisconnectCommand {
    }

    public String toJson() {
        return HEADER + " " + JsonConverter.objectToJson(this);
    }

    public static DisconnectCommand parse(String jsonString) {
        return JsonConverter.jsonToObject(jsonString, DisconnectCommand.class);
    }

    public static DisconnectCommand create(int reasonCode){
        return new DisconnectCommand(reasonCode);
    }
    public String getReasonMessage(){
        return ErrorMessage.get(this.reasonCode);
    }
}

