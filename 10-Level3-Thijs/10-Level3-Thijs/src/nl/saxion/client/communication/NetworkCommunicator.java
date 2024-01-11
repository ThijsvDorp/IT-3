package nl.saxion.client.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkCommunicator {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private final String serverAddress;
    private final int serverPort;
    private static final int RECONNECT_ATTEMPTS = 5;
    private static final int RECONNECT_INTERVAL_MS = 5000; // 5 seconden

    public NetworkCommunicator(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        connectToServer();
    }

    private void connectToServer() {
        for (int attempt = 1; attempt <= RECONNECT_ATTEMPTS; attempt++) {
            try {
                socket = new Socket(serverAddress, serverPort);
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Logger.getGlobal().log(Level.INFO, "Connected to server on port " + socket.getLocalPort());
                return; // Succesvol verbonden, dus break uit de loop
            } catch (IOException e) {
                Logger.getGlobal().log(Level.WARNING, "Connection attempt " + attempt + " failed, retrying...");
                try {
                    Thread.sleep(RECONNECT_INTERVAL_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
        Logger.getGlobal().log(Level.SEVERE, "Unable to connect to server after " + RECONNECT_ATTEMPTS + " attempts.");
        try {
            close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendRequest(String request) {
            out.println(request);
            Logger.getGlobal().log(Level.INFO, "--> SERVER: " + request);
    }

    public String readResponse() throws IOException {
        return in.readLine(); // Of gebruik een andere manier om te bepalen hoe je wilt lezen
    }

    public void close() throws IOException {
        in.close();
        out.close();
        socket.close();
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed();
    }
    // Mogelijk meer methoden voor specifieke soorten requests
}
