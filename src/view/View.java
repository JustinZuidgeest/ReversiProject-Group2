package view;

import Games.Controller;
import Games.Model;
import Games.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import view.panes.BoardPane;
import view.panes.MainMenu;
import view.panes.InfoPane;

import java.awt.*;

public class View extends Application {

    private static View instance;
    private Stage primaryStage;
    private BoardPane boardPane;
    private InfoPane infoPane;
    private Model model;
    private Controller controller;
    private BorderPane primaryPane;
    private Scene primaryScene;
    private boolean canMove;
    private Point nextMove;

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
        primaryStage.setTitle("Arithm Games");
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
        if(boardPane != null) {
            Platform.runLater(() -> boardPane.updateBoard(board));
        }
    }

    public void updateInfoPane(String infoOne, String infoTwo){ Platform.runLater(() -> infoPane.updateScores(infoOne, infoTwo)); }

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
        System.out.println("Can move: " + canMove);
        if(canMove){
            nextMove = new Point(x, y);
        }
    }

    public void startController(){
        Thread thread = new Thread(controller);
        thread.setDaemon(true);
        thread.start();
    }

    public void setCanMove(boolean canMove) { this.canMove = canMove; }

    public Point getNextMove() { return nextMove; }

    public void setNextMove(Point nextMove) { this.nextMove = nextMove; }

    public InfoPane getInfoPane() {
        return infoPane;
    }

    public void setInfoPane(InfoPane infoPane) {
        this.infoPane = infoPane;
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

    public void killController(){ if (controller != null) controller.killThread(); }

    public void quitApplication(){
        primaryStage.close();
    }
}
