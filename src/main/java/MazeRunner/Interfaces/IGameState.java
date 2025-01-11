package MazeRunner.Interfaces;

public interface IGameState {
    void saveHighScore ( String playerName , int score );
    void saveCurrentScore ();
    void showHighScores ();
    void setPlayerName ( String playerName );
    void addScore ( int points );
}
