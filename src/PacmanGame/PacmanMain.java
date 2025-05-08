package PacmanGame;

import pacManUI.LivesAndScoreBoard;
import pacManUI.MainMenu;

import javax.swing.*;
import java.awt.*;

public class PacmanMain {
    private int rowCount = 18;
    private int colCount = 19;
    private int tileSize = 32;
    private int boardWidth = colCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    private JFrame frame = new JFrame("Pac Man");
    private JPanel gamePanel = new JPanel();
    private JPanel scorePanel = new JPanel();
    private PacmanPlay pacmanPlay;
    private LivesAndScoreBoard livesAndScoreBoard;
    private MainMenu mainMenu;
    private Difficulty difficulty;
    public PacmanMain(MainMenu mainMenu, String difficultyStr) {
        this.mainMenu = mainMenu;
        switch (difficultyStr){
            case "EASY" :
                this.difficulty = difficulty.EASY;
                break;
            case "MEDIUM" :
                this.difficulty = difficulty.MEDIUM;
                break;
            case "HARD" :
                this.difficulty = difficulty.HARD;
                break;
            default:
                this.difficulty = difficulty.EASY;
        }
        setUpPacman();
    }

    public void setUpPacman() {
        livesAndScoreBoard = new LivesAndScoreBoard();
        pacmanPlay = new PacmanPlay(livesAndScoreBoard, mainMenu, frame, difficulty);
        gamePanel.setPreferredSize(new Dimension(boardWidth, boardHeight));
        gamePanel.setLayout(new BorderLayout());
        gamePanel.add(pacmanPlay, BorderLayout.CENTER);

        scorePanel.setPreferredSize(new Dimension(boardWidth, 40));
        scorePanel.setBackground(Color.BLACK);
        scorePanel.setLayout(new FlowLayout());
        scorePanel.add(livesAndScoreBoard);

        frame.setLayout(new BorderLayout());
        frame.add(gamePanel, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                mainMenu.showMainMenu();
            }
        });
        pacmanPlay.requestFocusInWindow();

    }

    public void showGame() {
        frame.setVisible(true);
        frame.revalidate();
        pacmanPlay.requestFocusInWindow();
    }
}