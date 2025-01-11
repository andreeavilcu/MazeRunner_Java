package MazeRunner.Models;

import MazeRunner.Interfaces.IMaze;

import java.util.ArrayList;
import java.util.Random;
import java.util.List;

public class Maze implements IMaze {
    private final int rows;
    private final int cols;
    private final boolean[][] walls;
    private final List<PowerUp> powerUps;
    private MazeRunner.Models.Maze.DifficultyLevel difficulty;


    public enum DifficultyLevel {
        EASY(0.2),
        MEDIUM(0.35),
        HARD(0.5);

        private final double wallDensity;

        DifficultyLevel(double wallDensity) {
            this.wallDensity = wallDensity;
        }

        public double getWallDensity() {
            return wallDensity;
        }
    }

    public Maze(int rows, int cols, MazeRunner.Models.Maze.DifficultyLevel difficulty) {
        this.rows = rows;
        this.cols = cols;
        this.difficulty = difficulty;
        this.walls = new boolean[rows][cols];
        this.powerUps = new ArrayList<>();
        generateMaze();
        spawnPowerUps();
    }

    private void generateMaze() {
        Random random = new Random();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walls[i][j] = true;
            }
        }

        int startRow = 0, startCol =0;
        walls[startRow][startCol] = false;

        dfs(startRow,startCol,random);

        ensurePathToExit(startRow, startCol, rows-1, cols -1);

        walls[rows - 1][cols - 1] = false;

        addDifficultyWalls(random);
    }

    private void addDifficultyWalls(Random random) {
        int totalCells = rows * cols;
        int desiredWalls = (int)(totalCells * difficulty.getWallDensity());
        int currentWalls = countWalls();
        int attempts = 0;
        int maxAttempts = totalCells * 2;

        while(currentWalls < desiredWalls && attempts < maxAttempts) {
            attempts++;
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);

            if(!walls[row][col] && !isExit(row, col) && !(row == 0 && col == 0)) {
                walls[row][col] = true;


                if (!hasValidPath(0, 0, rows-1, cols-1)) {
                    walls[row][col] = false;
                } else {
                    currentWalls++;
                }
            }
        }
    }


    private int countWalls(){
        int count = 0;
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(walls[i][j]) count++;
            }
        }
        return count;
    }

    private boolean hasValidPath(int startRow, int startCol, int endRow, int endCol) {
        boolean[][] visited = new boolean[rows][cols];
        return findPathDFS(startRow, startCol, endRow, endCol, visited);
    }

    private boolean findPathDFS(int row, int col, int endRow, int endCol, boolean[][] visited) {
        if (row < 0 || row >= rows || col < 0 || col >= cols || walls[row][col] || visited[row][col]) {
            return false;
        }

        if (row == endRow && col == endCol) {
            return true;
        }

        visited[row][col] = true;

        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (findPathDFS(newRow, newCol, endRow, endCol, visited)) {
                return true;
            }
        }

        return false;
    }

    private void ensurePathToExit(int startRow, int startCol, int endRow, int endCol) {
        int currentRow = startRow;
        int currentCol = startCol;

        while (currentRow != endRow || currentCol != endCol) {
            if (currentRow < endRow) {
                currentRow++;
            } else if (currentCol < endCol) {
                currentCol++;
            }
            walls[currentRow][currentCol] = false;
        }
    }

    private void dfs(int row, int col, Random random) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        shuffleArray(directions, random);

        for (int[] dir : directions) {
            int newRow = row + dir[0] * 2;
            int newCol = col + dir[1] * 2;

            if (isInBounds(newRow, newCol) && walls[newRow][newCol]) {
                walls[row + dir[0]][col + dir[1]] = false;
                walls[newRow][newCol] = false;
                dfs(newRow, newCol, random);
            }
        }
    }


    private void shuffleArray(int[][] array, Random random) {
        for (int i = array.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }


    public void spawnPowerUps(){
        Random random = new Random();
        int maxPowerUps = (rows * cols) / 20;
        String[] types = {"Health", "Speed", "Vision", "Shield"};

        for (int i = 0; i < maxPowerUps; i++) {
            int row, col;
            do {
                row = random.nextInt(rows);
                col = random.nextInt(cols);
            } while (walls[row][col] || isPowerUp(row, col) || isExit(row, col));

            String type = types[random.nextInt(types.length)];
            int duration = random.nextInt(20) + 10;
            powerUps.add(new PowerUp(row, col, type, duration));
        }
    }

    @Override
    public PowerUp getPowerUpAt(int row, int col){
        for(PowerUp p : powerUps){
            if(p.getRow() == row && p.getCol() == col){
                return p;
            }
        }
        return null;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    @Override
    public boolean isValidMove(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols && !walls[row][col];
    }

    @Override
    public boolean isExit(int row, int col) {
        return row == rows - 1 && col == cols - 1;
    }

    @Override
    public boolean isWall(int row, int col) {
        return walls[row][col];
    }

    @Override
    public boolean isPowerUp(int row, int col) {
        for (PowerUp powerUp : powerUps) {
            if (powerUp.getRow() == row && powerUp.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void removePowerUp(PowerUp powerUp){
        powerUps.remove(powerUp);
    }

    @Override
    public void resetMaze(MazeRunner.Models.Maze.DifficultyLevel difficulty) {
        this.difficulty = difficulty;
        generateMaze();
        powerUps.clear();
        spawnPowerUps();
    }

    @Override
    public int getRows() { return rows; }

    @Override
    public int getCols() {return cols; }

}

