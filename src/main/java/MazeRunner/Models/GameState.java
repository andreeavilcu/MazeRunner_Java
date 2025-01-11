package MazeRunner.Models;
import javax.swing.*;
import java.util.*;

import MazeRunner.Interfaces.IGameState;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;


public class GameState  implements IGameState {
    private static MazeRunner.Models.GameState instance;
    private int totalScore;
    private String currentPlayerName;
    private List<HighScore> highScores;
    private static final String HIGH_SCORES_FILE = "highscores.json";
    private static final Logger logger = Logger.getLogger(MazeRunner.Models.GameState.class.getName());

    private GameState(){
        totalScore = 0;
        currentPlayerName = "";
        highScores = loadHighScores();
    }

    public static MazeRunner.Models.GameState getInstance(){
        if (instance == null){
            instance = new MazeRunner.Models.GameState();
        }

        return instance;
    }

    public void setPlayerName(String playerName){
        this.currentPlayerName = playerName;
    }

    public void addScore(int points){
        totalScore += points;
    }

    public void saveHighScore(String playerName, int score){
        HighScore newScore = new HighScore(playerName, score);
        highScores.add(newScore);
        Collections.sort(highScores);

        if (highScores.size() > 10) {
            highScores = highScores.subList(0, 10);
        }

        saveHighScoresToFile();
    }

    public void saveCurrentScore() {
        saveHighScore(currentPlayerName, totalScore);
    }

    private List<HighScore> loadHighScores() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            File file = new File(HIGH_SCORES_FILE);
            if(file.exists()){
                return objectMapper.readValue(file,
                        objectMapper.getTypeFactory().constructCollectionType(List.class, HighScore.class));
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            logger.severe("Failed to load high scores: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private void saveHighScoresToFile() {
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(HIGH_SCORES_FILE), highScores);
        } catch (IOException e) {
            logger.severe("Failed to save high scores: " + e.getMessage());
        }
    }


    // În clasa GameState
    public void showHighScores() {
        StringBuilder sb = new StringBuilder("HighScores:\n\n");

        for (int i = 0; i < Math.min(10, highScores.size()); i++) {
            HighScore hs = highScores.get(i);
            sb.append(String.format("%d. %s: %d (Achieved: %s)\n", i + 1, hs.getPlayerName(), hs.getScore(), hs.getDate()));
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "High Scores", JOptionPane.INFORMATION_MESSAGE);
    }


    public String getPlayerName() {
        return currentPlayerName;
    }

    public List<HighScore> getHighScores() {
        return highScores;
    }

    public int getTotalScore() {
        return totalScore;
    }
}