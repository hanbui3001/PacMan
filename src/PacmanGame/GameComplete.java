package PacmanGame;

import Util.ButtonUtil;
import pacManUI.MainMenu;
import pacManUI.Username;

import javax.swing.*;
import java.awt.*;

public class GameComplete extends JFrame {
    private JLabel jLabel;
    private JLabel scoreLabel;
    private JButton retryButton;
    private JButton mainMenuButton;
    private MainMenu mainMenu;
    private PacmanPlay pacmanPlay;
    private JFrame gameFrame;
    private JFrame gameCompleteFrame;
    private int score;
    public GameComplete(MainMenu mainMenu, PacmanPlay pacmanPlay, int score, JFrame gameFrame){
        this.mainMenu = mainMenu;
        this.pacmanPlay = pacmanPlay;
        this.score = score;
        this.gameFrame = gameFrame;
        setUpGameOver();
    }
    public JFrame getFrame(){
        return gameCompleteFrame;
    }
    public void setUpGameOver(){
        String imagePath = "/image/game-complete.png";
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
            this.setVisible(false);
            Username nameInputDialog = new Username(gameFrame, score, pacmanPlay.getDifficulty().toString(), mainMenu, pacmanPlay);
            nameInputDialog.setVisible(true);
        });
        mainMenuButton = new JButton("MENU");
        mainMenuButton.setBounds(130, 250, 200, 50);
        new ButtonUtil().checkJbutton(mainMenuButton);
        mainMenuButton.addActionListener(e -> {
            setVisible(false);
            this.setVisible(false);
            Username nameInputDialog = new Username(gameFrame, score, pacmanPlay.getDifficulty().toString(), mainMenu, pacmanPlay);
            nameInputDialog.setVisible(true);
        });
        jLabel.add(retryButton);
        jLabel.add(mainMenuButton);
        jLabel.add(scoreLabel);
        this.add(jLabel);
        this.setSize(500, 350);
        this.setTitle("Game Complete");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    public void showGame(){
        this.setVisible(true);
        this.revalidate();
        this.repaint();
    }
}
