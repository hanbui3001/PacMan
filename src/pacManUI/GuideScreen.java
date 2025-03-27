package pacManUI;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class GuideScreen {
    private  JDialog guideDialog;
    private JTextPane guideText;

    public void showGuide(JFrame parentFrame) {
        guideDialog = new JDialog(parentFrame, "Game Guide", true);
        guideDialog.setBounds(200, 300, 250,280);

        guideDialog.setLocationRelativeTo(parentFrame);
        guideDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        guideDialog.setLayout(new BorderLayout());

        guideText = new JTextPane();

        guideText.setText("\n\n           W for go up" +
                          "\n\n           A for go left" +
                          "\n\n           S for go down" +
                          "\n\n           D for go right");
        guideText.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 15));
        guideText.setBackground(Color.BLACK);
        guideText.setForeground(Color.WHITE);
        guideText.setEditable(false);

        guideDialog.add(guideText);

        guideDialog.setVisible(true);
    }
}