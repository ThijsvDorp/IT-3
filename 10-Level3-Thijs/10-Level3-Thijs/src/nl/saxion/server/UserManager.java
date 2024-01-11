package nl.saxion.server;

import nl.saxion.shared.commands.client.LoginRequest;
import nl.saxion.shared.commands.client.WelcomeCommand;
import nl.saxion.shared.commands.server.UserJoinedCommand;
import nl.saxion.shared.commands.server.UserLeftCommand;
import nl.saxion.shared.responses.Response;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {
    private final List<ClientConnectionHandler> unregisteredUsers;
    private final Map<String, ClientConnectionHandler> registeredUsers;

    public UserManager() {
        this.registeredUsers = new ConcurrentHashMap<>();
        this.unregisteredUsers = new CopyOnWriteArrayList<>();
    }

    public void addUnregisteredUser(ClientConnectionHandler client) {
        unregisteredUsers.add(client);
    }

    public void loginUser(String username, ClientConnectionHandler client){
        if (!LoginRequest.validUsername(username)){
            String response = LoginRequest.createResponse(Response.error(5001));
            client.sendMessage(response);
            return;
        }
        if (registeredUsers.containsKey(username)){
            String response = LoginRequest.createResponse(Response.error(5002));
            client.sendMessage(response);
            return;
        }
        if (registeredUsers.containsValue(client)){
            String response = LoginRequest.createResponse(Response.error(5003));
            client.sendMessage(response);
            return;
        }
        if (!unregisteredUsers.contains(client)){
            String response = LoginRequest.createResponse(Response.error(5004));
            client.sendMessage(response);
            return;
        }
        if (true){
            //TODO: Add server full as response?
        }
        unregisteredUsers.remove(client);
        client.setUsername(username);
        registeredUsers.put(client.getUsername(), client);
        client.setLoggedIn(true);

        Logger.getGlobal().log(Level.INFO, "Server: " + client.getUsername() + " moved to registered users" );
        client.sendMessage(LoginRequest.createResponse(Response.ok()));
        sendAllExcept(username, UserJoinedCommand.create(username).toJson());
    }

    public void sendAll(String message) {
        for (ClientConnectionHandler user : registeredUsers.values()) {
            user.sendMessage(message);
        }
    }

    public void sendAllExcept(String excludedUser, String message) {
        Logger.getGlobal().log(Level.INFO, "--> OTHERS: " + message);
        for (Map.Entry<String, ClientConnectionHandler> entry : registeredUsers.entrySet()) {
            if (!entry.getKey().equals(excludedUser)) {
                entry.getValue().sendMessage(message, false);
            }
        }
    }

    public void removeClient(ClientConnectionHandler client, int reasonCode) {
        if (unregisteredUsers.contains(client)) {
            unregisteredUsers.remove(client);
        }else if (registeredUsers.containsKey(client.getUsername())){
            registeredUsers.remove(client.getUsername());
            sendAllExcept(client.getUsername(), UserLeftCommand.create(client.getUsername()).toJson());
        }
        client.disconnect(reasonCode);
    }

    public void closeAllConnections(){
        for (ClientConnectionHandler c : unregisteredUsers) {
            removeClient(c, 0);
        }
        for (Map.Entry<String, ClientConnectionHandler> entry : registeredUsers.entrySet()) {
            removeClient(entry.getValue(), 0);
        }
    }

    public int getSize() {
        return unregisteredUsers.size() + registeredUsers.size();
    }
}
