package MazeRunner.Models;

import MazeRunner.Interfaces.IPowerUp;

public class PowerUp implements IPowerUp {
    private final int row;
    private final int col;
    private final String type;
    private final int duration;

    public PowerUp(int row, int col, String type, int duration){
        this.row = row;
        this.col = col;
        this.type = type;
        this.duration = duration;
    }

    @Override
    public int getRow(){
        return row;
    }

    @Override
    public int getCol(){
        return col;
    }

    @Override
    public String getType(){
        return type;
    }

    @Override
    public int getDuration() {
        return duration;
    }
}