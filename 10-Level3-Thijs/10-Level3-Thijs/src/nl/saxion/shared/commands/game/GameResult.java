package nl.saxion.shared.commands.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameResult {
    @JsonProperty("username")
    private final String username;
    @JsonProperty("time")
    private final int time;
    @JsonProperty("status")
    private final String status;

    public GameResult(
            @JsonProperty("username")   String username,
            @JsonProperty("time")       int time,
            @JsonProperty("status")     String status) {
        this.username = username;
        this.time = time;
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public int getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("%s - Time: %d ms, Status: %s", username, time, status);
    }
}
