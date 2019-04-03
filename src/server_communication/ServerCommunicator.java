package server_communication;


import java.io.*;
import java.net.Socket;
import java.util.Properties;

public class ServerCommunicator implements Runnable {
    private String host;
    private int port;

    private DataOutputStream toServer;
    private BufferedReader fromServer;
    private Socket socket;

    private boolean shouldRun = true;


    public ServerCommunicator(){
        Properties properties = new Properties();
        String fileName = "settings.conf";
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
                            break;
                        case "SVR":
                            handleSRVMessage(splitString);
                            break;
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public void handleSRVMessage(String[] line){
        switch (line[1]){
            case "GAMELIST":
                System.out.println(line[2] + line[3]);
                break;
            case "PLAYERLIST":
                System.out.println(line[2] + line[3]);
                break;
            case "GAME":
                handleGAMEMessage(line);
                break;
        }
    }

    public void handleGAMEMessage(String[] line){
        switch (line[2]){
            case "MATCH":
                //TODO read map {gametype, playertomove, opponent}
                break;
            case "YOURTURN":
                //TODO read map {turnmessage}
                break;
            case "MOVE":
                //TODO read map {player, details, move}
                break;
            case "WIN":
                //TODO read map {playeronescore, playertwoscore, comment}
                //if comment.equals("Player forfeited match") opponent forfeited
                //else if comment.equals("Client disconnected") opponent disconnected
                break;
            case "LOSS":
                //TODO read map {playeronescore, playertwoscore, comment}
                //if comment.equals("Player forfeited match") opponent forfeited
                //else if comment.equals("Client disconnected") opponent disconnected
                break;
            case "DRAW":
                //TODO read map {playeronescore, playertwoscore, comment}
                //if comment.equals("Player forfeited match") opponent forfeited
                //else if comment.equals("Client disconnected") opponent disconnected
                break;
            case "CHALLENGE":
                handleCHALLENGEMessage(line);
                break;

        }
    }

    public void handleCHALLENGEMessage(String[] line){
        if(!line[3].equals("CANCELLED")){
            //TODO read map {challenger, gametype, challengenumber} and add to challengers
        }
        else{
            //TODO read map {challengenumber} and remove from challengers
        }
    }

    public void sendToServer(String command){
        try{
            toServer.writeBytes(command);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void login(){
        //TODO team name
        sendToServer("login group_2");
    }

    public void logout(){
        sendToServer("bye");
        try {
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

    public void challenge(String player){
        sendToServer("challenge " + player);
    }

    public void acceptChallenge(int challengeID){
        sendToServer("challenge accept " + challengeID);
    }

    public void forfeit(){
        sendToServer("forfeit");
    }
}