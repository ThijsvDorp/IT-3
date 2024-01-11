package nl.saxion.client.session;

import nl.saxion.client.core.ClientMediator;
import nl.saxion.shared.commands.client.LoginRequest;
import nl.saxion.shared.responses.ErrorMessage;
import nl.saxion.shared.responses.Response;

import java.util.Timer;
import java.util.TimerTask;

public class SessionManager {
    private static final long LOGIN_TIMEOUT = 10000; // 10 seconden in milliseconden
    private UserSession userSession;
    private Timer loginTimer;
    ClientMediator mediator;

    public SessionManager() {
        this.userSession = new UserSession();
    }

    public void initiateLogin() {
        new Thread(() -> {
            String username = mediator.getInput("Voer username in: ");
            userSession.setUsername(username);
            sendLoginRequest(username);
        }).start();
    }

    private void sendLoginRequest(String username) {
        LoginRequest loginRequest = new LoginRequest(username);
        mediator.sendToServer(loginRequest.toJson());
        userSession.setAwaitingLoginResponse(true);
        startLoginTimer();
    }

    private void startLoginTimer() {
        if (loginTimer != null) {
            loginTimer.cancel();
        }
        loginTimer = new Timer();
        loginTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (userSession.isAwaitingLogin()) {
                    userSession.setUsername(null);
                    userSession.setAwaitingLoginResponse(false);
                    mediator.display("Login timeout. Probeer opnieuw.");
                    initiateLogin();
                }
            }
        }, LOGIN_TIMEOUT);
    }

    public void handleLoginResponse(Response response) {
        if (loginTimer != null) {
            loginTimer.cancel();
        }
        userSession.setAwaitingLoginResponse(false);

        if ("OK".equals(response.getStatus())) {
            userSession.setLoggedIn(true);
            mediator.display("[SYSTEM]: Succesvol ingelogd");
            mediator.startListeningForUserInput();
            mediator.showCommandMenu();
        } else if ("ERROR".equals(response.getStatus())) {
            mediator.display("Fout tijdens inloggen:" + response.getErrorMessage());
            userSession.setLoggedIn(false);
            initiateLogin();
        }
    }


    public void logout() {
        userSession.logout();
    }

    public void setMediator(ClientMediator mediator) {
        this.mediator = mediator;
    }
}

