package nl.saxion.client.core;

import nl.saxion.client.userinterface.input.ConsoleInput;
import nl.saxion.client.userinterface.input.InputInterface;
import nl.saxion.client.userinterface.output.ConsoleOutput;
import nl.saxion.client.userinterface.output.OutputInterface;

import java.io.IOException;

public class ClientRunner {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 1337;

    public static void main(String[] args) throws IOException {
        InputInterface inputInterface = new ConsoleInput();
        OutputInterface outputInterface = new ConsoleOutput();
        ChatClient client = new ChatClient(SERVER_ADDRESS, SERVER_PORT, inputInterface, outputInterface);

    }
}
