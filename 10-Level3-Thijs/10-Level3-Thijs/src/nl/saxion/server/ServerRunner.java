package nl.saxion.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerRunner {
    public static void main(String[] args) {
        try {
            ServerConfig config = new ServerConfig();
            Server server = new Server(config);
            runServerConsole(server);
        } catch (IOException e) {
            System.err.println("Fout bij het laden van de serverconfiguratie: " + e.getMessage());
            // Log de volledige stack trace voor debugging
            e.printStackTrace();
        }
    }

    private static void runServerConsole(Server server) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("Type 'start' to start the server.");
            String line;
            boolean running = false;
            while (!(line = reader.readLine()).equalsIgnoreCase("exit")) {
                switch (line.toLowerCase()) {
                    case "start":
                        if (running){
                            System.out.println("Server already running");
                            break;
                        }
                        server.start();
                        running = true;
                        break;
                    case "stop":
                        server.stop();
                        running = false;
                        System.out.println("Server closed, type 'exit' to close window...");
                        break;
                    default:
                        System.out.println("unknown command");
                }
            }
        } catch (IOException e) {
            System.err.println("Fout bij het lezen van console input: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
