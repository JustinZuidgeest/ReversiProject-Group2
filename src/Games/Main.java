package Games;

import Games.Controllers.AiVsRemoteController;
import Games.Controllers.HumanVsAiController;
import Games.Reversi.ReversiCommandlineView;
import Games.Reversi.ReversiModels.ReversiMiniMaxAlphaBetaAI;
import Games.TicTacToe.TictactoeCommandlineView;
import Games.TicTacToe.TictactoeModels.TictactoeMinimaxAlphaBetaAI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Make empty variables that will hold the view, controller and model
        View view;
        Model model;
        Controller controller;

        // Ask what game the user wants to play
        Scanner scanner = new Scanner(System.in);
        System.out.println("TicTacToe (T) or Reversi (R)? T/R:");
        if(scanner.nextLine().toUpperCase().startsWith("T")){
            // Create the view for TicTacToe
            view = new TictactoeCommandlineView();
            // Create a model for a minimax AI with alpha-beta pruning
            model = new TictactoeMinimaxAlphaBetaAI(3);
        }else{
            // Create the view for Reversi
            view = new ReversiCommandlineView();
            model = new ReversiMiniMaxAlphaBetaAI(8);
        }
        // Create the controller and pass the view and model
        controller = new HumanVsAiController(view, model);
        // Start the main game loop
        controller.start();
    }
}
