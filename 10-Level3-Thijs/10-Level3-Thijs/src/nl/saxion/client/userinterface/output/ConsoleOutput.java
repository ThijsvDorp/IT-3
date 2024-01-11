package nl.saxion.client.userinterface.output;

public class ConsoleOutput implements OutputInterface{
    @Override
    public void display(String message) {
        System.out.println(message);
    }

    @Override
    public void showCommands() {
        System.out.println("/broadcast - Stuur een broadcast bericht");
        System.out.println("/help - Toon dit menu opnieuw");
        // Voeg andere commando's toe...
    }
}
