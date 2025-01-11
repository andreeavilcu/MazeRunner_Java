package MazeRunner.Interfaces;

import MazeRunner.Models.Player;
import MazeRunner.Models.PowerUp;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class IPlayerTest {

    private Player player;

    @Before
    public void setUp() {
        // Initialize player before each test
        player = new Player();
    }

    @Test
    public void testMove() {
        player.move(5, 10);
        assertEquals(5, player.getRow());
        assertEquals(10, player.getCol());

        player.move(-1, -1); // Invalid move (negative values)
        assertNotEquals(-1, player.getRow());
        assertNotEquals(-1, player.getCol());
    }

    @Test
    public void testResetPlayer() {
        player.move(5, 10); // Change position
        PowerUp healthPowerUp = new PowerUp(0, 0, "Health", 10); // Health power-up
        player.collectPowerUp(healthPowerUp); // Collect power-up
        player.addScore(50); // Add score

        player.resetPlayer(); // Reset player to default state

        // Verify the player has been reset
        assertEquals(0, player.getRow());
        assertEquals(0, player.getCol());
        assertEquals(100, player.getHealth()); // Should reset to 100
        assertEquals(0, player.getScore()); // Score should be reset
        assertFalse(player.hasSpeedBoost()); // No active power-ups
        assertFalse(player.hasShield());
    }

    @Test
    public void testGetRow() {
        player.move(5, 10);
        assertEquals(5, player.getRow());
    }

    @Test
    public void testGetCol() {
        player.move(5, 10);
        assertEquals(10, player.getCol());
    }

    @Test
    public void testGetHealth() {
        player.collectPowerUp(new PowerUp(0, 0, "Health", 10)); // Collect health power-up
        assertEquals(100, player.getHealth()); // Health should not exceed 100
    }

    @Test
    public void testGetScore() {
        player.addScore(100); // Add score
        assertEquals(100, player.getScore());
    }

    @Test
    public void testCollectPowerUp() {
        PowerUp speedPowerUp = new PowerUp(1, 1, "Speed", 5); // 5 seconds speed boost
        player.collectPowerUp(speedPowerUp);
        assertTrue(player.hasSpeedBoost());

        PowerUp shieldPowerUp = new PowerUp(2, 2, "Shield", 5); // Shield power-up
        player.collectPowerUp(shieldPowerUp);
        assertTrue(player.hasShield());
    }

    @Test
    public void testPowerUpTimer() {
        PowerUp speedPowerUp = new PowerUp(1, 1, "Speed", 2); // 2 second duration
        player.collectPowerUp(speedPowerUp);
        assertTrue(player.hasSpeedBoost());

        // Wait 3 seconds for the power-up to expire (duration + buffer)
        try {
            Thread.sleep(3000); // Sleep for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(player.hasSpeedBoost()); // Speed boost should be removed after timer expiration
    }
}
