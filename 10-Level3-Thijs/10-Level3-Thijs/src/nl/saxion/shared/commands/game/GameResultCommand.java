package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.saxion.shared.JsonConverter;

import java.util.List;

public class GameResultCommand {
    public static final String HEADER = "GAME_RESULT";
    @JsonProperty("results")
    private final List<GameResult> results;

    @JsonCreator
    public GameResultCommand(@JsonProperty("results") List<GameResult> results) {
        this.results = results;
    }

    public String toJson(){
        return JsonConverter.objectToJson(results);
    }

    public static GameResultCommand parse(String jsonString){
        return JsonConverter.jsonToObject(jsonString, GameResultCommand.class);
    }

    public List<GameResult> getResults() {
        return results;
    }

    public String display(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Guessing Game Results:\n");
        stringBuilder.append("-------------------------------------------------\n");
        stringBuilder.append(String.format("%-20s %-10s %s\n", "Username", "Time (ms)", "Status")); // Header

        for (GameResult result : results) {
            stringBuilder.append(result.toString()).append("\n");
        }

        stringBuilder.append("-------------------------------------------------\n");
        return stringBuilder.toString();
    }
}
