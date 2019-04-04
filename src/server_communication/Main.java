package server_communication;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ServerCommunicator communicator = new ServerCommunicator();

        communicator.connectToServer();
        new Thread(communicator).start();

        communicator.login();
        communicator.getGameList();
        communicator.getPlayerList();

        communicator.challenge("test", "Reversi");

        Scanner scanner = new Scanner(System.in);
        String line;
        while (scanner.hasNext()){
            line = scanner.next();
            if(line.equals("bye")){
                communicator.logout();
            }
        }
    }
}
