package pacManUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class FrameLock {
    private JFrame frame;
    private Point originalLocation;
    private ComponentListener movePreventer;
    public FrameLock(JFrame frame) {
        this.frame = frame;
        this.originalLocation = frame.getLocation();
        this.movePreventer = new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                frame.setLocation(originalLocation);
            }
        };

    }
    public void lockMovement() {
        frame.setLocationByPlatform(false);
        frame.addComponentListener(movePreventer);
    }
    public void unlockMovement() {
        frame.removeComponentListener(movePreventer);
    }
}
