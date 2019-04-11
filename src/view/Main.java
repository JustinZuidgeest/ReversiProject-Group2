package view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.popupWindows.ConfirmQuit;
import view.scenes.*;

public class Main extends Application {

    private static Main instance;
    private Stage window;
    private Scene mainMenuScene;
    private Scene reversiScene;
    private Scene ticTacToeScene;
    private Scene rulesScene;
    private Scene rulesScene2;
    private Scene tttsceneRegels;
    private Scene revsceneRegels;
    private Scene revsettings;
    private Scene tttsettings;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        instance = this;
        initScenes();

        window = primaryStage;
        window.setTitle("Arithm Games");

        window.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });

        window.setScene(mainMenuScene);
        window.show();

        //TODO fix questionable spaghetti
        //reversiScene = new RevToGame().getScene(window, mainMenuScene);
        //ticTacToeScene = new TttToGame().getScene(window, mainMenuScene);

        //rulesScene = new RulesMenu().getScene(window, mainMenuScene, revsceneRegels = new ReversiRegels().getScene(window), tttsceneRegels = new TicTacToeRuleScene().getScene(window));
    }

    private void initScenes(){
        mainMenuScene = new MainMenu().getScene();
        rulesScene = new RulesMenu().getScene();
        revsceneRegels = new ReversiRegels().getScene();
        tttsceneRegels = new TicTacToeRuleScene().getScene();
        revsettings = new RevToGame().getScene();
        tttsettings = new TttToGame().getScene();
    }

    public static Main getInstance(){
        return instance;
    }

    public void switchScene(Scenes scene){
        switch (scene){
            case MAINMENU: window.setScene(mainMenuScene);
                break;
            case RULESMENU: window.setScene(rulesScene);
                break;
            case REVRULES: window.setScene(revsceneRegels);
                break;
            case TTTRULES: window.setScene(tttsceneRegels);
                break;
            case REVSETTINGS: window.setScene(revsettings);
                break;
            case TTTSETTINGS: window.setScene(tttsettings);
            break;
        }
    }

    public void updateBoard(Tile[][] board){
        //TODO display board
    }

    public void tileClicked(int x, int y){

    }

    public Scene getScene(Scenes scene){
        Scene returnScene = null;
        switch (scene){
            case MAINMENU: returnScene = mainMenuScene;
                break;
            case RULESMENU: returnScene = rulesScene;
                break;
            case REVRULES: returnScene = revsceneRegels;
                break;
            case TTTRULES: returnScene = tttsceneRegels;
                break;
        }
        return returnScene;
    }

    public void closeProgram(){
        boolean result = ConfirmQuit.display("Quit", "Are you sure you want to quit?");
        if(result){
            window.close();
        }
    }
}