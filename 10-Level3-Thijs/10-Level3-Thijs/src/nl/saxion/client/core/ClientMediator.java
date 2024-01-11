package nl.saxion.client.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import nl.saxion.client.commands.CommandManager;
import nl.saxion.client.communication.NetworkCommunicator;
import nl.saxion.client.network.ServerMessageHandler;
import nl.saxion.client.session.SessionManager;
import nl.saxion.client.userinterface.UserInterfaceManager;
import nl.saxion.shared.responses.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMediator{
    private UserInterfaceManager uiManager;
    private ServerMessageHandler messageHandler;
    private NetworkCommunicator communicator;
    private CommandManager commandManager;
    private SessionManager sessionManager;

    public ClientMediator(UserInterfaceManager uiManager,
                          ServerMessageHandler messageHandler,
                          NetworkCommunicator communicator,
                          CommandManager commandManager,
                          SessionManager sessionManager) {
        this.uiManager = uiManager;
        this.messageHandler = messageHandler;
        this.communicator = communicator;
        this.commandManager = commandManager;
        this.sessionManager = sessionManager;
    }

    public String getInput(String prompt) {
        return uiManager.getUserInput(prompt);
    }

    public void display(String message) {
        uiManager.display(message);
    }

    public void sendToServer(String message) {

        communicator.sendRequest(message);
    }

    public void showCommandMenu(){
        uiManager.showMenu();
    }
    public void processUserInput(String userInput) {
        commandManager.processUserInput(userInput);
    }

    public void processServerMessage(String message) {

            Logger.getGlobal().log(Level.INFO, "<-- SERVER: " + message);
            messageHandler.handleMessage(message);
    }

    public void startListeningForUserInput(){
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String userInput = uiManager.getUserInput();
                System.out.println("verwerk input: " + userInput);
                // Verwerk de gebruikersinput
                processUserInput(userInput);
            }
        }).start();
    }

    public void initiateLogin(){
        sessionManager.initiateLogin();
    }

    public void handleLoginResponse(Response response) {
        sessionManager.handleLoginResponse(response);
    }

    public void logout() {
    }
}
