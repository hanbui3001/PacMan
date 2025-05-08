package pacManUI;

import javax.swing.*;
import java.awt.*;

public class LivesAndScoreBoard extends JPanel {
    private int scores = 0;
    private int lives = 3;
    private JLabel scoreLabel;
    private JLabel livesLabel;
    private JPanel livePanel;
    private Image pacmanLiveIcon;
    private final int currentLive = 3;
    public LivesAndScoreBoard(){
        this.setBackground(Color.BLACK);
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        pacmanLiveIcon = new ImageIcon(getClass().getResource("/image/pacmanRight.png")).getImage();
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));

        livesLabel = new JLabel();
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // Khởi tạo label và panel cho mạng
        livesLabel = new JLabel("Lives: ");
        livesLabel.setForeground(Color.WHITE);
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));

        livePanel = new JPanel();
        livePanel.setBackground(Color.BLACK);
        livePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        updateLivesLabel();

        this.add(scoreLabel);
        this.add(livesLabel);
        this.add(livePanel);
    }

    public int getScores() {
        return scores;
    }

    public int getLives() {
        return lives;
    }
    public void addScore(int point){
        scores += point;
        scoreLabel.setText("Score: " + scores);
    }
    public boolean loseLife(){
        lives--;
        updateLivesLabel();
        if(lives > 0){
            return true;

        }  else {
            return false;
        }
    }
    public void resetLives(){
        lives = currentLive;
        updateLivesLabel();
    }
    public void resetScores(){
        scores = 0;
        scoreLabel.setText("Score: " + scores);
    }
    public void updateLivesLabel(){
        livePanel.removeAll();
        Image scaleIcon = pacmanLiveIcon.getScaledInstance(20,20, Image.SCALE_SMOOTH);
        for(int i = 0; i < lives; i++){
            JLabel lifeIcon = new JLabel(new ImageIcon(scaleIcon));
            livePanel.add(lifeIcon);
        }
        livePanel.revalidate();
        livePanel.repaint();
    }
}
