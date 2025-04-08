package PacmanGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
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
    HashSet<Block> walls = new HashSetgt <Block>();
    HashSet<Block> ghosts = new HashSet<Block>();
    HashSet<Block> foods = new HashSet<Block>();
    Block pacMan;
    PacmanMap tileMaps = new PacmanMap();
    Timer gameLoop;
    private char[] direction = {'W','D', 'A', 'D'};
    Random random = new Random();
    private boolean isMouthOpen = true;
    private  int  CURRENT_MOUTH_OPEN = 1;
    private int mouthCouter = 0;

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
    private Image pacmManIcon;

    public PacmanPlay() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        loadImage();
        loadMap();
        gameLoop = new Timer(50 , this);
        gameLoop.start();
        ghostMove();
    }
    public int getTileSize(){
        return tileSize;
    }
    public Block getPacMan() {
        return pacMan;
    }

    public void setPacMan(Block pacMan) {
        this.pacMan = pacMan;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public void loadImage(){
       wallIcon = new ImageIcon(getClass().getResource("/image/wall.png")).getImage();
       blueGhostIcon = new ImageIcon(getClass().getResource("/image/blueGhost.png")).getImage();
       orangeGhostIcon = new ImageIcon(getClass().getResource("/image/orangeGhost.png")).getImage();
       pinkGhostIcon = new ImageIcon(getClass().getResource("/image/pinkGhost.png")).getImage();
       redGhostIcon = new ImageIcon(getClass().getResource("/image/redGhost.png")).getImage();

       pacmManIcon = new ImageIcon(getClass().getResource("/image/pacmanCircle.png")).getImage();
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

                if (tileMapChar == 'X') {
                    Block wall = new Block( x, y, tileSize, tileSize, wallIcon);
                    walls.add(wall);
                }
                else if (tileMapChar == 'b') {
                    Block ghost = new Block( x, y, tileSize, tileSize, blueGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') {
                    Block ghost = new Block(x, y, tileSize, tileSize, orangeGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') {
                    Block ghost = new Block( x, y, tileSize, tileSize, pinkGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') {
                    Block ghost = new Block( x, y, tileSize, tileSize, redGhostIcon);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') {
                    pacMan = new Block( x, y, tileSize, tileSize, pacmanRightIcon);
                }
                else if (tileMapChar == ' ') {
                    Block food = new Block( x + 14, y + 14, 4, 4, null);
                    foods.add(food);
                }

            }
        }
    }
    public void teleportDirection(){
     teleportEntity(pacMan);
     pacMan.setVelocity(pacMan.getDirection(), 0);
    }
    public void teleportEntity(Block entity) {
        int buffer = 10;
        if (entity.getX() + entity.getWidth() <= -buffer) {
            entity.setX(boardWidth - entity.getWidth() + buffer);
            entity.setVelocity(entity.getDirection(), 0);
        } else if (entity.getX() >= boardWidth + buffer) {
            entity.setX(-buffer);
            entity.setVelocity(entity.getDirection(), 0);
        }
    }
    public int getSpeed(Block block){
        if(block == pacMan){
            return tileSize / 4;
        }
        else return tileSize / 8;
    }
    public void ghostMove(){
        for(Block ghost : ghosts){
            if(ghost.changeDirection() ||  !canMoveCurrentDirection(ghost)) {
                ArrayList<Character> validDirections = getValidDirection(ghost);
                if(!validDirections.isEmpty()) {
                    char newDirection = validDirections.get(random.nextInt(validDirections.size()));
                    ghost.updateDirection(newDirection, this);
                    ghost.resetMoveCounter();
                }
                else{
                    ghost.setVelocity(ghost.getDirection(), 0);
                }
            }
            ghost.updateVelocityAndMove(this);
        }
    }
    public boolean canMoveCurrentDirection(Block ghost){
        ghost.setVelocity(ghost.getDirection(), getSpeed(ghost));
            int newX = ghost.getX() + ghost.getVelocityX() ;
            int newY = ghost.getY() + ghost.getVelocityY()  ;
            Block tempBlock = new Block(newX, newY, ghost.getWidth(), ghost.getHeight(), ghost.getImage());
            for (Block wall : walls) {
                if (collision(tempBlock, wall)) {
                    return false;
                }
            }
        return true;
    }
    public ArrayList<Character> getValidDirection(Block ghost){
        ArrayList<Character> validDirection = new ArrayList<>();
        char[] possibleDirections = {'W', 'A', 'S' , 'D'};
        int speed = getSpeed(ghost);
        char oppositeDir = getOpositeDirection(ghost.getDirection());
        for(char dir : possibleDirections) {
                ghost.setVelocity(dir, speed);
                boolean canMove = true;
                    int newX = ghost.getX() + ghost.getVelocityX() ;
                    int newY = ghost.getY() + ghost.getVelocityY() ;
                    Block tempBlock = new Block(newX, newY, ghost.getWidth(), ghost.getHeight(), ghost.getImage());
                    for (Block wall : walls) {
                        if (collision(tempBlock, wall)) {
                            canMove = false;
                            break;
                        }
                    }
                if (canMove) {
                    if (dir == ghost.getDirection() || (dir != ghost.getPreviousDirection() && dir != oppositeDir)) {
                        validDirection.add(dir);
                    }
                }
            }
        if(validDirection.isEmpty()){
            ghost.setVelocity(oppositeDir, speed);
            int newX = ghost.getX() + ghost.getVelocityX();
            int newY = ghost.getY() + ghost.getVelocityY();
            Block tempBlock = new Block(newX, newY, ghost.getWidth(), ghost.getHeight(), ghost.getImage());
            boolean getBack = true;
            for(Block wall : walls){
                if(collision(tempBlock, wall)){
                    getBack = false;
                    break;
                }
            }
            if(getBack){
                validDirection.add(oppositeDir);
            }
        }
        return validDirection;
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
    public boolean collision(Block a, Block b){
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    public void foodEat(){
        Block foodEaten = null;
        for(Block food : foods){
            if(collision(pacMan, food))
            {
                foodEaten = food;
            }
        }
        foods.remove(foodEaten);
    }
    public char getOpositeDirection(char direction){
        switch (direction){
            case 'W' : return 'S';
            case 'S' : return 'W';
            case 'A' : return 'D';
            case 'D' : return 'A';
            default: return direction;
        }
    }
    public void updatePacmanImage() {
        if(isMouthOpen) {
            switch (pacMan.getDirection()) {
                case 'W':
                    pacMan.setImage(pacmanUpIcon);
                    break;
                case 'S':
                    pacMan.setImage(pacmanDownIcon);
                    break;
                case 'A':
                    pacMan.setImage(pacmanLeftIcon);
                    break;
                case 'D':
                    pacMan.setImage(pacmanRightIcon);
                    break;
            }
        }
        else {
            pacMan.setImage(pacmManIcon);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //pacmanMove();

        mouthCouter++;
        pacMan.updateVelocityAndMove(this);
        //teleportDirection();
        if(mouthCouter > CURRENT_MOUTH_OPEN){
            isMouthOpen = !isMouthOpen;
            mouthCouter = 0;
        }
        updatePacmanImage();
        ghostMove();
        foodEat();
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
        //pacMan.updateVelocityAndMove(this);
    }

}
