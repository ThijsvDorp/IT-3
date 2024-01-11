package nl.saxion.client.userinterface;

import nl.saxion.client.userinterface.input.InputInterface;
import nl.saxion.client.userinterface.output.OutputInterface;

public class UserInterfaceManager {
    private final InputInterface inputHandler;
    private final OutputInterface outputHandler;

    public UserInterfaceManager(InputInterface inputHandler, OutputInterface outputHandler) {
        this.inputHandler = inputHandler;
        this.outputHandler = outputHandler;
    }

    public void showMenu() {
            display("Beschikbare commando's:");
            display("/broadcast - Stuur een broadcast bericht.");
            display("/logout - Log uit.");
            display("/help - toon dit menu");
    }
    public void display(String message) {
        outputHandler.display(message);
    }

    public String getUserInput() {
        return inputHandler.getInput();
    }
    public String getUserInput(String prompt) {
        return inputHandler.getInput(prompt);
    }

}
