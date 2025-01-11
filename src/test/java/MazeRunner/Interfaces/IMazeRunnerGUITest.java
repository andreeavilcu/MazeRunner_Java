package MazeRunner.Interfaces;

import MazeRunner.Models.MazeRunnerGUI;
import MazeRunner.Models.Player;
import org.junit.Test;

import static org.junit.Assert.*;

public class IMazeRunnerGUITest {

    @Test
    public void startNewGame() {
        MazeRunnerGUI gui = new MazeRunnerGUI(10, 10, MazeRunner.Models.Maze.DifficultyLevel.EASY);
        gui.startNewGame();
        assertNotNull(gui);
    }


    @Test
    public void togglePause() {
        MazeRunnerGUI gui = new MazeRunnerGUI(10, 10, MazeRunner.Models.Maze.DifficultyLevel.EASY);
        assertFalse(gui.getIsPaused());

        gui.togglePause();

        assertTrue(gui.getIsPaused());

        gui.togglePause();

        assertFalse(gui.getIsPaused());
    }


    @Test
    public void updateGameInfo() {
        MazeRunnerGUI gui = new MazeRunnerGUI(10, 10, MazeRunner.Models.Maze.DifficultyLevel.EASY);

        gui.setPlayerName("John");

        Player player = gui.getPlayer();
        player.setScore(50);
        player.setHealth(80);

        gui.updateGameInfo();

        assertEquals("John's Score: 50", gui.getScoreLabel().getText());
        assertEquals("Health: 80", gui.getHealthLabel().getText());
    }

}