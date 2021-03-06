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
    private boolean canMove;
    private Point nextMove;

    /**
     * The method that is called at application start by JavaFX. Starts a new JavaFX thread and initializes all variables
     * needed to display an application on the screen.
     * @param primaryStage A Stage object used by JavaFX as the main screen of the application. Automatically generated
     *                     by JavaFX at startup.
     */
    @Override
    public void start(Stage primaryStage){
        instance = this;
        this.primaryStage = primaryStage;
        primaryPane = new BorderPane();
        Scene primaryScene = new Scene(primaryPane);
        primaryScene.getStylesheets().add("/view/styling.css");

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

    public void setInfoPane(InfoPane infoPane) {
        this.infoPane = infoPane;
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
