package MazeRunner.Interfaces;

import MazeRunner.Models.GameState;
import MazeRunner.Models.HighScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class IGameStateTest {

    private GameState gameState;

    @BeforeEach
    public void setUp() {
        gameState = GameState.getInstance();
    }

    @Test
    public void testSaveHighScore() {
        gameState.setPlayerName("John");
        gameState.addScore(100);
        gameState.saveCurrentScore();

        List<HighScore> highScores = gameState.getHighScores(); // Assuming you add a getter for testing
        assertEquals(1, highScores.size());
        assertEquals("John", highScores.get(0).getPlayerName());
        assertEquals(100, highScores.get(0).getScore());
    }

    @Test
    public void testSaveCurrentScore() {
        gameState.setPlayerName("Jane");
        gameState.addScore(150);
        gameState.saveCurrentScore();

        List<HighScore> highScores = gameState.getHighScores();
        assertEquals(1, highScores.size());
        assertEquals("Jane", highScores.get(0).getPlayerName());
        assertEquals(150, highScores.get(0).getScore());
    }

    @Test
    public void testShowHighScores() {

        gameState.setPlayerName("Mike");
        gameState.addScore(200);
        gameState.saveCurrentScore();

        gameState.setPlayerName("Anna");
        gameState.addScore(250);
        gameState.saveCurrentScore();

        List<HighScore> highScores = gameState.getHighScores();
        assertEquals(2, highScores.size());
        assertEquals("Anna", highScores.get(0).getPlayerName()); // Assuming sorted by score
        assertEquals(250, highScores.get(0).getScore());
        assertEquals("Mike", highScores.get(1).getPlayerName());
        assertEquals(200, highScores.get(1).getScore());
    }

    @Test
    public void testSetPlayerName() {
        gameState.setPlayerName("Alex");
        assertEquals("Alex", gameState.getPlayerName()); // Assuming you add a getter for player name
    }

    @Test
    public void testAddScore() {
        gameState.addScore(50);
        gameState.addScore(30);
        assertEquals(80, gameState.getTotalScore()); // Assuming you add a getter for total score
    }
}
