package pacManUI;

import Util.ScreenUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class GuideScreen {
    private  JFrame guideFrame;
    private JLabel label;
    private JButton backButton = new JButton("Back");
    private MainMenu mainMenu;
    public GuideScreen(MainMenu mainMenu){
        this.mainMenu = mainMenu;
    }
    public void showGuide() {
        guideFrame = new JFrame( "Game Guide");
        guideFrame.setSize(new ScreenUtil().getDimension());

        backButton.setBounds(5,5,80,40);
        backButton.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 20));
        backButton.setFocusPainted(false);
        backButton.setBackground(Color.BLACK);
        backButton.setForeground(Color.white);
        backButton.setBorder(new LineBorder(Color.BLUE, 2, true));
        backButton.addActionListener(e -> {
            mainMenu.showMainMenu();
            guideFrame.setVisible(false);
        });
        guideFrame.setLocationRelativeTo(null);
        guideFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        guideFrame.setLayout(new BorderLayout());
        guideFrame.setResizable(false);
        String imagePath = "/image/pacman-gui.png";
        ImageIcon backgroundImage = new ImageIcon(getClass().getResource(imagePath));
        label = new JLabel(backgroundImage);
        label.add(backButton);
        guideFrame.add(label);
        guideFrame.setVisible(true);
    }
}