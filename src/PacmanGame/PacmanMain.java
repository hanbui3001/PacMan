package PacmanGame;

import javax.swing.*;

public class PacmanMain {
    private int rowCount = 21;
    private int colCount = 19;
    private int titleSize = 32;
    private  int boardWidth = colCount * titleSize;
    private int boardHeight = rowCount * titleSize;
    private JFrame frame = new JFrame("Pac Man");
    public PacmanMain(){
        setUpPacman();
    }
    public void setUpPacman(){
        PacmanPlay pacmanPlay = new PacmanPlay();
        frame.add(pacmanPlay);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setFocusableWindowState(true);
        frame.setResizable(false);
        pacmanPlay.requestFocusInWindow();

    }
    public void showGame(){
        frame.requestFocus();
        frame.revalidate();
        frame.setVisible(true);
        PacmanPlay pacmanPlay = (PacmanPlay) frame.getContentPane().getComponent(0);
        pacmanPlay.requestFocusInWindow(); // Đảm bảo tiêu điểm sau khi hiển thị
    }

    public static void main(String[] args) {
        PacmanMain pacmanMain = new PacmanMain();
        pacmanMain.showGame();

    }
}
