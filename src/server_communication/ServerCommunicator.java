package server_communication;


import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class ServerCommunicator implements Runnable {
    private String host;
    private int port;
    private String name;

    private DataOutputStream toServer;
    private BufferedReader fromServer;
    private Socket socket;

    private boolean shouldRun;

    ArrayList<String> gameList = new ArrayList<>();
    ArrayList<String> playerList = new ArrayList<>();


    public ServerCommunicator(){
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
                            //TODO last command failed, try again
                            System.out.println(line);
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
                String[] game = games.split(", ");
                for(String s: game){gameList.add(s);}
                //TODO send gamelist to controller
                System.out.println("Available games: " + gameList);
                break;
            case "PLAYERLIST":
                String players = trimLine(line.split("PLAYERLIST ")[1]);
                String[] player = players.split(", ");
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
                //TODO send map to controller
                System.out.println(matchMap);
                break;
            case "YOURTURN":
                String turnInfo = line.split("YOURTURN ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> turnMap = (HashMap<String, String>) Arrays.asList(trimLine(turnInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO send map to controller
                System.out.println(turnMap);
                break;
            case "MOVE":
                String moveInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> moveMap = (HashMap<String, String>) Arrays.asList(trimLine(moveInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO send map to controller
                System.out.println(moveMap);
                break;
            case "WIN":
                String winInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> winMap = (HashMap<String, String>) Arrays.asList(trimLine(winInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO send map to controller
                System.out.println(winMap);
                break;
            case "LOSS":
                String lossInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> lossMap = (HashMap<String, String>) Arrays.asList(trimLine(lossInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO send map to controller
                System.out.println(lossMap);
                break;
            case "DRAW":
                String drawInfo = line.split("MOVE ")[1];
                //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
                HashMap<String, String> drawMap = (HashMap<String, String>) Arrays.asList(trimLine(drawInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
                //TODO send map to controller
                System.out.println(drawMap);
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
            //TODO send map to controller
            System.out.println(map);
        }
        else{
            String challengeInfo = line.split("CANCELED ")[1];
            //Code by Jeremy Bidet -> https://stackoverflow.com/questions/10514473/string-to-hashmap-java
            HashMap<String, String> map = (HashMap<String, String>) Arrays.asList(trimLine(challengeInfo).split(",")).stream().map(s -> s.split(":")).collect(Collectors.toMap(e -> e[0], e -> e[1]));
            //TODO send map to controller
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

    public String trimLine(String lineToTrim){
        lineToTrim = lineToTrim.replace("{", "");
        lineToTrim = lineToTrim.replace("}", "");
        lineToTrim = lineToTrim.replace(" ", "");
        lineToTrim = lineToTrim.replace("\"", "");
        lineToTrim = lineToTrim.replace("[", "");
        lineToTrim = lineToTrim.replace("]", "");
        return lineToTrim;
    }

    public void login(){
        //TODO team name
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

    public void sendMove(String move){
        sendToServer("move " + move);
    }

    public void subscribe(String game){
        if(game.equals("reversi")) {
            sendToServer("subscribe Reversi");
        }
        else if(game.equals("tic-tac-toe")){
            sendToServer("subscribe Tic-tac-toe");
        }
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

    public void forfeit(){
        sendToServer("forfeit");
    }
}