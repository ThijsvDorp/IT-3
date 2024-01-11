package nl.saxion.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {
    private final Properties properties = new Properties();

    /*
    public ServerConfig(String configFile) throws IOException {
        try (FileInputStream fis = new FileInputStream(configFile)) {
            properties.load(fis);
        }
    }*/

    public ServerConfig() throws IOException {
        // Laadt de properties met getResourceAsStream vanaf de root van de classpath
        try (InputStream is = getClass().getResourceAsStream("/serverconfig.properties")) {
            if (is == null) {
                throw new IOException("Configuratiebestand 'serverconfig.properties' niet gevonden in de classpath root.");
            }
            properties.load(is);
        }
    }

    public int getPort() {
        return Integer.parseInt(properties.getProperty("port", "1337"));
    }

    public int getCapacity() {
        return Integer.parseInt(properties.getProperty("capacity", "100"));
    }

    // Voeg methoden toe voor eventuele andere configuraties
}
