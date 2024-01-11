package nl.saxion.shared.responses;

import java.util.HashMap;
import java.util.Map;

public class ErrorMessage {
    private static final Map<Integer, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put(5002, "Kan niet twee keer inloggen");
        errorMessages.put(5001, "Gebruikersnaam is syntactisch ongeldig");
        errorMessages.put(5000, "Gebruikersnaam is al in gebruik door een andere client");
        errorMessages.put(6000, "Moet eerst inloggen");
        errorMessages.put(8000, "Onverwacht PONG bericht ontvangen");
        // Voeg eventueel meer foutcodes en berichten toe
    }

    public static String get(int code) {
        return errorMessages.getOrDefault(code, "Onbekende fout opgetreden");
    }
}

