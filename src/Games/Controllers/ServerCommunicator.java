package Games.Controllers;

import Games.Controller;
import Games.Tile;
import javafx.application.Platform;
import view.Game;
import view.View;
import view.panes.BoardPane;
import view.panes.InfoPane;
import view.panes.humanvsremote.HumanVsRemoteBottomPane;
import view.panes.tournament.TournamentBottomPane;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class ServerCommunicator implements Runnable {
    private Controller controller;

    private String host;
    private int port;
    private String name;

    private DataOutputStream toServer;
    private BufferedReader fromServer;
    private Socket socket;

    private boolean shouldRun;

    private HumanVsRemoteBottomPane humanVsRemoteBottomPane;
    private TournamentBottomPane tournamentBottomPane;

    private ArrayList<String> gameList = new ArrayList<>();
    private ArrayList<String> playerList = new ArrayList<>();
    private ArrayList<Map> challengeList = new ArrayList<>();


    /**
     * Get settings from config file
     * @param controller AI or Human vs Remote controller
     */
    public ServerCommunicator(Controller controller){
        this.controller = controller;

        Properties properties = new Properties();
        String fileName = "src/Games/Controllers/settings.conf";
        InputStream is = null;
        try {
            is = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        host = properties.getProperty("host");
        port = Integer.valueOf(properties.getProperty("port"));
        name = properties.getProperty("name");
    }

    /**
     * Start socket connection with settings from config file
     * Open input and output streams
     */
    public void connectToServer(){
        try {
            socket = new Socket(host, port);
            toServer = new DataOutputStream(socket.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (shouldRun){
            try{
                String line;
                while ((line = fromServer.readLine()) != null){
                    String[] splitString = line.split("\\s+");
                    switch (splitString[0]){
                        //Handle server messages with keyword OK
                        case "OK":
                            break;
                        //Handle server messages with keyword ERR and throw an exception with the error message
                        case "ERR":
                            try {
                                throw new ERRException(line);
                            }catch (ERRException e){
                                e.printStackTrace();
                            }
                            break;
                        //Handle server messages with keyword SVR
                        case "SVR":
                            handleSRVMessage(line);
                            break;
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void handleSRVMessage(String line){
        String[] splitString = line.split("\\s+");
        switch (splitString[1]){
            //Handle server messages with keyword GAMELIST and add games to an arraylist
            case "GAMELIST":
                String games = trimLine(line.split("GAMELIST ")[1]);
                String[] game = games.split(",");
                for(String s: game){gameList.add(s);}
                break;
            //Handle server messages with keyword PLAYERLIST and add players to an arraylist
            case "PLAYERLIST":
                String players = trimLine(line.split("PLAYERLIST ")[1]);
                String[] player = players.split(",");
                for(String s: player){playerList.add(s);}
                break;
            case "GAME":
                handleGAMEMessage(line);
                break;
        }
    }

    public void handleGAMEMessage(String line){
        String[] splitString = line.split("\\s+");
        switch (splitString[2]){
            //Handle server messages with keyword MATCH
            case "MATCH":
                String matchInfo = line.split("MATCH ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> matchMap = (HashMap<String, String>) Arrays.asList(trimLine(matchInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //Check which player is the player to move first and set tile colours and info messages accordingly
                if (matchMap.get("PLAYERTOMOVE").equals(name)) {
                    controller.setPlayerOne(Tile.BLACK);
                    startGame(matchMap.get("GAMETYPE"));
                    View.getInstance().startController();
                    View.getInstance().updateInfoPane("Opponent is " + matchMap.get("OPPONENT"), "");
                }
                else{
                    controller.setPlayerOne(Tile.WHITE);
                    startGame(matchMap.get("GAMETYPE"));
                    View.getInstance().startController();
                    View.getInstance().updateInfoPane("Opponent is " + matchMap.get("OPPONENT"), "");
                }
                break;
            //Handle server messages with keyword YOURTURN
            case "YOURTURN":
                String turnInfo = line.split("YOURTURN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> turnMap = (HashMap<String, String>) Arrays.asList(trimLine(turnInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //If its our turn and the controller is for AI vs remote games, calculate an ai move
                if(controller instanceof AiVsRemoteController) {
                    controller.aiMove();
                }
                break;
            //Handle server messages with keyword MOVE
            case "MOVE":
                String moveInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> moveMap = (HashMap<String, String>) Arrays.asList(trimLine(moveInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //If the controller is for AI vs remote games, use playermove to tell the model what move was played by the opponent
                if(controller instanceof AiVsRemoteController) {
                    if (!moveMap.get("PLAYER").equals(name)) {
                        try {
                            int x = Integer.valueOf(moveMap.get("MOVE")) % controller.getBoardSize();
                            int y = Math.floorDiv(Integer.valueOf(moveMap.get("MOVE")), controller.getBoardSize());
                            controller.playerMove(x, y);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }
                }
                //If the controller is for Human vs remote games, use playertwomove to tell the model what move was played by the opponent
                else if (controller instanceof HumanVsRemoteController){
                    if (!moveMap.get("PLAYER").equals(name)) {
                        try {
                            int x = Integer.valueOf(moveMap.get("MOVE")) % controller.getBoardSize();
                            int y = Math.floorDiv(Integer.valueOf(moveMap.get("MOVE")), controller.getBoardSize());
                            controller.playerTwoMove(x, y);
                        }catch (NumberFormatException e){
                            e.printStackTrace();
                        }
                    }
                }
                break;
            //Handle server messages with keyword WIN
            case "WIN":
                String winInfo = line.split("WIN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> winMap = (HashMap<String, String>) Arrays.asList(trimLine(winInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //Display win message on the screen and remove forfeit button and add backtolobby button
                controller.displayGameResult("WIN", winMap.get("COMMENT"));
                if(controller instanceof HumanVsRemoteController){
                    Platform.runLater(()-> {
                        humanVsRemoteBottomPane.getChildren().remove(1);
                        humanVsRemoteBottomPane.addLobbyButton();
                    });
                //Display win message on the screen and remove forfeit button and add backtolobby button
                }else if(controller instanceof AiVsRemoteController){
                    Platform.runLater(()-> {
                        tournamentBottomPane.getChildren().remove(1);
                        tournamentBottomPane.addLobbyButton();
                    });
                }
                break;
            //Handle server messages with keyword LOSS
            case "LOSS":
                String lossInfo = line.split("LOSS ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> lossMap = (HashMap<String, String>) Arrays.asList(trimLine(lossInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //Display loss message on the screen and remove forfeit button and add backtolobby butotn
                controller.displayGameResult("LOSS", lossMap.get("COMMENT"));
                if(controller instanceof HumanVsRemoteController){
                    Platform.runLater(()-> {
                        humanVsRemoteBottomPane.getChildren().remove(1);
                        humanVsRemoteBottomPane.addLobbyButton();
                    });
                //Display loss message on the screen and remove forfeit button and add backtolobby butotn
                }else if(controller instanceof AiVsRemoteController){
                    Platform.runLater(()-> {
                        tournamentBottomPane.getChildren().remove(1);
                        tournamentBottomPane.addLobbyButton();
                    });
                }
                break;
            //Handle server messages with keyword DRAW
            case "DRAW":
                String drawInfo = line.split("DRAW ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> drawMap = (HashMap<String, String>) Arrays.asList(trimLine(drawInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //Display loss message on the screen and remove forfeit button and add backtolobby butotn
                controller.displayGameResult("DRAW", "");
                if(controller instanceof HumanVsRemoteController){
                    Platform.runLater(()-> {
                        humanVsRemoteBottomPane.getChildren().remove(1);
                        humanVsRemoteBottomPane.addLobbyButton();
                    });
                //Display loss message on the screen and remove forfeit button and add backtolobby butotn
                }else if(controller instanceof AiVsRemoteController){
                    Platform.runLater(()-> {
                        tournamentBottomPane.getChildren().remove(1);
                        tournamentBottomPane.addLobbyButton();
                    });
                }
                break;
            //Handle server messages with keyword CHALLENGE
            case "CHALLENGE":
                handleCHALLENGEMessage(line);
                break;
        }
    }

    public void handleCHALLENGEMessage(String line){
        String[] splitString = line.split("\\s+");
        //Handle server messages with keyword CANCELLED
        if(splitString[3].equals("CANCELLED")){
            String challengeInfo = line.split("CANCELED ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> challengeMap = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            //Remove the cancelled challenge from the challenges arraylist
            for(int i = 0; i<challengeList.size(); i++){
                if(challengeList.get(i).get("CHALLENGENUMBER").equals(challengeMap.get("CHALLENGENUMBER"))){
                    challengeList.remove(i);
                    break;
                }
            }
        }
        //Else handle server messages with keyword CHALLENGE
        else{
            String challengeInfo = line.split("CHALLENGE ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> challengeMap = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            //Add challenge to arraylist
            challengeList.add(challengeMap);
        }
    }

    private void startGame(String game){
        View.getInstance().clearStage();
        View.getInstance().setNextMove(null);
        BoardPane boardPane;
        String stringOne;
        String stringTwo = "You are playing vs a remote opponent";
        Game thisGame;

        if (game.equals("Tic-tac-toe")){
            thisGame = Game.TICTACTOE;
            boardPane = new BoardPane(3);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            stringOne = "Welcome to a new game of TicTacToe!";
        }
        else if(game.equals("Reversi")){
            thisGame = Game.REVERSI;
            boardPane = new BoardPane(8);
            View.getInstance().setBoardPane(boardPane);
            View.getInstance().setCenter(boardPane);
            stringOne = "Welcome to a new game of Reversi!";
        }
        else throw new IllegalArgumentException();

        InfoPane infoPane = new InfoPane(stringOne, stringTwo);
        View.getInstance().setInfoPane(infoPane);
        View.getInstance().setTop(infoPane);

        if(controller instanceof HumanVsRemoteController){
            humanVsRemoteBottomPane = new HumanVsRemoteBottomPane(thisGame);
            View.getInstance().setBottom(humanVsRemoteBottomPane);
        }else if(controller instanceof AiVsRemoteController){
            tournamentBottomPane = new TournamentBottomPane(thisGame);
            View.getInstance().setBottom(tournamentBottomPane);
        }
        else throw new IllegalStateException();

        View.getInstance().getController().newGame();
    }

    /**
     * Send a command to the server
     * @param command
     */
    public void sendToServer(String command){
        try{
            toServer.writeBytes(command + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Filter the messages from the server so that the values can be added to lists or maps in a clean manner
     * @param lineToTrim unfiltered string
     * @return filtered string output
     */
    public String trimLine(String lineToTrim){
        lineToTrim = lineToTrim.replace("{", "");
        lineToTrim = lineToTrim.replace("}", "");
        lineToTrim = lineToTrim.replace(" ", "");
        lineToTrim = lineToTrim.replace("\"\"", " ");
        lineToTrim = lineToTrim.replace("\"", "");
        lineToTrim = lineToTrim.replace("[", "");
        lineToTrim = lineToTrim.replace("]", "");
        return lineToTrim;
    }

    public void login(){
        shouldRun = true;
        sendToServer("login " + name);
    }

    public void logout(){
        sendToServer("bye");
        try {
            shouldRun = false;
            fromServer.close();
            toServer.close();
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void sendMove(int move){ sendToServer("move " + move); }

    public void subscribe(String game){ sendToServer("subscribe " + game); }

    public void getGameList(){ sendToServer("get gamelist"); }

    public void getPlayerList(){ sendToServer("get playerlist"); }

    public void challenge(String player, String game){ sendToServer("challenge \"" + player + "\" \"" + game + "\""); }

    public void acceptChallenge(int challengeID){ sendToServer("challenge accept " + challengeID); }

    public void forfeit(){ sendToServer("forfeit"); }

    public ArrayList controllerGetPlayerList(){ return playerList; }

    public ArrayList controllerGetGameList(){ return gameList;}

    public ArrayList controllerGetChallengeList(){return challengeList;}
}