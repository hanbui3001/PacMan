package PacmanGame;

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
    private PacmanPlay pacmanPlay; // Lưu tham chiếu đến PacmanPlay
    private LivesAndScoreBoard livesAndScoreBoard;
    public PacmanMain() {
        setUpPacman();
    }

    public void setUpPacman() {
        livesAndScoreBoard = new LivesAndScoreBoard();
        pacmanPlay = new PacmanPlay(livesAndScoreBoard); // Khởi tạo và lưu tham chiếu
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        pacmanPlay.requestFocusInWindow();
    }

    public void showGame() {
        frame.setVisible(true);
        frame.revalidate();
        pacmanPlay.requestFocusInWindow();
    }

    public static void main(String[] args) {
        PacmanMain pacmanMain = new PacmanMain();
        pacmanMain.showGame();
    }
}