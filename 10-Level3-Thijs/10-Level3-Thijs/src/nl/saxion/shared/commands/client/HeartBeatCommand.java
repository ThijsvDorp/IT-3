package nl.saxion.shared.commands.client;

public class HeartBeatCommand {

    public static String getPingCommand() {
        return "PING";
    }
    public static String getPongCommand() {
        return "PONG";
    }
}
