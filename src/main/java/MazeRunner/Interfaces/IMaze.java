package MazeRunner.Interfaces;

import MazeRunner.Models.Maze;
import MazeRunner.Models.PowerUp;

public interface IMaze {
    boolean isValidMove (int row , int col );
    boolean isExit (int row , int col );
    boolean isWall (int row , int col );
    boolean isPowerUp (int row , int col );
    PowerUp getPowerUpAt (int row , int col );
    void removePowerUp ( PowerUp powerUp );
    void resetMaze ( Maze.DifficultyLevel difficulty );
    int getRows ();
    int getCols ();
}
