package nl.saxion.client.core;

import nl.saxion.client.commands.CommandManager;
import nl.saxion.client.communication.NetworkCommunicator;
import nl.saxion.client.network.ServerMessageHandler;
import nl.saxion.client.session.SessionManager;
import nl.saxion.client.userinterface.UserInterfaceManager;
import nl.saxion.client.userinterface.input.InputInterface;
import nl.saxion.client.userinterface.output.OutputInterface;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient {
    private ClientMediator mediator;
    private final NetworkCommunicator communicator;
    private final SessionManager sessionManager;
    private final CommandManager commandManager;
    private final ServerMessageHandler serverMessageHandler;
    private final UserInterfaceManager uiManager;

    public ChatClient(String serverAddress, int serverPort, InputInterface inputHandler, OutputInterface outputHandler) {
        this.communicator = new NetworkCommunicator(serverAddress, serverPort);
        this.sessionManager = new SessionManager();
        this.serverMessageHandler = new ServerMessageHandler();
        this.uiManager = new UserInterfaceManager(inputHandler, outputHandler);
        this.commandManager = new CommandManager();

        // Eerst alle componenten initialiseren
        this.mediator = new ClientMediator(uiManager, serverMessageHandler, communicator, commandManager, sessionManager);

        this.sessionManager.setMediator(mediator);
        this.serverMessageHandler.setMediator(mediator);
        this.commandManager.setMediator(mediator);

        startListeningForServerMessages();
        Logger.getGlobal().log(Level.INFO, "Chat client connected to " + serverAddress + " on port " + serverPort);
    }


    private void startListeningForServerMessages() {
        new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    String message = communicator.readResponse();
                    if (!(message == null)){
                        if (!message.isEmpty()) {
                            Logger.getGlobal().log(Level.INFO, "<-- SERVER: " + message);
                            serverMessageHandler.handleMessage(message);
                        }
                    }
                }
            } catch (IOException e) {
                Logger.getGlobal().log(Level.WARNING, "Stopped listening to server messages: " + e.getMessage());
                disconnect();
            }
        }).start();
    }


    public void disconnect() {
        try {
            communicator.close();
            sessionManager.logout();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
