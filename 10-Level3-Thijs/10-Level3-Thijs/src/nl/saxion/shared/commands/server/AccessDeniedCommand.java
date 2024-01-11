package nl.saxion.shared.commands.server;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

public class AccessDeniedCommand {
    public static final String HEADER = "ACCESS_DENIED";
    @JsonProperty
    private final int reasonCode;

    @JsonCreator
    public AccessDeniedCommand(@JsonProperty int reasonCode) {
        this.reasonCode = reasonCode;
    }

    public static AccessDeniedCommand create(int reasonCode){
        return new AccessDeniedCommand(reasonCode);
    }


    public String toJson() {
        return JsonConverter.objectToJson(this);
    }
}
