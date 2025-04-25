package PacmanGame;

import Util.ButtonUtil;
import Util.ScreenUtil;
import pacManUI.MainMenu;
import pacManUI.Username;

import javax.swing.*;
import java.awt.*;

public class GameOver extends JFrame{
    private JLabel jLabel;
    private JLabel scoreLabel;
    private JButton retryButton;
    private JButton mainMenuButton;
    private MainMenu mainMenu;
    private PacmanPlay pacmanPlay;
    private JFrame gameFrame;
    private JFrame gameOverFrame;
    private int score;

    public GameOver(MainMenu mainMenu, PacmanPlay pacmanPlay, int score, JFrame gameFrame){
        this.mainMenu = mainMenu;
        this.pacmanPlay = pacmanPlay;
        this.score = score;
        this.gameFrame = gameFrame;
        setUpGameOver();
    }
    public JFrame getFrame() {
        return gameOverFrame;
    }
    public void setUpGameOver(){
        String imagePath = "/image/game-over.png";
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource(imagePath));
        jLabel = new JLabel(backgroundImage);
        jLabel.setLayout(null);
        jLabel.setSize(backgroundImage.getIconWidth(), backgroundImage.getIconHeight());

        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 35));
        scoreLabel.setBounds(130, 100, 300, 100);

        retryButton = new JButton("RETRY");
        retryButton.setBounds(130, 180, 200, 50);
        new ButtonUtil().checkJbutton(retryButton);
        retryButton.addActionListener(e -> {
            setVisible(false);
            pacmanPlay.resetGame();

        });

        mainMenuButton = new JButton("MENU");
        mainMenuButton.setBounds(130, 250, 200, 50);
        new ButtonUtil().checkJbutton(mainMenuButton);
        mainMenuButton.addActionListener(e -> {
            this.setVisible(false);
            Username nameInputDialog = new Username(gameFrame, score, pacmanPlay.getDifficulty().toString(), mainMenu, pacmanPlay);
            nameInputDialog.setVisible(true);
        });

        jLabel.add(retryButton);
        jLabel.add(mainMenuButton);
        jLabel.add(scoreLabel);
        this.add(jLabel);
        this.setSize(500, 350);
        this.setTitle("Game Over");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public void showGameOver(){
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }

//    public static void main(String[] args) {
//        int score = 250000;
//       GameOver gameOver = new GameOver(score);
//       gameOver.showGameOver();
//
//    }

}

