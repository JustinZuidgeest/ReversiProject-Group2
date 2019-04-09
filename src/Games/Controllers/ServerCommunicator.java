package Games.Controllers;

import Games.Controller;
import Games.Tile;

import java.awt.*;
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

    private ArrayList<String> gameList = new ArrayList<>();
    private ArrayList<String> playerList = new ArrayList<>();


    public ServerCommunicator(Controller controller){
        this.controller = controller;

        Properties properties = new Properties();
        String fileName = "D:\\Vincent\\School\\Jaar 2\\Project bordspel AI\\ReversiProject-Group2\\src\\server_communication\\settings.conf";
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
                            //command goed uitgevoerd
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
                //TODO send gamelist to controller
                System.out.println("Available games: " + gameList);
                break;
            case "PLAYERLIST":
                String players = trimLine(line.split("PLAYERLIST ")[1]);
                String[] player = players.split(",");
                for(String s: player){playerList.add(s);}
                //TODO send playerlist to controller
                System.out.println("Players online: " + playerList);
                break;
            case "GAME":
                handleGAMEMessage(line);
                break;
        }
    }

    public void handleGAMEMessage(String line){
        String[] splitString = line.split("\\s+");

        switch (splitString[2]){
            case "MATCH":
                String matchInfo = line.split("MATCH ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> matchMap = (HashMap<String, String>) Arrays.asList(trimLine(matchInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                if (matchMap.get("PLAYERTOMOVE").equals(name)) {
                    controller.setHumanPlayer(Tile.WHITE);
                }
                else{
                    controller.setHumanPlayer(Tile.BLACK);
                }
                System.out.println(matchMap);
                break;
            case "YOURTURN":
                String turnInfo = line.split("YOURTURN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> turnMap = (HashMap<String, String>) Arrays.asList(trimLine(turnInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                System.out.println(turnMap);
                //TODO choose between aiMove or playerMove
                controller.aiMove();
                break;
            case "MOVE":
                String moveInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> moveMap = (HashMap<String, String>) Arrays.asList(trimLine(moveInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                System.out.println(moveMap);
                if(!moveMap.get("PLAYER").equals(name)) {
                    int x = Integer.valueOf(moveMap.get("MOVE")) % 3;
                    int y = Math.floorDiv(Integer.valueOf(moveMap.get("MOVE")), 3);
                    System.out.println("player moved " + x + y);
                    controller.playerMove(x, y);
                }
                break;
            case "WIN":
                String winInfo = line.split("WIN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> winMap = (HashMap<String, String>) Arrays.asList(trimLine(winInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO link to GUI display win message
                System.out.println("YOU WON!!! " + winMap);
                controller.newGame();
                break;
            case "LOSS":
                String lossInfo = line.split("LOSS ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> lossMap = (HashMap<String, String>) Arrays.asList(trimLine(lossInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO link to GUI display loss message
                System.out.println("YOU LOST!! " + lossMap);
                controller.newGame();
                break;
            case "DRAW":
                String drawInfo = line.split("DRAW ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> drawMap = (HashMap<String, String>) Arrays.asList(trimLine(drawInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO link to GUI display draw message
                System.out.println("ITS A DRAW!! " + drawMap);
                controller.newGame();
                break;
            case "CHALLENGE":
                handleCHALLENGEMessage(line);
                break;

        }
    }

    public void handleCHALLENGEMessage(String line){
        String[] splitString = line.split("\\s+");

        if(!splitString[3].equals("CANCELLED")){
            String challengeInfo = line.split("CHALLENGE ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> map = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            //TODO link to GUI display challenge message
            System.out.println(map);
        }
        else{
            String challengeInfo = line.split("CANCELED ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> map = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            //TODO cancel challenge message
            System.out.println(map);
        }
    }

    public void sendToServer(String command){
        try{
            toServer.writeBytes(command + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Point convertIndexToPoint(int index){
        //TODO get boardsize from controller
        Point point = new Point();
        int x = index % 3;
        int y = Math.floorDiv(index, 3);
        return point;
    }

    public String trimLine(String lineToTrim){
        lineToTrim = lineToTrim.replace("{", "");
        lineToTrim = lineToTrim.replace("}", "");
        lineToTrim = lineToTrim.replace(" ", "");
        lineToTrim = lineToTrim.replace("\"\"", "empty");
        lineToTrim = lineToTrim.replace("\"", "");
        lineToTrim = lineToTrim.replace("[", "");
        lineToTrim = lineToTrim.replace("]", "");
        return lineToTrim;
    }

    public void login(){
        shouldRun = true;
        sendToServer("login " + name);
        System.out.println("logged in");
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

    public ArrayList controllerGetPlayerList(){ return playerList;}

    public ArrayList controllerGetGameList(){ return gameList;}
}