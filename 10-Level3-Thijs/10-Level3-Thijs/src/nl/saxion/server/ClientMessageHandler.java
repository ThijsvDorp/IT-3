package nl.saxion.server;

import nl.saxion.shared.commands.client.FileTransferRequest;
import nl.saxion.shared.commands.client.HeartBeatCommand;
import nl.saxion.shared.commands.client.LeaveRequest;
import nl.saxion.shared.commands.client.LoginRequest;
import nl.saxion.shared.commands.game.GameCreateRequest;
import nl.saxion.shared.commands.game.GameGuessRequest;
import nl.saxion.shared.commands.game.GameJoinRequest;
import nl.saxion.shared.commands.message.BroadcastCommand;
import nl.saxion.shared.commands.message.BroadcastRequest;
import nl.saxion.shared.commands.message.PrivateMessageRequest;
import nl.saxion.shared.commands.server.AccessDeniedCommand;
import nl.saxion.shared.commands.server.DisconnectCommand;
import nl.saxion.shared.responses.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientMessageHandler {

    private final UserManager userManager;
    private final GameManager gameManager;
    private final Map<String, CommandHandler> commandHandler;

    public ClientMessageHandler(UserManager userManager, GameManager gameManager) {
        this.userManager = userManager;
        this.gameManager = gameManager;
        this.commandHandler = new HashMap<>();

        initializeCommandHandlers();
    }

    private void initializeCommandHandlers() {
        commandHandler.put(LoginRequest.REQUEST_HEADER, this::handleLogin);
        commandHandler.put(LeaveRequest.REQUEST_HEADER, this::handleLeaveRequest);

        commandHandler.put(PrivateMessageRequest.REQUEST_HEADER, this::handlePrivateMessage);
        commandHandler.put(BroadcastRequest.REQUEST_HEADER, this::handleBroadcast);

        commandHandler.put(GameCreateRequest.REQUEST_HEADER, this::handleGameStart);
        commandHandler.put(GameJoinRequest.REQUEST_HEADER, this::handleGameJoin);
        commandHandler.put(GameGuessRequest.REQUEST_HEADER, this::handleGameGuess);

        commandHandler.put(FileTransferRequest.REQUEST_HEADER, this::handleFileTransfer);

        //commandHandler.put(, this::);
    }

    public void handleMessage(String message, ClientConnectionHandler client) {
        String[] parts = message.split(" ", 2);
        String command = parts[0];
        String payload = parts.length > 1 ? parts[1] : "";

        if (command.equals(HeartBeatCommand.getPongCommand())){
            client.confirmPong();
            return;
        }

        if (!client.isLoggedIn() && !command.equals(LoginRequest.REQUEST_HEADER)) {
            Logger.getGlobal().log(Level.WARNING, "Unauthorized attempt: User '" + client.getUsername() + "' tried to execute command '" + command + "' without being logged in.");

            //TODO: Add proper error/reason code in protocol for denying access.
            client.sendMessage(AccessDeniedCommand.create(1337).toJson());
        }else {
            if (commandHandler.containsKey(command)) {
                CommandHandler handler = commandHandler.get(command);
                handler.handle(payload, client);
            } else {
                Logger.getGlobal().log(Level.WARNING, "Unauthorized attempt: User '" + client.getUsername() + "' tried to execute command '" + command + "' without being logged in.");
                //TODO: inform client of unidentified command
            }
        }
    }

    private void handleBroadcast(String payload, ClientConnectionHandler client) {
        BroadcastRequest request = BroadcastRequest.parse(payload);
        String broadcastMessage = request.message();
        if (!broadcastMessage.isBlank()){
            client.sendMessage(BroadcastRequest.createResponse(Response.ok()));
            BroadcastCommand broadcast = new BroadcastCommand(client.getUsername(), broadcastMessage);
            userManager.sendAllExcept(client.getUsername(), broadcast.toJson());
        }
    }

    private void handleLogin(String payload, ClientConnectionHandler client) {
        LoginRequest request = LoginRequest.parse(payload);
        String username = request.getUsername().trim();
        userManager.loginUser(username, client);
    }

    private void handleLeaveRequest(String payload, ClientConnectionHandler client) {
        client.sendMessage(LeaveRequest.createResponse(Response.ok()));

        userManager.removeClient(client, 7003);

    }

    public void handleGameStart(String payload, ClientConnectionHandler client){
        gameManager.startGame(client);
    }

    public void handleGameJoin(String payload, ClientConnectionHandler client){
        gameManager.addPlayer(client);
    }

    public void handleGameGuess(String payload, ClientConnectionHandler client){
        GameGuessRequest guessRequest = GameGuessRequest.parse(payload);
        int guess = guessRequest.getNumber();
        gameManager.handleGuess(guess, client);
    }

    public void handlePrivateMessage(String payload, ClientConnectionHandler client){
        //TODO: Add PrivateMessaging with encryption
    }

    public void handleFileTransfer(String payload, ClientConnectionHandler client){
        //TODO: Add file transfer protocol
    }
}
