package TicTacToe;

import TicTacToe.AI_Models.MinimaxAIModel;
import TicTacToe.AI_Models.AbstractModel;

public class Main {

    public static void main(String[] args) {
        CommandlineView view = new CommandlineView();
        AbstractModel model = new MinimaxAIModel(3);
        TictactoeController controller = new TictactoeController(view, model);
        view.setController(controller);

        view.start();
    }
}
