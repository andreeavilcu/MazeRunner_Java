package MazeRunner.Models;

import MazeRunner.Interfaces.IPlayer;

import javax.swing.Timer;
import java.util.*;

public class Player implements IPlayer {
    private int row;
    private int col;
    private int health;
    private int score;
    private boolean hasSpeedBoost;
    private boolean hasShield;
    private final Map<String, Timer> powerUpTimers;


    public Player() {
        this.row = 0;
        this.col = 0;
        this.health = 100;
        this.score = 0;
        this.powerUpTimers = new HashMap<>();
    }

    public void collectPowerUp(PowerUp powerUp) {
        score += 50;

        if (powerUpTimers.containsKey(powerUp.getType())) {
            powerUpTimers.get(powerUp.getType()).stop();
        }
        applyPowerUpEffect(powerUp);

        Timer timer = new Timer(powerUp.getDuration() * 1000, e -> {
            removePowerUpEffect(powerUp.getType());
            powerUpTimers.remove(powerUp.getType());
        });
        timer.setRepeats(false);
        timer.start();

        powerUpTimers.put(powerUp.getType(), timer);
    }

    private void applyPowerUpEffect(PowerUp powerUp) {
        switch (powerUp.getType()) {
            case "Health":
                this.health = Math.min(100, this.health + 20);
                break;
            case "Speed":
                this.hasSpeedBoost = true;
                break;
            case "Shield":
                this.hasShield = true;
                break;
        }
    }

    private void removePowerUpEffect(String powerUpType) {
        switch (powerUpType) {
            case "Speed":
                this.hasSpeedBoost = false;
                break;
            case "Shield":
                this.hasShield = false;
                break;
        }
    }

    @Override
    public void move(int newRow, int newCol){
        if (newRow >= 0 && newCol >= 0) {
            this.row = newRow;
            this.col = newCol;
        }
    }


    @Override
    public void resetPlayer() {
        this.row = 0;
        this.col = 0;
        this.health = 100;
        this.score = 0;

        this.hasSpeedBoost = false;
        this.hasShield = false;

        for (Timer timer : powerUpTimers.values()) {
            timer.stop();
        }

        powerUpTimers.clear();
    }

    @Override
    public int getRow(){ return row; }

    @Override
    public int getCol(){ return col; }

    @Override
    public int getHealth() { return health; }

    @Override
    public int getScore() { return score; }

    public boolean hasSpeedBoost() { return hasSpeedBoost; }
    public boolean hasShield() { return hasShield; }



    public void addScore(int score) {
        this.score +=score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setHealth(int health) {
        this.health=health;
    }
}