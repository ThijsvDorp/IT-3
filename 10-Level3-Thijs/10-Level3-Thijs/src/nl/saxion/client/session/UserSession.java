package nl.saxion.client.session;

public class UserSession {
    private boolean isLoggedIn = false;
    private String username;
    private boolean awaitingLoginResponse = false;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoggedIn(boolean status) {
        this.isLoggedIn = status;
        awaitingLoginResponse = false;
    }

    public void logout() {
        this.username = null;
        this.isLoggedIn = false;
        awaitingLoginResponse = false;
    }

    public String getUsername() {
        return username;
    }

    public void setAwaitingLoginResponse(boolean status) {
        awaitingLoginResponse = status;
    }

    public boolean isAwaitingLogin() {
        return awaitingLoginResponse;
    }
}
