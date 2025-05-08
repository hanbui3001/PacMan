package pacManUI;

import PacmanGame.PacmanPlay;
import Util.ButtonUtil;
import Util.ScreenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu {
    private final JFrame menu = new JFrame();
    private final JButton buttonStart = new JButton("Start");
    private final JButton buttonGuide = new JButton("Guide");
    private final JButton buttonLeader = new JButton("Ranking");
    private final JButton buttonExit = new JButton("Exit");
    private final GuideScreen guideScreen;
    private final levelScreen levelScreen;

    public MainMenu() {
        levelScreen = new levelScreen(this);
        guideScreen = new GuideScreen(this);
        setupMainMenu();
    }

    private void setupMainMenu() {
        menu.setLayout(null);
        String imagePath = "/image/pacman-thumb1.jpg";
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource(imagePath));
        JLabel pic = new JLabel(backgroundImage);
        pic.setLayout(null);
        pic.setSize(new ScreenUtil().getDimension());

        buttonStart.setBounds(220, 220, 180, 50);
        new ButtonUtil().checkJbutton(buttonStart);

        buttonGuide.setBounds(20, 220, 180, 50);
        new ButtonUtil().checkJbutton(buttonGuide);

        buttonGuide.addActionListener(e -> {
            menu.setVisible(false);
            guideScreen.showGuide();
        });
        buttonStart.addActionListener(e -> {
            menu.setVisible(false);
            levelScreen.showScreen();
        });
        buttonLeader.setBounds(220, 280, 180, 50);
        new ButtonUtil().checkJbutton(buttonLeader);
        buttonExit.setBounds(20, 280, 180, 50);
        new ButtonUtil().checkJbutton(buttonExit);
        buttonExit.addActionListener(e -> {
            menu.dispose();
            System.exit(0);
        });
        buttonLeader.addActionListener(e -> {
            menu.setVisible(false);
            JFrame leaderFrame = new JFrame("Leaderboard");
            leaderFrame.setSize(608, 576);
            leaderFrame.setLocationRelativeTo(null);
            leaderFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            leaderFrame.setResizable(false);
            LeaderBoardScreen leaderboardScreen = new LeaderBoardScreen(this, null, leaderFrame);
            leaderFrame.add(leaderboardScreen);
            leaderFrame.setVisible(true);
        });
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 0, pic.getWidth(), pic.getHeight());
        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonGuide);
        buttonPanel.add(buttonLeader);
        buttonPanel.add(buttonExit);

        pic.add(buttonPanel);
        menu.add(pic);


        menu.setTitle("Pacman");
        menu.setSize(new ScreenUtil().getDimension());
        menu.setLocationRelativeTo(null);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menu.setResizable(false);
    }

    public void showMainMenu() {
        menu.setVisible(true);
        menu.revalidate();
        menu.repaint();
    }

    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.showMainMenu();

    }
}