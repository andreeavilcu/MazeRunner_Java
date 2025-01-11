package MazeRunner.Models;
import MazeRunner.Interfaces.IMazeRunnerGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;

public class MazeRunnerGUI extends JFrame implements IMazeRunnerGUI {
    private String playerName;
    private final MazeRunner.Models.Maze maze;
    private final Player player;
    private final JPanel mazePanel;
    private final JPanel gameInfoPanel;
    private final JLabel timerLabel;
    private final JLabel scoreLabel;
    private final JLabel healthLabel;
    private final JLabel powerUpsLabel;
    private int seconds = 0;
    private Timer gameTimer;
    private final MazeRunner.Models.GameState gameState;
    private boolean isPaused = false;

    public MazeRunnerGUI(int rows, int cols, MazeRunner.Models.Maze.DifficultyLevel difficulty) {
        super("Maze Runner");
        this.gameState = GameState.getInstance();
        this.maze = new MazeRunner.Models.Maze(rows, cols, difficulty);
        this.player = new Player();
        this.mazePanel = new JPanel();
        this.gameInfoPanel = new JPanel();
        this.timerLabel = new JLabel("Time: 0s", JLabel.CENTER);
        this.scoreLabel = new JLabel("Score: 0", JLabel.CENTER);
        this.healthLabel = new JLabel("Health: 100", JLabel.CENTER);
        this.powerUpsLabel = new JLabel("Power-ups: None", JLabel.CENTER);

        setupFrame();
        createMenu();
        setupGameInfoPanel();
        drawMaze();
        addKeyListener(new MazeRunner.Models.MazeRunnerGUI.MazeKeyListener());
        startTimer();
    }

    public void setPlayerName(String playerName){
        this.playerName = playerName;
        gameState.setPlayerName(playerName);
        updateGameInfo();
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // Main layout setup
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Setup maze panel with grid
        mazePanel.setLayout(new GridLayout(maze.getRows(), maze.getCols(), 1, 1));
        mazePanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));

        // Adaugă JScrollPane pentru a face maze-ul scrollabil
        JScrollPane mazeScrollPane = new JScrollPane(mazePanel);
        mazeScrollPane.setPreferredSize(new Dimension(maze.getCols() * 50, maze.getRows() * 50));
        // Ajustează dimensiunea celulelor

        mainPanel.add(gameInfoPanel, BorderLayout.NORTH);
        mainPanel.add(mazePanel, BorderLayout.CENTER);

        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem pause = new JMenuItem("Pause");
        JMenuItem highScores = new JMenuItem("High Scores");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(e -> startNewGame());
        pause.addActionListener(e -> togglePause());
        highScores.addActionListener(e -> showHighScores());
        exit.addActionListener(e -> System.exit(0));

        gameMenu.add(newGame);
        gameMenu.add(pause);
        gameMenu.addSeparator();
        gameMenu.add(highScores);
        gameMenu.addSeparator();
        gameMenu.add(exit);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem instructions = new JMenuItem("How to Play");
        JMenuItem about = new JMenuItem("About");

        instructions.addActionListener(e -> showInstructions());
        about.addActionListener(e -> showAbout());

        helpMenu.add(instructions);
        helpMenu.add(about);

        menuBar.add(gameMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private void setupGameInfoPanel() {
        gameInfoPanel.setLayout(new GridLayout(1, 4, 10, 0));

        // Style the labels
        Font infoFont = new Font("Arial", Font.BOLD, 14);
        timerLabel.setFont(infoFont);
        scoreLabel.setFont(infoFont);
        healthLabel.setFont(infoFont);
        powerUpsLabel.setFont(infoFont);

        // Add backgrounds and borders
        Component[] labels = {timerLabel, scoreLabel, healthLabel, powerUpsLabel};
        for (Component label : labels) {
            ((JLabel) label).setOpaque(true);
            ((JLabel) label).setBackground(new Color(240, 240, 240));
            ((JLabel) label).setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    new EmptyBorder(5, 5, 5, 5)
            ));
        }

        gameInfoPanel.add(timerLabel);
        gameInfoPanel.add(scoreLabel);
        gameInfoPanel.add(healthLabel);
        gameInfoPanel.add(powerUpsLabel);
    }

    private void drawMaze() {
        mazePanel.removeAll();

        for (int i = 0; i < maze.getRows(); i++) {
            for (int j = 0; j < maze.getCols(); j++) {
                JLabel cell = createCell(i, j);
                mazePanel.add(cell);
            }
        }

        mazePanel.revalidate();
        mazePanel.repaint();
        updateGameInfo();
    }

    private JLabel createCell(int row, int col) {
        JLabel cell = new JLabel();
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setVerticalAlignment(SwingConstants.CENTER);
        cell.setOpaque(true);
        //cell.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        if (row == player.getRow() && col == player.getCol()) {
            cell.setText("😀");
            cell.setBackground(Color.WHITE);
        } else if (maze.isExit(row, col)) {
            cell.setText("🚪");
            cell.setBackground(new Color(255, 220, 220));
        } else if (maze.isWall(row, col)) {
            cell.setBackground(Color.BLACK);
        } else if (maze.isPowerUp(row, col)) {
            PowerUp powerUp = maze.getPowerUpAt(row, col);
            cell.setText(getPowerUpSymbol(powerUp.getType()));
            cell.setBackground(new Color(220, 255, 220));
        } else {
            cell.setBackground(Color.WHITE);
        }

        return cell;
    }

    private String getPowerUpSymbol(String type) {
        switch (type) {
            case "Health":
                return "❤️";
            case "Speed":
                return "⚡";
            case "Shield":
                return "🛡️";
            default:
                return "💎";
        }
    }

    @Override
    public void updateGameInfo() {
        scoreLabel.setText(playerName + "'s Score: " + player.getScore());
        healthLabel.setText("Health: " + player.getHealth());
        updatePowerUpsLabel();
    }



    private void updatePowerUpsLabel() {
        StringBuilder powerUps = new StringBuilder("Active: ");
        if (player.hasSpeedBoost()) powerUps.append("Speed ");
        if (player.hasShield()) powerUps.append("Shield ");
        powerUpsLabel.setText(powerUps.toString());
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            if (!isPaused) {
                seconds++;
                timerLabel.setText("Time: " + seconds + "s");
            }
        });
        gameTimer.start();
    }

    @Override
    public void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            showPauseDialog();
        }
    }

    private void showPauseDialog() {
        Object[] options = {"Resume", "Quit"};
        int choice = JOptionPane.showOptionDialog(this,
                "Game Paused\nChoose an action:",
                "Paused",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            isPaused = false;  // Reluăm jocul
        } else if (choice == 1) {
            System.exit(0);  // Ieșim din joc
        }
    }

    private void showHighScores() {
        gameState.showHighScores();
    }

    private void showInstructions() {
        JOptionPane.showMessageDialog(this,
                "How to Play Maze Runner:\n\n" +
                        "- Use arrow keys to move\n" +
                        "- Collect power-ups for bonuses:\n" +
                        "  ❤️ Health: Restores health\n" +
                        "  ⚡ Speed: Temporary speed boost\n" +
                        "  🛡️ Shield: Temporary protection\n" +
                        "- Reach the exit (🚪) to complete the level\n" +
                        "- Avoid walls and manage your time\n\n" +
                        "Good luck!",
                "How to Play",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,
                "Maze Runner v1.0\n" +
                        "A fun maze game with power-ups and challenges!\n\n" +
                        "Created with ❤️ by Andreea ",
                "About Maze Runner",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public Player getPlayer() {
        return player;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public JLabel getHealthLabel() {
        return healthLabel;
    }

    public boolean getIsPaused(){
        return isPaused;
    }

    private class MazeKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            if (isPaused) return;

            int newRow = player.getRow();
            int newCol = player.getCol();

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    newRow--;
                    break;
                case KeyEvent.VK_DOWN:
                    newRow++;
                    break;
                case KeyEvent.VK_LEFT:
                    newCol--;
                    break;
                case KeyEvent.VK_RIGHT:
                    newCol++;
                    break;
                case KeyEvent.VK_ESCAPE:
                    togglePause();
                    return;
            }

            handleMovement(newRow, newCol);
        }

        private void handleMovement(int newRow, int newCol) {
            if (maze.isValidMove(newRow, newCol)) {
                if (player.hasSpeedBoost()) {
                    // Allow double movement with speed boost
                    int deltaRow = newRow - player.getRow();
                    int deltaCol = newCol - player.getCol();
                    if (maze.isValidMove(newRow + deltaRow, newCol + deltaCol)) {
                        newRow += deltaRow;
                        newCol += deltaCol;
                    }
                }

                player.move(newRow, newCol);

                if (maze.isExit(newRow, newCol)) {
                    handleLevelComplete();
                } else if (maze.isPowerUp(newRow, newCol)) {
                    handlePowerUpCollection(newRow, newCol);
                }

                drawMaze();
            }
        }


        public void handleLevelComplete() {
            gameTimer.stop();
            int timeBonus = Math.max(0, 1000 - seconds * 10);
            int finalScore = player.getScore() + timeBonus;

            // Arată mesajul de felicitare și scorul final
            JOptionPane.showMessageDialog(null,
                    "Level Complete!\n\n" +
                            "Time: " + seconds + " seconds\n" +
                            "Time Bonus: " + timeBonus + "\n" +
                            "Final Score: " + finalScore,
                    "Congratulations!",
                    JOptionPane.INFORMATION_MESSAGE);

            // Adăuga scorul la clasament și salvează
            gameState.addScore(finalScore);
            gameState.saveCurrentScore();

            // Permite utilizatorului să înceapă un nou joc sau să părăsească
            int choice = JOptionPane.showConfirmDialog(MazeRunner.Models.MazeRunnerGUI.this, // Folosește instanța corectă
                    "Would you like to play again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {
                startNewGame(); // Începe un nou joc
            } else {
                // Poți adăuga logica de ieșire fără a închide fereastra
                JOptionPane.showMessageDialog(MazeRunner.Models.MazeRunnerGUI.this,
                        "Thanks for playing!\n" +
                                "Your score has been saved.",
                        "Goodbye",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }


        private void handlePowerUpCollection(int row, int col) {
            PowerUp powerUp = maze.getPowerUpAt(row, col);
            if (powerUp != null) {
                player.collectPowerUp(powerUp);
                maze.removePowerUp(powerUp);

                String message;
                switch (powerUp.getType())
                {
                    case "Health":
                        message = "Health restored!";
                        break;
                    case "Speed":
                        message = "Speed boost activated!";
                        break;
                    case "Shield":
                        message = "Shield activated!";
                        break;
                    default:
                        message = "Power-up collected!";
                        break;
                }

                showToast(message);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}
    }

    private void showToast(String message) {
        JLabel toast = new JLabel(message);
        toast.setFont(new Font("Arial", Font.BOLD, 16));
        toast.setForeground(Color.WHITE);
        toast.setBackground(new Color(0, 0, 0, 200));
        toast.setOpaque(true);
        toast.setBorder(new EmptyBorder(10, 20, 10, 20));

        JPanel toastPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        toastPanel.setOpaque(false);
        toastPanel.add(toast);

        JWindow toastWindow = new JWindow(this);
        toastWindow.setContentPane(toastPanel);
        toastWindow.pack();
        toastWindow.setLocationRelativeTo(this);

        Timer toastTimer = new Timer(2000, e -> toastWindow.dispose());
        toastTimer.setRepeats(false);

        toastWindow.setVisible(true);
        toastTimer.start();
    }

    @Override
    public void startNewGame() {
        // Creăm opțiunile pentru selecția dificultății
        String[] difficulties = {"Easy", "Medium", "Hard"};
        String selectedDifficulty = (String) JOptionPane.showInputDialog(
                this,
                "Select the difficulty for the new game:",
                "New Game Difficulty",
                JOptionPane.PLAIN_MESSAGE,
                null,
                difficulties,
                difficulties[0] // Opțiunea implicită
        );

        // Dacă utilizatorul nu alege nimic sau închide dialogul, ieșim din funcție
        if (selectedDifficulty == null) return;

        // Convertim selecția la un nivel de dificultate
        MazeRunner.Models.Maze.DifficultyLevel difficulty;
        switch (selectedDifficulty) {
            case "Medium":
                difficulty = MazeRunner.Models.Maze.DifficultyLevel.MEDIUM;
                break;
            case "Hard":
                difficulty = MazeRunner.Models.Maze.DifficultyLevel.HARD;
                break;
            default:
                difficulty = Maze.DifficultyLevel.EASY;
        }

        // Confirmăm că jocul curent va fi resetat
        int choice = JOptionPane.showConfirmDialog(
                this,
                "Start a new game? Current progress will be lost.",
                "New Game",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            // Resetează labirintul cu noua dificultate
            maze.resetMaze(difficulty); // Asigură-te că metoda acceptă un nivel de dificultate
            player.resetPlayer();
            seconds = 0;
            gameTimer.restart();
            drawMaze();
            updateGameInfo();
        }
    }

}