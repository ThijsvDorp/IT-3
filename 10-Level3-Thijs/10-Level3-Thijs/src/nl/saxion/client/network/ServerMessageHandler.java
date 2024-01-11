package nl.saxion.client.network;

import nl.saxion.client.core.ClientMediator;
import nl.saxion.shared.commands.client.*;
import nl.saxion.shared.commands.client.WelcomeCommand;
import nl.saxion.shared.commands.game.*;
import nl.saxion.shared.commands.message.BroadcastCommand;
import nl.saxion.shared.commands.message.BroadcastRequest;
import nl.saxion.shared.commands.message.PrivateMessageRequest;
import nl.saxion.shared.commands.server.*;
import nl.saxion.shared.responses.Response;

import java.util.HashMap;
import java.util.Map;


public class ServerMessageHandler {
    private ClientMediator mediator;
    private final Map<String, MessageHandler> messageHandler;

    public ServerMessageHandler() {
        this.mediator = null;
        this.messageHandler = new HashMap<>();
        initializeMessageProcessors();
    }

    public void handleMessage(String messageJson) {
        String[] parts = messageJson.split(" ", 2);
        String header = parts[0];
        String payload = parts.length > 1 ? parts[1] : "{}";

        if (messageHandler.containsKey(header)) {
            MessageHandler handler = messageHandler.get(header);
            handler.handle(payload);
        } else {
            mediator.display("Unknown message type: " + header);
        }
    }

    private void initializeMessageProcessors() {
        // Add processors for each type of command/response
        messageHandler.put(WelcomeCommand.HEADER, this::processWelcomeCommand);
        messageHandler.put(LoginRequest.RESPONSE_HEADER, this::handleLoginResponse);
        messageHandler.put(LeaveRequest.RESPONSE_HEADER, this::handleLeaveResponse);
        messageHandler.put(DisconnectCommand.HEADER, this::handleDisconnect);
        messageHandler.put(HeartBeatCommand.getPingCommand(), this::handlePing);
        messageHandler.put(BroadcastCommand.HEADER, this::handleBroadcastCommand);
        messageHandler.put(GameInviteCommand.HEADER, this::handleGameInvite);
        messageHandler.put(GameResultCommand.HEADER, this::handleGameResults);
        messageHandler.put(UserJoinedCommand.HEADER, this::handleJoinCommand);
        messageHandler.put(UserLeftCommand.HEADER, this::handleLeftCommand);

        //move to top if implemented
        messageHandler.put(BroadcastRequest.RESPONSE_HEADER, this::handleResponse);
        messageHandler.put(GameJoinRequest.RESPONSE_HEADER, this::handleCommand);
        messageHandler.put(GameGuessRequest.RESPONSE_HEADER, this::handleCommand);
        messageHandler.put(GameGuessFeedback.HEADER, this::handleCommand);

        messageHandler.put(AccessDeniedCommand.HEADER, this::handleCommand);
        messageHandler.put(CapacityFullCommand.HEADER, this::handleCommand);
        messageHandler.put(ServerShutdownCommand.HEADER, this::handleCommand);

        messageHandler.put(PrivateMessageRequest.RESPONSE_HEADER, this::handleCommand);
        messageHandler.put(FileTransferRequest.RESPONSE_HEADER, this::handleCommand);
        // Other handlers...
    }

    private void handleJoinCommand(String payload) {
        mediator.display(UserJoinedCommand.parse(payload).display());
    }
    private void handleLeftCommand(String payload) {
        mediator.display(UserLeftCommand.parse(payload).display());
    }

    private void handleResponse(String s) {

    }

    private void handleGameInvite(String payload) {
        GameInviteCommand gameInvite = GameInviteCommand.parse(payload);
        mediator.display(gameInvite.display());
    }

    private void handleGameResults(String jsonString) {
        GameResultCommand gameresults = GameResultCommand.parse(jsonString);
        mediator.display(gameresults.display());
    }

    private void handleCommand(String payload) {
        //placeholder Method for unimplemented messages
        mediator.display("Oeps, deze moet je nog toevoegen");
    }

    private void handleLoginResponse(String payload) {
        mediator.handleLoginResponse(Response.parse(payload));
    }

    private void handleLeaveResponse(String payload) {
        Response leaveResponse = Response.parse(payload);
        if (!leaveResponse.isOk()){
            mediator.display("Logged out succesfully");
            mediator.logout();
        }else {
            mediator.display("Error logging out: " + leaveResponse.getErrorMessage());
        }
    }

    private void handleDisconnect(String payload) {
        DisconnectCommand command = DisconnectCommand.parse(payload);
        mediator.display("Disconnected: " + command.getReasonMessage());
    }

    private void handlePing(String payload) {
        mediator.sendToServer(HeartBeatCommand.getPongCommand());
    }

    private void handleBroadcastCommand(String payload) {
        BroadcastCommand broadcast = BroadcastCommand.parse(payload);
        if (!broadcast.getUsername().isEmpty() && !broadcast.getMessage().isEmpty()){
            mediator.display(broadcast.display());
        }
    }

    private void processWelcomeCommand(String jsonBody) {
        mediator.display(WelcomeCommand.parse(jsonBody).getMessage());
        mediator.initiateLogin();
    }


    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
    }

    @FunctionalInterface
    protected interface MessageHandler {
        void handle(String payload);
    }

}
