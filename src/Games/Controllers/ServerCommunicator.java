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
                        case "OK":
                            break;
                        case "ERR":
                            try {
                                throw new ERRException(line);
                            }catch (ERRException e){
                                e.printStackTrace();
                            }
                            break;
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
            case "GAMELIST":
                String games = trimLine(line.split("GAMELIST ")[1]);
                String[] game = games.split(",");
                for(String s: game){gameList.add(s);}
                break;
            case "PLAYERLIST":
                String players = trimLine(line.split("PLAYERLIST ")[1]);
                String[] player = players.split(",");
                playerList = new ArrayList<>();
                for(String s: player){playerList.add(s);}
                break;
            case "GAME":
                handleGAMEMessage(line);
                break;
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

        //humanVsRemoteBottomPane = new HumanVsRemoteBottomPane(thisGame);
        //View.getInstance().setBottom(humanVsRemoteBottomPane);

        if(controller instanceof HumanVsRemoteController){
            System.out.println("Human vs remote bottompane made");
            humanVsRemoteBottomPane = new HumanVsRemoteBottomPane(thisGame);
            View.getInstance().setBottom(humanVsRemoteBottomPane);
        }else if(controller instanceof AiVsRemoteController){
            System.out.println("Tournament bottompane made");
            tournamentBottomPane = new TournamentBottomPane(thisGame);
            View.getInstance().setBottom(tournamentBottomPane);
        }
        else throw new IllegalStateException();

        View.getInstance().getController().newGame();
    }

    public void handleGAMEMessage(String line){
        String[] splitString = line.split("\\s+");

        switch (splitString[2]){
            case "MATCH":
                String matchInfo = line.split("MATCH ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> matchMap = (HashMap<String, String>) Arrays.asList(trimLine(matchInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
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
            case "YOURTURN":
                String turnInfo = line.split("YOURTURN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> turnMap = (HashMap<String, String>) Arrays.asList(trimLine(turnInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                if(controller instanceof AiVsRemoteController) {
                    controller.aiMove();
                }
                break;
            case "MOVE":
                String moveInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> moveMap = (HashMap<String, String>) Arrays.asList(trimLine(moveInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                if(controller instanceof AiVsRemoteController) {
                    if (!moveMap.get("PLAYER").equals(name)) {
                        int x = Integer.valueOf(moveMap.get("MOVE")) % controller.getBoardSize();
                        int y = Math.floorDiv(Integer.valueOf(moveMap.get("MOVE")), controller.getBoardSize());
                        controller.playerMove(x, y);
                    }
                }
                else if (controller instanceof HumanVsRemoteController){
                    if (!moveMap.get("PLAYER").equals(name)) {
                        int x = Integer.valueOf(moveMap.get("MOVE")) % controller.getBoardSize();
                        int y = Math.floorDiv(Integer.valueOf(moveMap.get("MOVE")), controller.getBoardSize());
                        controller.playerTwoMove(x, y);
                    }
                }
                break;
            case "WIN":
                String winInfo = line.split("WIN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> winMap = (HashMap<String, String>) Arrays.asList(trimLine(winInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO link to GUI display win message
                controller.displayGameResult("WIN", winMap.get("COMMENT"));
                if(controller instanceof HumanVsRemoteController){
                    Platform.runLater(()-> {
                        humanVsRemoteBottomPane.getChildren().remove(1);
                        humanVsRemoteBottomPane.addLobbyButton();
                    });
                }else if(controller instanceof AiVsRemoteController){
                    Platform.runLater(()-> {
                        tournamentBottomPane.getChildren().remove(1);
                        tournamentBottomPane.addLobbyButton();
                    });
                }
                break;
            case "LOSS":
                String lossInfo = line.split("LOSS ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> lossMap = (HashMap<String, String>) Arrays.asList(trimLine(lossInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO link to GUI display loss message
                controller.displayGameResult("LOSS", lossMap.get("COMMENT"));
                if(controller instanceof HumanVsRemoteController){
                    Platform.runLater(()-> {
                        humanVsRemoteBottomPane.getChildren().remove(1);
                        humanVsRemoteBottomPane.addLobbyButton();
                    });
                }else if(controller instanceof AiVsRemoteController){
                    Platform.runLater(()-> {
                        tournamentBottomPane.getChildren().remove(1);
                        tournamentBottomPane.addLobbyButton();
                    });
                }
                break;
            case "DRAW":
                String drawInfo = line.split("DRAW ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> drawMap = (HashMap<String, String>) Arrays.asList(trimLine(drawInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO link to GUI display draw message
                controller.displayGameResult("DRAW", drawMap.get("COMMENT"));
                if(controller instanceof HumanVsRemoteController){
                    Platform.runLater(()-> {
                        humanVsRemoteBottomPane.getChildren().remove(1);
                        humanVsRemoteBottomPane.addLobbyButton();
                    });
                }else if(controller instanceof AiVsRemoteController){
                    Platform.runLater(()-> {
                        tournamentBottomPane.getChildren().remove(1);
                        tournamentBottomPane.addLobbyButton();
                    });
                }
                break;
            case "CHALLENGE":
                handleCHALLENGEMessage(line);
                break;
        }
    }

    public void handleCHALLENGEMessage(String line){
        String[] splitString = line.split("\\s+");

        if(splitString[3].equals("CANCELLED")){
            String challengeInfo = line.split("CANCELED ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> challengeMap = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            for(int i = 0; i<challengeList.size(); i++){
                if(challengeList.get(i).get("CHALLENGENUMBER").equals(challengeMap.get("CHALLENGENUMBER"))){
                    challengeList.remove(i);
                }
            }
        }
        else{
            String challengeInfo = line.split("CHALLENGE ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> challengeMap = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            challengeList.add(challengeMap);
        }
    }

    public void sendToServer(String command){
        try{
            toServer.writeBytes(command + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

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

    public void sendMove(int move){
        sendToServer("move " + move);
    }

    public void subscribe(String game){
        sendToServer("subscribe " + game);
    }

    public void getGameList(){
        sendToServer("get gamelist");
    }

    public void getPlayerList(){
        sendToServer("get playerlist");
    }

    public void challenge(String player, String game){
        sendToServer("challenge \"" + player + "\" \"" + game + "\"");
    }

    public void acceptChallenge(int challengeID){
        sendToServer("challenge accept " + challengeID);
    }

    public void forfeit(){ sendToServer("forfeit"); }

    public ArrayList controllerGetPlayerList(){
        return playerList;
    }

    public ArrayList controllerGetGameList(){ return gameList;}

    public ArrayList controllerGetChallengeList(){return challengeList;}
}