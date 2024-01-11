package nl.saxion.server;

@FunctionalInterface
public interface CommandHandler {
    void handle(String payload, ClientConnectionHandler clientHandler);
}

