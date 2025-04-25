package pacManUI;

import PacmanGame.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Username extends JDialog {
    private JTextField nameField;
    private JButton submitButton;
    private int score;
    private String difficulty;
    private MainMenu mainMenu;
    private PacmanPlay pacmanPlay;
    private JFrame gameFrame;
    public Username(JFrame parent, int score, String difficulty, MainMenu mainMenu, PacmanPlay pacmanPlay) {
        super(parent, "Enter Your Name", true);
        this.score = score;
        this.difficulty = difficulty;
        this.mainMenu = mainMenu;
        this.pacmanPlay = pacmanPlay;
        this.gameFrame = parent;

        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        JLabel nameLabel = new JLabel("Enter Your Name:");
        nameLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBounds(50, 30, 200, 30);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        nameField.setBounds(50, 70, 200, 30);
        add(nameField);

        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        submitButton.setFocusPainted(false);
        submitButton.setBackground(Color.BLACK);
        submitButton.setForeground(Color.white);
        submitButton.setBorder(new LineBorder(Color.BLUE, 3, true));
        submitButton.setBounds(100, 120, 100, 30);
        submitButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (playerName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (playerName.length() > 10) {
                playerName = playerName.substring(0, 10);
            }
            saveScore(playerName, score, difficulty);
        });
        add(submitButton);

        getContentPane().setBackground(Color.BLACK);
    }

    private void saveScore(String name, int score, String difficulty) {
        ArrayList<PlayerScore> scoresList = new ArrayList<>();
        File file = new File(System.getProperty("user.dir") + "/scores.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                PlayerScore playerScore = PlayerScore.fromString(line);
                if (playerScore != null) {
                    scoresList.add(playerScore);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (PlayerScore playerScore : scoresList) {
            if (playerScore.getName().equalsIgnoreCase(name)) {
                JOptionPane.showMessageDialog(this, "Name already exists! Please choose a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                nameField.setText("");
                return;
            }
        }

        scoresList.add(new PlayerScore(name, score, difficulty));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (PlayerScore playerScore : scoresList) {
                writer.write(playerScore.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error saving score to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        dispose();
        gameFrame.dispose();
        mainMenu.showMainMenu();
    }
}