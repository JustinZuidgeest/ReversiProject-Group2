package Games;

public interface Controller extends Runnable{
    void newGame();
    void run();
    void setPlayerOne(Tile tile);
    boolean playerMove(int x, int y);
    void aiMove();
    void hasWin();
    int getBoardSize();
    void killThread();
}
