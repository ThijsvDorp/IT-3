package nl.saxion.server;

import nl.saxion.shared.commands.server.CapacityFullCommand;
import nl.saxion.shared.commands.server.ServerShutdownCommand;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    private ServerSocket serverSocket;
    private UserManager userManager;
    private GameManager gameManager;
    private ClientMessageHandler messageHandler;

    final private int capacity;
    private final int port;
    private boolean running;

    public Server(ServerConfig config) {
        this.port = config.getPort();
        this.capacity = config.getCapacity();
        System.out.println("Server settings loaded with capacity of " +this.capacity + " on port " + config.getPort());
    }

    public void start() {
        this.userManager = new UserManager();
        this.gameManager = new GameManager(userManager);
        this.messageHandler = new ClientMessageHandler(userManager, gameManager);
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Server accepting clients on port: " + serverSocket.getLocalSocketAddress().toString());
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(e);
        }
        new Thread(()-> {
            running = true;
            System.out.println("Server is running...");
            System.out.println("Type 'stop' to close the server.");
            while (running) {
                try {
                    Socket newSocket = serverSocket.accept();
                    Logger.getGlobal().log(Level.INFO, "New connection " + newSocket.getLocalPort() + " and client port number " + newSocket.getPort());
                    ClientConnectionHandler clientHandler = new ClientConnectionHandler(newSocket, messageHandler, userManager);
                    if (userManager.getSize() >= capacity) {
                        Logger.getGlobal().log(Level.INFO, "Capacity reached, no new users will be added");
                        CapacityFullCommand capacityCommand = new CapacityFullCommand();
                        clientHandler.sendMessage(capacityCommand.toJson());
                        newSocket.close();
                    }
                    userManager.addUnregisteredUser(clientHandler);
                    Logger.getGlobal().log(Level.INFO, userManager.getSize()+ "/" + capacity + " New client added with port " + newSocket.getLocalPort() + " and client port number " + newSocket.getPort());

                } catch (IOException e) {
                    Logger.getGlobal().log(Level.WARNING, e.getMessage());
                    if (!running) {
                        break; // Stop the loop if server stopped.
                    }
                }
            }
            Logger.getGlobal().log(Level.INFO, "Server stopped accepting new connections...");
        }).start();
    }

    public void stop() {
        try {
            serverSocket.close();
            userManager.sendAll(ServerShutdownCommand.create("Closing server...").toJson());
            userManager.closeAllConnections();
            running = false;
            this.userManager = null;
            this.gameManager = null;
            this.messageHandler = null;
            this.serverSocket = null;
        } catch (IOException e) {
            Logger.getGlobal().log(Level.WARNING, e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
