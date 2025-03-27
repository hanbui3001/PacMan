package pacManUI;

import Util.ButtonUtil;
import Util.ScreenUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MainMenu {
    private final JFrame menu = new JFrame();
    private final JButton buttonStart = new JButton("Start");
    private final JButton buttonGuide = new JButton("Guide");
    private final GuideScreen guideScreen = new GuideScreen();
    private final levelScreen levelScreen;

    public MainMenu() {
        levelScreen = new levelScreen(this); // Truyền tham chiếu tới `Test` để quay lại
        setupMainMenu();
    }

    private void setupMainMenu() {
        menu.setLayout(null);
        String imagePath = "/image/pac-man-thumb.jpg";
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource(imagePath));
        JLabel pic = new JLabel(backgroundImage);
        pic.setLayout(null);
        pic.setSize(new ScreenUtil().getDimension());

        buttonStart.setBounds(140, 180, 200, 50);
        new ButtonUtil().checkJbutton(buttonStart);

        buttonGuide.setBounds(140, 250, 200, 50);
        new ButtonUtil().checkJbutton(buttonGuide);

        buttonGuide.addActionListener(e -> guideScreen.showGuide(menu));
        buttonStart.addActionListener(e -> {
            menu.setVisible(false);  // Ẩn menu
            levelScreen.showScreen(); // Hiển thị levelScreen
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(null);
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 0, pic.getWidth(), pic.getHeight());
        buttonPanel.add(buttonStart);
        buttonPanel.add(buttonGuide);

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
