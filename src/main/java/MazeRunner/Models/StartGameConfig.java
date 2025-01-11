package MazeRunner.Models;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.*;

public class StartGameConfig extends JDialog{
    private String playerName = "";
    private MazeRunner.Models.Maze.DifficultyLevel selectedDifficulty ;
    private boolean startGame = false;
    private JTextField nameField;
    private JComboBox<MazeRunner.Models.Maze.DifficultyLevel> difficultyCombo;

    public StartGameConfig(Frame parent){
        super(parent, "Maze Runner - New Game", true);
        setupDialog();
    }

    private void setupDialog(){
        setLayout(new BorderLayout(10,10));

        JPanel titlePanel = createTitlePanel();

        JPanel configPanel = createConfigPanel();

        JPanel buttonPanel = createButtonPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(configPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(600, 700));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private JPanel createTitlePanel(){
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(51, 153, 255));
        JLabel titleLabel = new JLabel("Welcome to Maze Runner!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        titlePanel.setBorder(new EmptyBorder(15, 0, 15, 0));
        return titlePanel;
    }

    private JPanel createConfigPanel(){
        JPanel configPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        configPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 10, 5);
        JLabel nameLabel = new JLabel("Player Name:");
        configPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        nameField = new JTextField(15);
        configPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel difficultyLabel = new JLabel("Difficulty:");
        configPanel.add(difficultyLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        difficultyCombo = new JComboBox<>(MazeRunner.Models.Maze.DifficultyLevel.values());
        difficultyCombo.setSelectedItem(selectedDifficulty);
        configPanel.add(difficultyCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 0, 0);
        JTextArea difficultyDescription = new JTextArea(
                "EASY: 20% walls - Perfect for beginners\n" +
                        "MEDIUM: 35% walls - Balanced challenge\n" +
                        "HARD: 50% walls - For experienced players"
        );
        difficultyDescription.setEditable(false);
        difficultyDescription.setBackground(new Color(240, 240, 240));
        difficultyDescription.setFont(new Font("Arial", Font.ITALIC, 12));
        configPanel.add(difficultyDescription, gbc);

        return configPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JButton startButton = new JButton("Start Game");
        JButton exitButton = new JButton("Exit");

        startButton.setPreferredSize(new Dimension(100, 30));
        exitButton.setPreferredSize(new Dimension(100, 30));

        startButton.addActionListener(e -> validateAndStart());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(startButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    public void validateAndStart() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter your name!",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        selectedDifficulty = (MazeRunner.Models.Maze.DifficultyLevel) difficultyCombo.getSelectedItem();

        if (selectedDifficulty == null) {
            JOptionPane.showMessageDialog(this,
                    "Please select a difficulty level!",
                    "Input Required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        playerName = name;
        startGame = true;
        dispose();
    }


    public boolean shouldStartGame() {
        return startGame;
    }

    public String getPlayerName() {
        return playerName;
    }

    public MazeRunner.Models.Maze.DifficultyLevel getSelectedDifficulty() {
        return selectedDifficulty;
    }

    public JTextField getNameField() {
        return nameField;
    }

    public JComboBox<Maze.DifficultyLevel> getDifficultyCombo() {
        return difficultyCombo;
    }
}
