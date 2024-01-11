package nl.saxion.client.userinterface.input;

import java.util.Scanner;

public class ConsoleInput implements InputInterface {
    private final Scanner scanner;

    public ConsoleInput() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    @Override
    public String getInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public void showMenu() {
        System.out.println("/broadcast - Stuur een broadcast bericht");
        System.out.println("/help - Toon dit menu opnieuw");
        // Voeg andere commando's toe...
    }
}
