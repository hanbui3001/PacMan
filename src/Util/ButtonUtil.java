package Util;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ButtonUtil {
    public JButton checkJbutton(JButton jButton){
        //JButton jButton = new JButton();
        jButton.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 28));
        jButton.setFocusPainted(false);
        jButton.setBackground(Color.BLACK);
        jButton.setForeground(Color.white);
        jButton.setBorder(new LineBorder(Color.BLUE, 3, true));
        return jButton;
    }
}
