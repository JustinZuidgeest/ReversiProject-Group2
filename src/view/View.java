package view;

import games.Controller;
import games.Model;
import games.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import view.panes.BoardPane;
import view.panes.MainMenu;
import view.panes.ScorePane;

public class View extends Application {

    private static View instance;
    private Stage primaryStage;
    private BoardPane boardPane;
    private ScorePane scorePane;
    private Model model;
    private Controller controller;
    private BorderPane primaryPane;
    private Scene primaryScene;

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        this.primaryStage = primaryStage;
        primaryPane = new BorderPane();
        primaryScene = new Scene(primaryPane);

        // Start the application at the Main Menu
        setCenter(new MainMenu());

        primaryStage.setScene(primaryScene);
        primaryStage.setMaxWidth(800);
        primaryStage.setMaxHeight(800);
        primaryStage.setTitle("Arithm games");
        primaryStage.show();
    }

    public void setCenter(Node node){
        Platform.runLater(() -> primaryPane.setCenter(node));
    }

    public void setRight(Node node){
        Platform.runLater(() -> primaryPane.setRight(node));
    }

    public void setLeft(Node node){
        Platform.runLater(() -> primaryPane.setLeft(node));
    }

    public void setTop(Node node){
        Platform.runLater(() -> primaryPane.setTop(node));
    }

    public void setBottom(Node node){
        Platform.runLater(() -> primaryPane.setBottom(node));
    }

    public void updateBoard(Tile[][] board){
        Platform.runLater(() -> boardPane.updateBoard(board));
    }

    public void updateScores(int player1, int player2){
        Platform.runLater(() -> scorePane.updateScores(player1, player2));
    }

    public void clearStage(){
        Platform.runLater(() -> {
            primaryPane.setCenter(null);
            primaryPane.setLeft(null);
            primaryPane.setRight(null);
            primaryPane.setBottom(null);
            primaryPane.setTop(null);
        });
    }

    public void moveMade(int x, int y){
        controller.playerMove(x, y);
        controller.aiMove();
    }

    public ScorePane getScorePane() {
        return scorePane;
    }

    public void setScorePane(ScorePane scorePane) {
        this.scorePane = scorePane;
    }

    public BoardPane getBoardPane() {
        return boardPane;
    }

    public void setBoardPane(BoardPane boardPane) {
        this.boardPane = boardPane;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public static View getInstance(){
        return instance;
    }

    public void quitApplication(){
        primaryStage.close();
    }
}
