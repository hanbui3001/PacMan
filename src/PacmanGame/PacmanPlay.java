package PacmanGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class PacmanPlay extends JPanel implements ActionListener, KeyListener {
    private int rowCount = 21;
    private int colCount = 19;
    private int tileSize = 32;
    private  int boardWidth = colCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    private Block block  = new Block(); ;
    HashSet<Block> walls = new HashSet<Block>();
    HashSet<Block> ghosts = new HashSet<Block>();
    HashSet<Block> foods = new HashSet<Block>();
    Block pacMan;
    PacmanMap tileMaps = new PacmanMap();
    Timer gameLoop;



    JFrame frame = new JFrame("Pac Man");
    private Image wallIcon;
    private Image blueGhostIcon;
    private Image orangeGhostIcon;
    private Image pinkGhostIcon;
    private Image redGhostIcon;

    private Image pacmanDownIcon;
    private Image pacmanUpIcon;
    private Image pacmanLeftIcon;
    private Image pacmanRightIcon;
    public PacmanPlay() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        loadImage();
        loadMap();
        gameLoop = new Timer(50, this);
        gameLoop.start();
    }

    public Block getPacMan() {
        return pacMan;
    }

    public void setPacMan(Block pacMan) {
        this.pacMan = pacMan;
    }

    public void loadImage(){
       wallIcon = new ImageIcon(getClass().getResource("/image/wall.png")).getImage();
       blueGhostIcon = new ImageIcon(getClass().getResource("/image/blueGhost.png")).getImage();
       orangeGhostIcon = new ImageIcon(getClass().getResource("/image/orangeGhost.png")).getImage();
       pinkGhostIcon = new ImageIcon(getClass().getResource("/image/pinkGhost.png")).getImage();
       redGhostIcon = new ImageIcon(getClass().getResource("/image/redGhost.png")).getImage();

       pacmanDownIcon = new ImageIcon(getClass().getResource("/image/pacmanDown.png")).getImage();
       pacmanUpIcon = new ImageIcon(getClass().getResource("/image/pacmanUp.png")).getImage();
       pacmanLeftIcon = new ImageIcon(getClass().getResource("/image/pacmanLeft.png")).getImage();
       pacmanRightIcon = new ImageIcon(getClass().getResource("/image/pacmanRight.png")).getImage();
    }
    public void loadMap() {
        Random random = new Random();
        String[] selectedMap = tileMaps.getTileMaps();
        parseMap(selectedMap);
    }
    public void parseMap(String[] tileMap){
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                String row = tileMap[r];
                char tileMapChar = row.charAt(c);

                int x = c*tileSize;
                int y = r*tileSize;

                if (tileMapChar == 'X') { //block wall
                    Block wall = new Block( x, y, tileSize, tileSize, wallIcon);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') { //blue ghost
                    Block ghost = new Block( x, y, tileSize, tileSize, blueGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') { //orange ghost
                    Block ghost = new Block(x, y, tileSize, tileSize, orangeGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') { //pink ghost
                    Block ghost = new Block( x, y, tileSize, tileSize, pinkGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') { //red ghost
                    Block ghost = new Block( x, y, tileSize, tileSize, redGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') { //pacman
                    pacMan = new Block( x, y, tileSize, tileSize, pacmanRightIcon);
                }
                else if (tileMapChar == ' ') { //food
                    Block food = new Block( x + 14, y + 14, 4, 4, null);
                    foods.add(food);
                }
            }
        }
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }
    public void draw(Graphics g){
        g.drawImage(pacMan.getImage(), pacMan.getX(),pacMan.getY(), pacMan.getWidth(), pacMan.getHeight(), null);
        for(Block ghost : ghosts){
            g.drawImage(ghost.getImage(), ghost.getX(), ghost.getY(), ghost.getWidth(), ghost.getHeight(), null);
        }
        for(Block wall : walls){
            g.drawImage(wall.getImage(), wall.getX(), wall.getY(), wall.getWidth(), wall.getHeight(), null);
        }
        g.setColor(Color.WHITE);
        for(Block food : foods){
            g.fillRect(food.getX(), food.getY(), food.getWidth(), food.getHeight());
        }
    }
    void pacmanMove() {
        pacMan.setX(pacMan.getX() + pacMan.getVelocityX());
        pacMan.setY(pacMan.getY() + pacMan.getVelocityY());

        for(Block wall : walls){
            if(collision(pacMan, wall)){
                pacMan.setX(pacMan.getX() - pacMan.getVelocityX());
                pacMan.setY(pacMan.getY() - pacMan.getVelocityY());
                break;
            }
        }

    }
    public boolean collision(Block a, Block b){
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }
    private void updatePacmanImage() {
        switch (pacMan.getDirection()) {
            case 'W': pacMan.setImage(pacmanUpIcon); break;
            case 'S': pacMan.setImage(pacmanDownIcon); break;
            case 'A': pacMan.setImage(pacmanLeftIcon); break;
            case 'D': pacMan.setImage(pacmanRightIcon); break;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //pacmanMove();
        pacMan.updateVelocityAndMove(this);
        updatePacmanImage();
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP){
            pacMan.updateDirection('W', this);
        }
        else if(e.getKeyCode() == KeyEvent.VK_S  || e.getKeyCode() == KeyEvent.VK_DOWN){
            pacMan.updateDirection('S', this);
        }
        else if(e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT){
            pacMan.updateDirection('A', this);
        }else if(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT){
            pacMan.updateDirection('D', this);
        }
        updatePacmanImage();
    }

}
