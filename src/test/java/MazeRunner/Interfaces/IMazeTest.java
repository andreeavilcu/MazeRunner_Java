package MazeRunner.Interfaces;

import MazeRunner.Models.Maze;
import MazeRunner.Models.PowerUp;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class IMazeTest {

    private Maze maze;

    @Before
    public void setUp() {
        maze = new Maze(5, 5, Maze.DifficultyLevel.MEDIUM);
    }

    @Test
    public void isValidMove() {
        assertTrue(maze.isValidMove(1, 1));
        assertFalse(maze.isValidMove(0, 0));
    }

    @Test
    public void isExit() {
        assertTrue(maze.isExit(4, 4));
        assertFalse(maze.isExit(0, 0));
    }

    @Test
    public void isWall() {
        assertTrue(maze.isWall(0, 0));
        assertFalse(maze.isWall(1, 1));
    }

    @Test
    public void isPowerUp() {
        maze.spawnPowerUps();
        assertTrue(maze.isPowerUp(1, 1));
        assertFalse(maze.isPowerUp(0, 0));
    }

    @Test
    public void getPowerUpAt() {

        maze.spawnPowerUps();
        PowerUp powerUp = maze.getPowerUpAt(1, 1);
        assertNotNull(powerUp);
        assertEquals("Health", powerUp.getType());
    }

    @Test
    public void removePowerUp() {
        maze.spawnPowerUps();
        PowerUp powerUp = maze.getPowerUpAt(1, 1);
        maze.removePowerUp(powerUp);
        assertNull(maze.getPowerUpAt(1, 1));
    }

    @Test
    public void resetMaze() {
        int initialRows = maze.getRows();
        int initialCols = maze.getCols();
        maze.resetMaze(Maze.DifficultyLevel.HARD);
        assertEquals(initialRows, maze.getRows());
        assertEquals(initialCols, maze.getCols());
    }

    @Test
    public void getRows() {
        assertEquals(5, maze.getRows());
    }

    @Test
    public void getCols() {
        assertEquals(5, maze.getCols());
    }
}
