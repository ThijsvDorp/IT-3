package nl.saxion.server;

import nl.saxion.shared.commands.client.WelcomeCommand;
import nl.saxion.shared.commands.server.DisconnectCommand;
import nl.saxion.shared.commands.client.HeartBeatCommand;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnectionHandler {
    private final Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final ClientMessageHandler handler;
    private final UserManager userManager;
    private String username;
    private boolean loggedIn;

    private boolean running;

    private final ScheduledExecutorService pingExecutor;
    private boolean pongReceived;


    public ClientConnectionHandler(Socket socket, ClientMessageHandler messageHandler, UserManager userManager) {
        this.socket = socket;
        this.handler = messageHandler;
        this.userManager = userManager;
        try {
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, "Error setting up streams: " + e.getMessage());
            running = false;
        }
        this.username = String.valueOf(socket.getPort());
        this.loggedIn = false;

        this.pingExecutor = Executors.newSingleThreadScheduledExecutor();
        this.pongReceived = true;

        this.sendMessage(WelcomeCommand.create("Welcome to the chad server").toJson());
        this.run();
    }

    private void run() {
        this.running = true;
        new Thread(() -> {
            while (running) {
                try {
                    String message = input.readLine();
                    if (message != null) {
                        Logger.getGlobal().log(Level.INFO, "<-- " + getUsername() + " " + message);
                        handler.handleMessage(message, this);
                    }
                } catch (IOException e) {
                    System.out.println("Error reading message: " + e.getMessage());
                    Logger.getGlobal().log(Level.WARNING, "reading messages: " + e.getMessage());
                    break;
                }
            }
            Logger.getGlobal().log(Level.WARNING, "Stopped receiving messages, closing down...");
            userManager.removeClient(this,7001);

        }).start();
        this.startPingThread();
    }

    private void startPingThread() {
        pingExecutor.scheduleAtFixedRate(() -> {
            if (pongReceived) {
                sendMessage(HeartBeatCommand.getPingCommand());
                pongReceived = false;
            } else {
                Logger.getGlobal().log(Level.WARNING, getUsername() + " Pong timeout, closing connection");
                userManager.removeClient(this, 7000);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    public void sendMessage(String message) {
        sendMessage(message, true);
    }

    public void sendMessage(String message, boolean log) {
        if (log) {
            Logger.getGlobal().log(Level.INFO, "--> " + getUsername() + " " + message);
        }
        output.println(message);
    }

    public void disconnect(int reasonCode) {
        try {
            sendMessage(DisconnectCommand.create(reasonCode).toJson());
            running = false;
            socket.close();
            input.close();
            output.close();
            pingExecutor.shutdown();
            Logger.getGlobal().log(Level.INFO, "Closed connection with " + getUsername() + ".");
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, "Failed to remove " + getUsername() + ": " + e.getMessage());
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void confirmPong() {
        pongReceived = true;
    }

    public String getUsername() {
        return username;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
