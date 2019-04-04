package server_communication;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServerCommunicator implements Runnable {
    private String host;
    private int port;
    private String name;

    private DataOutputStream toServer;
    private BufferedReader fromServer;
    private Socket socket;

    private boolean shouldRun = true;

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
                String games = line.split("GAMELIST ")[1];
                games = games.replace("[", "");
                games = games.replace("]", "");
                games = games.replace("\"", "");
                String[] game = games.split(", ");
                for(String s: game){gameList.add(s);}
                System.out.println("Available games: " + gameList);
                break;
            case "PLAYERLIST":
                String players = line.split("PLAYERLIST ")[1];
                players = players.replace("[", "");
                players = players.replace("]", "");
                players = players.replace("\"", "");
                String[] player = players.split(", ");
                for(String s: player){playerList.add(s);}
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
                //TODO read map {gametype, playertomove, opponent}
                for(String s : splitString){System.out.print(s);}
                break;
            case "YOURTURN":
                //TODO read map {turnmessage}
                for(String s : splitString){System.out.print(s);}
                break;
            case "MOVE":
                //TODO read map {player, details, move}
                for(String s : splitString){System.out.print(s);}
                break;
            case "WIN":
                //TODO read map {playeronescore, playertwoscore, comment}
                //if comment.equals("Player forfeited match") opponent forfeited
                //else if comment.equals("Client disconnected") opponent disconnected
                for(String s : splitString){System.out.print(s);}
                break;
            case "LOSS":
                //TODO read map {playeronescore, playertwoscore, comment}
                //if comment.equals("Player forfeited match") opponent forfeited
                //else if comment.equals("Client disconnected") opponent disconnected
                for(String s : splitString){System.out.print(s);}
                break;
            case "DRAW":
                //TODO read map {playeronescore, playertwoscore, comment}
                //if comment.equals("Player forfeited match") opponent forfeited
                //else if comment.equals("Client disconnected") opponent disconnected
                for(String s : splitString){System.out.print(s);}
                break;
            case "CHALLENGE":
                handleCHALLENGEMessage(line);
                break;

        }
    }

    public void handleCHALLENGEMessage(String line){
        String[] splitString = line.split("\\s+");

        if(!splitString[3].equals("CANCELLED")){
            //TODO read map {challenger, gametype, challengenumber} and add to challengers
            for(String s : splitString){System.out.print(s);}
        }
        else{
            //TODO read map {challengenumber} and remove from challengers
            for(String s : splitString){System.out.print(s);}
        }
    }

    public void sendToServer(String command){
        try{
            toServer.writeBytes(command + "\n");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void login(){
        //TODO team name
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
        sendToServer("challenge " + "\"" + player + "\"" + " " + "\"" + game + "\"");
    }

    public void acceptChallenge(int challengeID){
        sendToServer("challenge accept " + challengeID);
    }

    public void forfeit(){
        sendToServer("forfeit");
    }
}