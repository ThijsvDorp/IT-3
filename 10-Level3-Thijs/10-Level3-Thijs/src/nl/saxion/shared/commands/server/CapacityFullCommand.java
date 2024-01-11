package nl.saxion.shared.commands.server;

public class CapacityFullCommand {
    public static final String HEADER = "SERVER_CAPACITY_FULL";
    private static final String MESSAGE = "Capacity has been reached, please try reconnecting at a different time";

    public static CapacityFullCommand create() {
        return new CapacityFullCommand();
    }

    public String toJson() {
        return HEADER;
    }

    public static String getMessage() {
        return MESSAGE;
    }
}
