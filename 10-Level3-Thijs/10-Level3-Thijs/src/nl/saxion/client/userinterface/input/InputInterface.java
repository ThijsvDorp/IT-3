package nl.saxion.client.userinterface.input;

public interface InputInterface {
    String getInput();
    String getInput(String prompt);

    void showMenu();
}

