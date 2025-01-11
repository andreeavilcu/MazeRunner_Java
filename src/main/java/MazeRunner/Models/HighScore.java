package MazeRunner.Models;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HighScore implements Comparable<MazeRunner.Models.HighScore>, Serializable {
    private final String playerName;
    private final int score;
    private final String date;

    // Default constructor for Jackson
    public HighScore() {
        this.playerName = "";
        this.score = 0;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public HighScore(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public int compareTo(MazeRunner.Models.HighScore other) {
        return Integer.compare(other.score, this.score);
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public String getDate() {
        return date;
    }
}