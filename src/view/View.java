package view;

import games.Controller;
import games.Model;
import games.Tile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.panes.BoardPane;
import view.panes.MainMenu;

public class View extends Application {

    private static View instance;
    private Stage primaryStage;
    private VBox mainMenu;
    private BoardPane boardPane;
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

        initPanes();
        setCenter(mainMenu);

        primaryStage.setScene(primaryScene);
        primaryStage.setMaxWidth(800);
        primaryStage.setMaxHeight(800);
        primaryStage.setTitle("Arithm games");
        primaryStage.show();
    }

    private void initPanes(){
        mainMenu = new MainMenu();
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
        Platform.runLater(() -> boardPane.updateBoard(board));
    }

    public void moveMade(int x, int y){
        controller.playerMove(x, y);
        controller.aiMove();
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
