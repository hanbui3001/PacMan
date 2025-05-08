package PacmanGame;

import Util.ButtonUtil;
import pacManUI.GameComplete;
import pacManUI.GameOver;
import pacManUI.LivesAndScoreBoard;
import pacManUI.MainMenu;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

public class PacmanPlay extends JPanel implements ActionListener, KeyListener {
    private int rowCount = 18;
    private int colCount = 19;
    private int tileSize = 32;
    private  int boardWidth = colCount * tileSize;
    private int boardHeight = rowCount * tileSize;
    private Block block  = new Block(); ;
    HashSet<Block> walls = new HashSet <Block>();
    HashSet<Block> ghosts = new HashSet<Block>();
    HashSet<Block> foods = new HashSet<Block>();
    HashSet<Block> teleports = new HashSet<>();
    Block pacMan;
    PacmanMap tileMaps = new PacmanMap();
    Timer gameLoop;
    private String[] mapData;
    private char[] direction = {'W','D', 'A', 'D'};
    Random random = new Random();
    private boolean isMouthOpen = true;
    private  int  CURRENT_MOUTH_OPEN = 1;
    private int mouthCouter = 0;
    private boolean justTeleported = false;
    private boolean isTeleporting = false;
    private int teleportFrame = 0;
    private final int TELEPORT_FRAMES = 8;
    private int teleportStartX;
    private int teleportEndX;
    private int teleportY;
    private char teleportDirection;
    private Block teleportingEntity;
    private LivesAndScoreBoard livesAndScoreBoard;
    private boolean gameOver = false;
    private Timer pauseTimer;
    private boolean isPaused = false;
    private boolean isCollision = false;
    private char[][] map;
    private boolean isChasing = false;
    private Timer delayTimer;
    private final int CHASE_UPDATE_INTERVAL = 4;
    private boolean pauseGame = false;
    private MainMenu mainMenu;
    private GameOver gameOverScore;
    private GameComplete gameComplete;
    private JFrame gameFrame;
    private Difficulty difficulty;
    private boolean isFinished = false;
    private Clip backgroundMusic;
    private Image wallIcon;
    private Image blueGhostIcon;
    private Image orangeGhostIcon;
    private Image pinkGhostIcon;
    private Image redGhostIcon;
    private Image purpleGhostIcon;

    private Image pacmanDownIcon;
    private Image pacmanUpIcon;
    private Image pacmanLeftIcon;
    private Image pacmanRightIcon;
    private Image pacmManIcon;

    public PacmanPlay(LivesAndScoreBoard livesAndScoreBoard, MainMenu mainMenu, JFrame gameFrame, Difficulty difficulty) {
        this.livesAndScoreBoard = livesAndScoreBoard;
        this.mainMenu = mainMenu;
        this.gameFrame = gameFrame;
        this.difficulty = difficulty;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        loadImage();
        loadMap();
        playBackgroundMusic();
        int ghostIndex = 0;
        for (Block ghost : ghosts) {
            int speed = getSpeed(ghost);
            System.out.println("Ghost " + ghostIndex + " speed: " + speed);
            ghostIndex++;
        }
        gameLoop = new Timer(50 , this);
        gameLoop.start();
        ghostMove();
        pauseTimer = new Timer(1000, e -> {
            if (isFinished) {
                return;
            }
            isPaused = pauseGame;
            isCollision = false;
            pauseTimer.stop();
            resetPacman();
            resetGhost();
            if (!pauseGame && !isFinished) {
                gameLoop.start();
            }
            repaint();
        });
        pauseTimer.setRepeats(false);
        map = new char[rowCount][colCount];
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
                map[i][j] = mapData[i].charAt(j);
            }
        }
        int delay = 4000 + random.nextInt(1000);
        delayTimer = new Timer(delay, e -> {
            if(isFinished){
                return;
            }
            isChasing = true;
            delayTimer.stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();

    }
    public void resetGame(){
        livesAndScoreBoard.resetLives();
        livesAndScoreBoard.resetScores();
        gameOver = false;
        isPaused = false;
        pauseGame = false;
        isCollision = false;
        isChasing = false;
        isTeleporting  = false;
        justTeleported = false;
        isFinished = false;
        resetPacman();
        resetGhost();
        foods.clear();
        loadMap();
        gameLoop.start();
        requestFocusInWindow();
        setVisible(true);
        int delay = 4000 + random.nextInt(1000);
        delayTimer = new Timer(delay, e -> {
            if (isFinished) {
                return;
            }   
            isChasing = true;
            delayTimer.stop();
        });
        delayTimer.setRepeats(false);
        delayTimer.start();
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void loadImage(){
        wallIcon = new ImageIcon(getClass().getResource("/image/wall.png")).getImage();
        blueGhostIcon = new ImageIcon(getClass().getResource("/image/blueGhost.png")).getImage();
        orangeGhostIcon = new ImageIcon(getClass().getResource("/image/orangeGhost.png")).getImage();
        pinkGhostIcon = new ImageIcon(getClass().getResource("/image/pinkGhost.png")).getImage();
        redGhostIcon = new ImageIcon(getClass().getResource("/image/redGhost.png")).getImage();
        purpleGhostIcon = new ImageIcon(getClass().getResource("/image/purple-ghost.png")).getImage();

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
        mapData = tileMap;
        walls.clear();
        ghosts.clear();
        foods.clear();
        teleports.clear();
        int maxGhost = difficulty.getNumberOfGhost();
        int ghostCount = 0;
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
                else if (tileMapChar == 'b' && ghostCount < maxGhost) {
                    Block ghost = new Block( x, y, tileSize, tileSize, blueGhostIcon);
                    ghosts.add(ghost);
                    ghostCount++;
                }
                else if (tileMapChar == 'o' && ghostCount < maxGhost) {
                    Block ghost = new Block(x, y, tileSize, tileSize, orangeGhostIcon);
                    ghosts.add(ghost);
                    ghostCount++;
                }
                else if (tileMapChar == 'p' && ghostCount < maxGhost) {
                    Block ghost = new Block( x, y, tileSize, tileSize, pinkGhostIcon);
                    ghosts.add(ghost);
                    ghostCount++;
                }
                else if (tileMapChar == 'r' && ghostCount < maxGhost) {
                    Block ghost = new Block( x, y, tileSize, tileSize, redGhostIcon);
                    ghosts.add(ghost);
                    ghostCount++;
                }
                else if (tileMapChar == 'P') {
                    pacMan = new Block( x, y, tileSize, tileSize, pacmanRightIcon);
                }
                else if (tileMapChar == ' ') {
                    Block food = new Block( x + 14, y + 14, 4, 4, null);
                    foods.add(food);
                }
                else if(tileMapChar == 'T'){
                    Block teleport = new Block(x, y, tileSize, tileSize, null);
                    teleports.add(teleport);
                }
                else if(tileMapChar == 'z'){
                    Block ghost = new Block( x, y, tileSize, tileSize, purpleGhostIcon);
                    ghosts.add(ghost);
                    ghostCount++;
                }

            }
        }
    }


    public void chasePacman(Block ghost) {
        int ghostRow = ghost.getY() / tileSize;
        int ghostCol = ghost.getX() / tileSize;
        int pacmanRow = pacMan.getY() / tileSize;
        int pacmanCol = pacMan.getX() / tileSize;
        System.out.println("chasePacman called for ghost at (" + ghostRow + ", " + ghostCol + "), Pacman at (" + pacmanRow + ", " + pacmanCol + ")");
        java.util.Queue<int[]> queue = new LinkedList<>();
        int[][] parentRow = new int[rowCount][colCount];
        int[][] parentCol = new int[rowCount][colCount];
        boolean[][] visited = new boolean[rowCount][colCount];
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        char[] directions = {'W', 'S', 'A', 'D'};
        queue.offer(new int[]{ghostRow, ghostCol});
        visited[ghostRow][ghostCol] = true;
        parentRow[ghostRow][ghostCol] = -1;
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            if (row == pacmanRow && col == pacmanCol) {
                System.out.println("Path found to Pacman!");
                while (parentRow[row][col] != -1) {
                    int nextRow = parentRow[row][col];
                    int nextCol = parentCol[row][col];
                    if (nextRow == ghostRow && nextCol == ghostCol) {
                        for (int i = 0; i < 4; i++) {
                            if (row == ghostRow + dr[i] && col == ghostCol + dc[i]) {
                                ghost.updateDirection(directions[i], this);
                                ghost.setVelocity(directions[i], getSpeed(ghost));
                                System.out.println("Ghost directed to " + directions[i]);
                                return;
                            }
                        }
                    }
                    row = nextRow;
                    col = nextCol;
                }
                break;
            }
            for (int i = 0; i < 4; i++) {
                int newRow = row + dr[i];
                int newCol = col + dc[i];
                if (newRow >= 0 && newRow < rowCount && newCol >= 0 && newCol < colCount &&
                        !visited[newRow][newCol] && map[newRow][newCol] != 'X') {
                    queue.offer(new int[]{newRow, newCol});
                    visited[newRow][newCol] = true;
                    parentRow[newRow][newCol] = row;
                    parentCol[newRow][newCol] = col;
                }
            }
        }
        System.out.println("No path found to Pacman, switching to random movement");
        ArrayList<Character> validDirections = getValidDirection(ghost);
        if (!validDirections.isEmpty()) {
            char newDirection = validDirections.get(random.nextInt(validDirections.size()));
            ghost.updateDirection(newDirection, this);
            ghost.setVelocity(newDirection, getSpeed(ghost));
            System.out.println("Ghost switched to random direction: " + newDirection);
        } else {
            char oppositeDir = getOpositeDirection(ghost.getDirection());
            ghost.updateDirection(oppositeDir, this);
            ghost.setVelocity(oppositeDir, getSpeed(ghost));
            System.out.println("Ghost reversed to direction: " + oppositeDir);
        }
    }
    public void teleportEntity(Block entity) {
        if (isTeleporting && entity != teleportingEntity) {
            return; // Đợi đến khi thực thể hiện tại hoàn thành teleport
        }
        if (isTeleporting && entity == teleportingEntity) {
            teleportFrame++;
            if (teleportFrame <= TELEPORT_FRAMES / 2) {
                if (teleportDirection == 'A') {
                    teleportingEntity.setX(teleportStartX - (teleportFrame * tileSize / (TELEPORT_FRAMES / 2)));
                } else {
                    teleportingEntity.setX(teleportStartX + (teleportFrame * tileSize / (TELEPORT_FRAMES / 2)));
                }
            } else if (teleportFrame <= TELEPORT_FRAMES) {
                int framesIn = teleportFrame - TELEPORT_FRAMES / 2;
                if (teleportDirection == 'A') {
                    teleportingEntity.setX(teleportEndX + tileSize - (framesIn * tileSize / (TELEPORT_FRAMES / 2)));
                } else {
                    teleportingEntity.setX(teleportEndX - tileSize + (framesIn * tileSize / (TELEPORT_FRAMES / 2)));
                }
            }
            if (teleportFrame >= TELEPORT_FRAMES) {
                // Kết thúc animation
                isTeleporting = false;
                justTeleported = true;
                teleportingEntity.setX(teleportEndX);
                teleportingEntity.setY(teleportY);
                teleportingEntity.updateDirection(teleportDirection, this);
            }
            return;
        }

        if (justTeleported) {
            // Kiểm tra xem entity có còn ở ô teleport không
            boolean stillAtTeleport = false;
            for (Block teleport : teleports) {
                if (collision(entity, teleport)) {
                    stillAtTeleport = true;
                    break;
                }
            }
            if (!stillAtTeleport) {
                justTeleported = false; // Reset cờ khi rời ô teleport
            }
            return;
        }

        for (Block teleport : teleports) {
            if (collision(entity, teleport)) {
                char currentDirection = entity.getDirection();
                // Chỉ teleport khi di chuyển ngang
                if (!(currentDirection == 'D' || currentDirection == 'A')) {
                    return;
                }

                int currentY = teleport.getY();
                Block oppositeTeleport = null;

                // Tìm ô teleport đối diện (cùng Y, khác X)
                for (Block otherTeleport : teleports) {
                    if (otherTeleport.getY() == currentY && otherTeleport != teleport) {
                        oppositeTeleport = otherTeleport;
                        break;
                    }
                }

                if (oppositeTeleport == null) {
                    return;
                }

                int newX;
                char newDirection = currentDirection;

                // Đặt vị trí mới và hướng di chuyển
                if (teleport.getX() == 0) { // Cạnh trái
                    newX = oppositeTeleport.getX() - tileSize;
                    newDirection = 'A';
                } else if (teleport.getX() == boardWidth - tileSize) {
                    newX = oppositeTeleport.getX() + tileSize;
                    newDirection = 'D';
                } else {
                    return; // Không ở cạnh biên
                }

                // Kiểm tra vị trí mới không va chạm tường
                Block tempBlock = new Block(newX, currentY, entity.getWidth(), entity.getHeight(), null);
                boolean canTeleport = true;
                for (Block wall : walls) {
                    if (collision(tempBlock, wall)) {
                        canTeleport = false;
                        break;
                    }
                }
                boolean willCollideWithOppositeTeleport = collision(tempBlock, oppositeTeleport);
                if (willCollideWithOppositeTeleport) {
                    return; // Bỏ qua teleport để tránh vòng lặp
                }
                if (canTeleport) {
                    // Bắt đầu animation
                    isTeleporting = true;
                    teleportFrame = 0;
                    teleportingEntity = entity;
                    teleportStartX = entity.getX();
                    teleportEndX = newX;
                    teleportY = currentY;
                    teleportDirection = newDirection;
                }
                return;
            }
        }
    }
    public int getSpeed(Block block){
        if(block == pacMan){
            return tileSize / 4;
        }
        else return difficulty.getGhostSpeed();
    }
    public void ghostMove() {
        if (isPaused) return;
        int maxChasingGhost = difficulty.getNumberOfChasingGhosts();
        System.out.println("maxChasingGhost: " + maxChasingGhost + ", isChasing: " + isChasing);
        StringBuilder speedLog = new StringBuilder("[Frame Update] ");
        int ghostIndexForLog = 0;
        for (Block ghost : ghosts) {
            speedLog.append("Ghost ").append(ghostIndexForLog).append(" speed: ").append(getSpeed(ghost));
            if (ghostIndexForLog < ghosts.size() - 1) {
                speedLog.append(", ");
            }
            ghostIndexForLog++;
        }
        System.out.println(speedLog.toString());
        int ghostIndex = 0;
        for (Block ghost : ghosts) {
            if (ghostIndex < maxChasingGhost && isChasing) {
                System.out.println("Ghost " + ghostIndex + " is chasing at position (" + ghost.getX() + ", " + ghost.getY() + ")");
                ghost.incrementChaseCounter();
                if (ghost.getChaseCounter() >= CHASE_UPDATE_INTERVAL) {
                    System.out.println("Ghost " + ghostIndex + " calling chasePacman");
                    chasePacman(ghost);
                    ghost.setChaseCounter(0);
                }
                if (!canMoveCurrentDirection(ghost)) {
                    ArrayList<Character> validDirections = getValidDirection(ghost);
                    if (!validDirections.isEmpty()) {
                        char newDirection = validDirections.get(random.nextInt(validDirections.size()));
                        ghost.updateDirection(newDirection, this);
                        ghost.setVelocity(newDirection, getSpeed(ghost));
                        System.out.println("Ghost " + ghostIndex + " (chasing) changed direction to " + newDirection);
                    } else {
                        ghost.setVelocity(ghost.getDirection(), 0);
                        System.out.println("Ghost " + ghostIndex + " (chasing) stopped, no valid directions");
                    }
                }
            } else {
                if (random.nextInt(4) < 3 || !canMoveCurrentDirection(ghost)) {
                    ArrayList<Character> validDirections = getValidDirection(ghost);
                    if (!validDirections.isEmpty()) {
                        char newDirection = validDirections.get(random.nextInt(validDirections.size()));
                        ghost.updateDirection(newDirection, this);
                        ghost.setVelocity(newDirection, getSpeed(ghost));
                        ghost.resetMoveCounter();
                    } else {
                        char oppositeDir = getOpositeDirection(ghost.getDirection());
                        ghost.updateDirection(oppositeDir, this);
                        ghost.setVelocity(oppositeDir, getSpeed(ghost));
                    }
                }
            }

            if (isTeleporting && ghost == teleportingEntity) {
                teleportEntity(ghost);
            } else {
                if (canMoveCurrentDirection(ghost)) {
                    ghost.setVelocity(ghost.getDirection(), getSpeed(ghost));
                    int newX = ghost.getX() + ghost.getVelocityX();
                    int newY = ghost.getY() + ghost.getVelocityY();

                    if (collideWithOtherGhost(ghost, newX, newY, ghosts)) {
                        ArrayList<Character> validDirections = getValidDirection(ghost);
                        if (!validDirections.isEmpty()) {
                            ArrayList<Character> nonCollidingDirections = new ArrayList<>();
                            for (char dir : validDirections) {
                                ghost.setVelocity(dir, getSpeed(ghost));
                                int testX = ghost.getX() + ghost.getVelocityX();
                                int testY = ghost.getY() + ghost.getVelocityY();
                                if (!collideWithOtherGhost(ghost, testX, testY, ghosts)) {
                                    nonCollidingDirections.add(dir);
                                }
                            }

                            if (!nonCollidingDirections.isEmpty()) {
                                char newDirection = nonCollidingDirections.get(random.nextInt(nonCollidingDirections.size()));
                                ghost.updateDirection(newDirection, this);
                                ghost.setVelocity(newDirection, getSpeed(ghost));
                                System.out.println("Ghost " + ghostIndex + " avoided collision, changed direction to " + newDirection);
                            } else {
                                ghost.setVelocity(ghost.getDirection(), 0);
                                System.out.println("Ghost " + ghostIndex + " stopped to avoid collision");
                            }
                        } else {
                            ghost.setVelocity(ghost.getDirection(), 0);
                            System.out.println("Ghost " + ghostIndex + " stopped, no valid directions to avoid collision");
                        }
                    }
                    if (canMoveCurrentDirection(ghost)) {
                        ghost.updateVelocityAndMove(this);
                    }
                }
                teleportEntity(ghost);
            }
            ghostIndex++;
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
    public ArrayList<Character> getValidDirection(Block ghost) {
        ArrayList<Character> validDirections = new ArrayList<>();
        ArrayList<Character> horizontalDirections = new ArrayList<>();
        char[] possibleDirections = {'W', 'A', 'S', 'D'};
        int speed = getSpeed(ghost);
        char oppositeDir = getOpositeDirection(ghost.getDirection());

        for (char dir : possibleDirections) {
            ghost.setVelocity(dir, speed);
            boolean canMove = true;
            int newX = ghost.getX() + ghost.getVelocityX();
            int newY = ghost.getY() + ghost.getVelocityY();
            Block tempBlock = new Block(newX, newY, ghost.getWidth(), ghost.getHeight(), ghost.getImage());

            for (Block wall : walls) {
                if (collision(tempBlock, wall)) {
                    canMove = false;
                    break;
                }
            }

            if (canMove) {
                if (dir != oppositeDir) {
                    validDirections.add(dir);
                    if (dir == 'A' || dir == 'D') {
                        horizontalDirections.add(dir);
                    }
                }
            }
        }

        if (validDirections.isEmpty()) {
            ghost.setVelocity(oppositeDir, speed);
            int newX = ghost.getX() + ghost.getVelocityX();
            int newY = ghost.getY() + ghost.getVelocityY();
            Block tempBlock = new Block(newX, newY, ghost.getWidth(), ghost.getHeight(), ghost.getImage());
            boolean getBack = true;
            for (Block wall : walls) {
                if (collision(tempBlock, wall)) {
                    getBack = false;
                    break;
                }
            }
            if (getBack) {
                validDirections.add(oppositeDir);
            }
        }
        if (!horizontalDirections.isEmpty() && (ghost.getDirection() == 'W' || ghost.getDirection() == 'S')) {
            return horizontalDirections;
        }
        return validDirections;
    }
    private boolean collideWithOtherGhost(Block currentGhost, int newX, int newY, HashSet<Block> ghosts) {
        for (Block otherGhost : ghosts) {
            if (otherGhost == currentGhost) continue;
            if (otherGhost.getX() == newX && otherGhost.getY() == newY) {
                return true;
            }
        }
        return false;
    }
    private void showPauseMenu(){
        JFrame pauseFrame = new JFrame("Pause Menu");
        pauseFrame.setSize(350, 250);
        pauseFrame.setLocationRelativeTo(gameFrame);
        pauseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pauseFrame.setResizable(false);
        pauseFrame.setLayout(null);
        this.stopBackgroundMusic();
        pauseFrame.getContentPane().setBackground(new Color(30, 58, 138));
        JButton resumeButton = new JButton("Resume");
        resumeButton.setBounds(70, 30, 200, 50);
        new ButtonUtil().checkJbutton(resumeButton);
        resumeButton.addActionListener(e -> {
            playBackgroundMusic();
            pauseGame = false;
            isPaused = false;
            gameLoop.start();
            pauseFrame.dispose();
            requestFocusInWindow();
        });
        JButton retryButton = new JButton("Retry");
        retryButton.setBounds(70, 90, 200, 50);
        new ButtonUtil().checkJbutton(retryButton);
        retryButton.addActionListener(e -> {
            playBackgroundMusic();
            resetGame();
            pauseGame = false;
            gameLoop.start();
            pauseFrame.dispose();
            requestFocusInWindow();
        });
        JButton menuButton = new JButton("Menu");
        menuButton.setBounds(70, 150, 200, 50);
        new ButtonUtil().checkJbutton(menuButton);
        menuButton.addActionListener(e -> {
            gameFrame.dispose();
            mainMenu.showMainMenu();
            pauseFrame.dispose();
        });
        pauseFrame.add(resumeButton);
        pauseFrame.add(retryButton);
        pauseFrame.add(menuButton);
        pauseFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                pauseGame = false;
                isPaused = false;
                gameLoop.start();
                requestFocusInWindow();
            }
        });
        pauseFrame.setVisible(true);
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
        if (pauseGame) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial Rounded MT Bold", Font.BOLD, 28));
            FontMetrics fm = g.getFontMetrics();
            String pauseText = "Pause";
            int textWidth = fm.stringWidth(pauseText);
            int textHeight = fm.getHeight();
            int x = (boardWidth - textWidth) / 2;
            int y = (boardHeight - textHeight) / 2 + fm.getAscent();
            g.drawString(pauseText, x, y);
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
                livesAndScoreBoard.addScore(10);
            }
        }
        foods.remove(foodEaten);
    }
    public void resetPacman() {
        pacMan.setX(pacMan.getStartX());
        pacMan.setY(pacMan.getStartY());
        pacMan.updateDirection('D', this);
        pacMan.setImage(pacmanRightIcon);
        isTeleporting = false;
        justTeleported = false;
    }
    public void resetGhost(){
        for (Block ghost : ghosts) {
            ghost.setX(ghost.getStartX());
            ghost.setY(ghost.getStartY());
            ghost.updateDirection('W', this);
            ghost.setChaseCounter(0);
        }
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
        if(gameOver || isPaused) return;
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
    private void playBackgroundMusic() {
        try {
            InputStream audioStreamInput = getClass().getClassLoader().getResourceAsStream("radio/pacman.wav");
            if (audioStreamInput == null) {
                throw new IOException("Audio file not found: radio/videoplayback.wav");
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(audioStreamInput));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
            System.out.println("Error playing background music: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        //pacmanMove();
        if(isFinished){
            return;
        }
        if (!isCollision && !pauseGame) {
            mouthCouter++;
                pacMan.updateVelocityAndMove(this);
            teleportEntity(pacMan);
            if (mouthCouter > CURRENT_MOUTH_OPEN) {
                isMouthOpen = !isMouthOpen;
                mouthCouter = 0;
            }
            updatePacmanImage();
            ghostMove();
            foodEat();
            for (Block ghost : ghosts) {
                if (collision(pacMan, ghost)) {
                    isCollision = true;
                    isPaused = true;
                    pacMan.setVelocity(pacMan.getDirection(), 0);
                    if (livesAndScoreBoard.loseLife()) {
                        pauseTimer.start();
                    } else {
                        isFinished = true;
                        gameLoop.stop();
                        pauseTimer.stop();
                        delayTimer.stop();
                        gameOver = true;
                        gameOverScore = new GameOver(mainMenu, this, livesAndScoreBoard.getScores(), gameFrame);
                        gameOverScore.showGameOver();
                    }
                    break;
                }
            }

            // Kiểm tra điều kiện hoàn thành game
            if (foods.isEmpty()) {
                isFinished = true;
                gameLoop.stop();
                pauseTimer.stop();
                delayTimer.stop();
                if (livesAndScoreBoard.getLives() > 0) {
                    gameComplete = new GameComplete(mainMenu, this, livesAndScoreBoard.getScores(), gameFrame);
                    gameComplete.showGame();
                } else {
                    gameOver = true;
                    gameOverScore = new GameOver(mainMenu, this, livesAndScoreBoard.getScores(), gameFrame);
                    gameOverScore.showGameOver();
                }
                return;
            }
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!isFinished) {
                if (!pauseGame) {
                    pauseGame = true;
                    isPaused = true;
                    gameLoop.stop();
                } else {
                    pauseGame = false;
                    isPaused = false;
                    gameLoop.start();
                }
                repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (!isFinished) {
                pauseGame = true;
                isPaused = true;
                gameLoop.stop();
                showPauseMenu();
                repaint();
                requestFocusInWindow();
            }
        }
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
        pacMan.setVelocity(pacMan.getDirection(), getSpeed(pacMan));
    }

}
