package MazeRunner;
import MazeRunner.Models.*;

public class Main {
    public static void main(String[] args) {
        StartGameConfig startDialog = new StartGameConfig(null);
        startDialog.setVisible(true);

        if(startDialog.shouldStartGame()){
            String playerName = startDialog.getPlayerName();
            Maze.DifficultyLevel difficulty = startDialog.getSelectedDifficulty();

            MazeRunnerGUI game = new MazeRunnerGUI(20, 20,difficulty);
            game.setPlayerName(playerName);
        }
    }
}