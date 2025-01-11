package MazeRunner.Interfaces;

public interface IPlayer {
    void move ( int newRow , int newCol );
    void resetPlayer ();
    int getRow ();
    int getCol ();
    int getHealth ();
    int getScore ();
}
