package pacManUI;

import Util.ButtonUtil;
import Util.ScreenUtil;

import javax.swing.*;

public class levelScreen {
    private final JFrame screen = new JFrame("Level");
    private final JButton buttonEasy = new JButton("EASY");
    private final JButton buttonMedium = new JButton("MEDIUM");
    private final JButton buttonHard = new JButton("HARD");
    private final JButton buttonBack = new JButton("Back");
    private final MainMenu mainMenu;

    public levelScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu; // Tham chiếu tới Test để quay lại
        setupLevelScreen();
    }

    private void setupLevelScreen() {
        screen.setLayout(null);

        String imagePath = "/image/pac-man-thumb.jpg";
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource(imagePath));
        JLabel background = new JLabel(backgroundImage);
        background.setLayout(null);
        background.setSize(new ScreenUtil().getDimension());

        buttonEasy.setBounds(130, 80, 220, 60);
        new ButtonUtil().checkJbutton(buttonEasy);

        buttonMedium.setBounds(130, 160, 220, 60);
        new ButtonUtil().checkJbutton(buttonMedium);

        buttonHard.setBounds(130, 240, 220, 60);
        new ButtonUtil().checkJbutton(buttonHard);

        buttonBack.setBounds(10, 10, 100, 60);
        new ButtonUtil().checkJbutton(buttonBack);

        buttonBack.addActionListener(e -> {
            screen.setVisible(false); // Ẩn levelScreen
            mainMenu.showMainMenu(); // Hiển thị lại menu
        });

        background.add(buttonEasy);
        background.add(buttonMedium);
        background.add(buttonHard);
        background.add(buttonBack);
        screen.add(background);

        screen.setSize(new ScreenUtil().getDimension());
        screen.setLocationRelativeTo(null);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        screen.setResizable(false);
    }

    public void showScreen() {
        screen.setVisible(true);
        screen.revalidate();
        screen.repaint();
    }
}
