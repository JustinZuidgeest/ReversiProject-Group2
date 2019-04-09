package tictactoe;

import server_communication.ServerCommunicator;
import tictactoe.AI_Models.MinimaxAIModel;
import tictactoe.AI_Models.AbstractModel;
import tictactoe.AI_Models.MinimaxAlphaBetaAIModel;
import tictactoe.AI_Models.RandomAIModel;

public class Main {

    public static void main(String[] args) {
        // Create the view
        CommandlineView view = new CommandlineView();

        // Create a model
        // Use this model for a minimax AI with alpha-beta pruning
        AbstractModel model = new MinimaxAlphaBetaAIModel(3);

        // Create the controller and pass the view and model
        TictactoeController controller = new TictactoeController(view, model);

        // Set the controller for the view
        view.setController(controller);
        // Start the main game loop
        view.start();
    }
}
