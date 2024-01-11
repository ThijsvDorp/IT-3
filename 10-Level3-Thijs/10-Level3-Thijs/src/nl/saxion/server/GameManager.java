package nl.saxion.server;

import nl.saxion.shared.commands.game.*;
import nl.saxion.shared.responses.Response;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager {
    private final UserManager userManager;
    private List<ClientConnectionHandler> players;
    private List<GameResult> results;
    private boolean gameStarting;
    private boolean gameInProgress;
    private int number;
    private Timer joinTimer;
    private Timer gameTimer;
    private LocalDateTime startTime;


    public GameManager(UserManager userManager) {
        this.userManager = userManager;
        this.players = new CopyOnWriteArrayList<>();
        this.results = new ArrayList<>();
        this.gameStarting = false;

        this.joinTimer = new Timer();
        this.gameTimer = new Timer();

    }

    public void startGame(ClientConnectionHandler player) {
        if (userManager.getSize() < 2){
            String response = Response.formatCommand(GameCreateRequest.RESPONSE_HEADER, Response.error(6200));
            player.sendMessage(response);
            return;
        }
        if (!gameInProgress){
            String response = Response.formatCommand(GameCreateRequest.RESPONSE_HEADER, Response.error(6201));
            player.sendMessage(response);
            return;
        }
        if (gameStarting){
            String response = Response.formatCommand(GameCreateRequest.RESPONSE_HEADER, Response.error(6202));
            player.sendMessage(response);
            return;
        }
        //confirm to starter
        String okResponse = Response.formatCommand(GameCreateRequest.RESPONSE_HEADER, Response.ok());
        player.sendMessage(okResponse);

        //notify everyone about game
        GameInviteCommand gameInvite = new GameInviteCommand(player.getUsername());
        userManager.sendAll(gameInvite.toJson());

        //10 second period to let players join
        joinTimer = new Timer();
        joinTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (players.size() < 2) {
                    gameStarting = false;
                    String response = Response.formatCommand(GameCreateRequest.RESPONSE_HEADER, Response.error(6200));
                    userManager.sendAll(response);
                } else {
                    startGame();
                }
            }
        }, 10000); // 10 seconden
    }

    private void startGame() {
        gameStarting = false;
        gameInProgress = true;
        setRandomNumber();
        userManager.sendAll(GameBeginCommand.HEADER);
        startTime = LocalDateTime.now();

        //2-minute timer starts and will call endgame if not everyone has guessed
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                endGame();
            }
        }, 120000); // 2 minutes
    }


    public void addPlayer(ClientConnectionHandler player) {
        //check if game has not been initiated
        if(!gameStarting){
            String response = GameJoinRequest.createResponse(Response.error(6300));
            player.sendMessage(response);
            return;
        }
        //check if game is in progress
        if (!gameInProgress){
            String response = GameJoinRequest.createResponse(Response.error(6301));
            player.sendMessage(response);
            return;
        }
        //check if player already joined
        if (players.contains(player)){
            String response = GameJoinRequest.createResponse(Response.error(6302));
            player.sendMessage(response);
            return;
        }
        players.add(player);
    }

    public void handleGuess(int guess, ClientConnectionHandler player) {
        //if user has not joined the game in progress
        if (!players.contains(player)){
            String response = GameGuessRequest.createResponse(Response.error(6400));
            player.sendMessage(response);
            return;
        }
        //if no game is in progess
        if (!gameInProgress){
            String response = GameGuessRequest.createResponse(Response.error(6401));
            player.sendMessage(response);
            return;
        }
        //if user has already guessed correctly
        if (isUsernamePresent(player.getUsername())){
            String response = GameGuessRequest.createResponse(Response.error(6402));
            player.sendMessage(response);
            return;
        }
        //if user guess is out of bounds
        if (guess <= 0 || guess > 50){
            String response = GameGuessRequest.createResponse(Response.error(6403));
            player.sendMessage(response);
            return;
        }
        //handle guess
        if (guess < number){
            player.sendMessage(GameGuessFeedback.tooLow());
        } else if (guess > number) {
            player.sendMessage(GameGuessFeedback.tooHigh());
        }else if (guess == number) {
            player.sendMessage(GameGuessFeedback.correct());
            GameResult gameResult = new GameResult(
                    player.getUsername(),
                    (int) Duration.between(startTime, LocalDateTime.now()).toMillis(),
                    "WINNER");
            results.add(gameResult);

            //check if all players guessed
            if (results.size() >= players.size()) {
                gameTimer.cancel();
                endGame();
            }
        }
    }

    public boolean isUsernamePresent(String username) {
        //check if player already has a final gameResult
        for (GameResult result : results) {
            if (result.getUsername().equals(username)) {
                return true; // username found
            }
        }
        return false; // Username not found
    }

    public void setRandomNumber(){
        Random random = new Random();
        this.number = random.nextInt(50) + 1;
    }

    public void endGame(){
        GameResultCommand gameResults = new GameResultCommand(results);
        userManager.sendAll(gameResults.toJson());
        addLosers();
    }

    private void addLosers() {
        for (ClientConnectionHandler player : players){
            if (!isUsernamePresent(player.getUsername())){
                GameResult loserResult = new GameResult(player.getUsername(), 120000, "LOSER");
                results.add(loserResult);
            }
        }
    }
}
