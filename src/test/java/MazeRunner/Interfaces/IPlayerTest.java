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
        player = new Player();
    }

    @Test
    public void testMove() {
        player.move(5, 10);
        assertEquals(5, player.getRow());
        assertEquals(10, player.getCol());

        player.move(-1, -1);
        assertNotEquals(-1, player.getRow());
        assertNotEquals(-1, player.getCol());
    }

    @Test
    public void testResetPlayer() {
        player.move(5, 10);
        PowerUp healthPowerUp = new PowerUp(0, 0, "Health", 10);
        player.collectPowerUp(healthPowerUp);
        player.addScore(50);

        player.resetPlayer();


        assertEquals(0, player.getRow());
        assertEquals(0, player.getCol());
        assertEquals(100, player.getHealth());
        assertEquals(0, player.getScore());
        assertFalse(player.hasSpeedBoost());
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
        player.collectPowerUp(new PowerUp(0, 0, "Health", 10));
        assertEquals(100, player.getHealth());
    }

    @Test
    public void testGetScore() {
        player.addScore(100);
        assertEquals(100, player.getScore());
    }

    @Test
    public void testCollectPowerUp() {
        PowerUp speedPowerUp = new PowerUp(1, 1, "Speed", 5);
        player.collectPowerUp(speedPowerUp);
        assertTrue(player.hasSpeedBoost());

        PowerUp shieldPowerUp = new PowerUp(2, 2, "Shield", 5);
        player.collectPowerUp(shieldPowerUp);
        assertTrue(player.hasShield());
    }

    @Test
    public void testPowerUpTimer() {
        PowerUp speedPowerUp = new PowerUp(1, 1, "Speed", 2);
        player.collectPowerUp(speedPowerUp);
        assertTrue(player.hasSpeedBoost());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(player.hasSpeedBoost());
    }
}
