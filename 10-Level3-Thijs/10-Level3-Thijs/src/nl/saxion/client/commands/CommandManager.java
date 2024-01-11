package nl.saxion.client.commands;

import nl.saxion.client.core.ClientMediator;
import nl.saxion.client.network.ServerMessageHandler;
import nl.saxion.shared.commands.client.LeaveRequest;
import nl.saxion.shared.commands.client.UserListRequest;
import nl.saxion.shared.commands.message.BroadcastRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandManager {
    private ClientMediator mediator;
    private final Map<String, MessageHandler> messageHandler;

    public CommandManager() {
        this.messageHandler = new HashMap<>();
        initializeCommands();
    }

    private void initializeCommands() {
        messageHandler.put("/broadcast", this::processBroadcastCommand);
        messageHandler.put("/help", this::processHelpMenu);
        messageHandler.put("/exit", this::processLeaveCommand);
        messageHandler.put("/users", this::requestUserList);

        //TODO: implement commands below
        messageHandler.put("/msg", this::processHelpMenu);
        messageHandler.put("/filetransfer", this::processHelpMenu);

        //game commands
        messageHandler.put("/guess", this::processGameCommand);
        messageHandler.put("/start", this::processGameCommand);
        messageHandler.put("/join",  this::processGameCommand);

    }

    private void processGameCommand(String arguments) {

    }

    private void requestUserList(String s) {
        mediator.sendToServer(UserListRequest.REQUEST_HEADER);
    }

    private void processLeaveCommand(String arguments) {
        mediator.sendToServer(LeaveRequest.REQUEST_HEADER);
    }

    private void processHelpMenu(String arguments) {
        mediator.showCommandMenu();
    }

    public void processUserInput(String userInput) {
        String[] parts = userInput.split(" ", 2);
        String command = parts[0].toLowerCase();
        String arguments = parts.length > 1 ? parts[1] : "";

        if (messageHandler.containsKey(command)) {
            MessageHandler handler = messageHandler.get(command);
            handler.handle(arguments);
        } else {
            mediator.display("Unknown command: " + command);
        }

    }

    private void processBroadcastCommand(String message) {
        if (!message.isEmpty()) {
            BroadcastRequest request = new BroadcastRequest(message);
            mediator.sendToServer(request.toJson());
        }
        else {
            Logger.getGlobal().log(Level.INFO, "No broadcast sent: no message given" );
        }
    }
    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
    }

    @FunctionalInterface
    protected interface MessageHandler {
        void handle(String payload);
    }
}

