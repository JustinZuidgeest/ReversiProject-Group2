package Games;

public interface Controller {
    void newGame();
    void start();
    void setPlayerOne(Tile tile);
    boolean playerMove(int x, int y);
    void aiMove();
    void hasWin();
    int getBoardSize();
}
