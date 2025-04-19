package PacmanGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
    private final int TELEPORT_FRAMES = 48;
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
    private int chaseCounter = 0;
    private final int CHASE_UPDATE_INTERVAL = 4;
    private boolean pauseGame = false;

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

    public PacmanPlay(LivesAndScoreBoard livesAndScoreBoard) {
        this.livesAndScoreBoard = livesAndScoreBoard;
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
        pauseTimer = new Timer(1000, e -> {
            isPaused = pauseGame;
            isCollision = false;
            pauseTimer.stop();
            resetPacman();
            resetGhost();
            if(!pauseGame){
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
        mapData = tileMap;
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
                else if(tileMapChar == 'T'){
                    Block teleport = new Block(x, y, tileSize, tileSize, null);
                    teleports.add(teleport);
                }

            }
        }
    }


    public void chasePacman(Block ghost) {
        int ghostRow = ghost.getY() / tileSize;
        int ghostCol = ghost.getX() / tileSize;
        int pacmanRow = pacMan.getY() / tileSize;
        int pacmanCol = pacMan.getX() / tileSize;
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
                while (parentRow[row][col] != -1) {
                    int nextRow = parentRow[row][col];
                    int nextCol = parentCol[row][col];
                    if (nextRow == ghostRow && nextCol == ghostCol) {
                        for (int i = 0; i < 4; i++) {
                            if (row == ghostRow + dr[i] && col == ghostCol + dc[i]) {
                                ghost.updateDirection(directions[i], this);
                                ghost.setVelocity(directions[i], getSpeed(ghost));
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
        ArrayList<Character> validDirections = getValidDirection(ghost);
        if (!validDirections.isEmpty()) {
            char newDirection = validDirections.get(random.nextInt(validDirections.size()));
            ghost.updateDirection(newDirection, this);
            ghost.setVelocity(newDirection, getSpeed(ghost));
        } else {
            ghost.setVelocity(ghost.getDirection(), 0);
        }
    }
    public void teleportEntity(Block entity) {
        if (isTeleporting) {
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
                    return; // Không tìm thấy ô teleport đối diện
                }

                int newX;
                char newDirection = currentDirection;

                // Đặt vị trí mới và hướng di chuyển
                if (teleport.getX() == 0) { // Cạnh trái
                    newX = oppositeTeleport.getX() - tileSize; // Đặt bên trái ô teleport phải
                    newDirection = 'A'; // Tiếp tục di chuyển trái
                } else if (teleport.getX() == boardWidth - tileSize) { // Cạnh phải
                    newX = oppositeTeleport.getX() + tileSize; // Đặt bên phải ô teleport trái
                    newDirection = 'D'; // Tiếp tục di chuyển phải
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
        else return tileSize / 8;
    }
    public void ghostMove() {
        if (isPaused) return;
        int ghostIndex = 0;

        for (Block ghost : ghosts) {
            // Cập nhật hướng và vận tốc
            if (ghostIndex == 0 && isChasing) {
                // Chỉ cập nhật hướng của ghost đuổi theo sau mỗi CHASE_UPDATE_INTERVAL frame
                chaseCounter++;
                if (chaseCounter >= CHASE_UPDATE_INTERVAL) {
                    chasePacman(ghost); // Cập nhật hướng và vận tốc
                    chaseCounter = 0; // Reset bộ đếm
                }
                // Nếu không thể di chuyển theo hướng hiện tại, chọn hướng mới
                if (!canMoveCurrentDirection(ghost)) {
                    ArrayList<Character> validDirections = getValidDirection(ghost);
                    if (!validDirections.isEmpty()) {
                        char newDirection = validDirections.get(random.nextInt(validDirections.size()));
                        ghost.updateDirection(newDirection, this);
                        ghost.setVelocity(newDirection, getSpeed(ghost));
                    } else {
                        ghost.setVelocity(ghost.getDirection(), 0);
                    }
                }
            } else {
                // Logic cho ghost ngẫu nhiên
                if (ghost.changeDirection() || !canMoveCurrentDirection(ghost)) {
                    ArrayList<Character> validDirections = getValidDirection(ghost);
                    if (!validDirections.isEmpty()) {
                        char newDirection = validDirections.get(random.nextInt(validDirections.size()));
                        ghost.updateDirection(newDirection, this);
                        ghost.setVelocity(newDirection, getSpeed(ghost));
                        ghost.resetMoveCounter();
                    } else {
                        ghost.setVelocity(ghost.getDirection(), 0);
                    }
                }
            }

            // Di chuyển ghost chỉ một lần mỗi frame
            if (canMoveCurrentDirection(ghost)) {
                ghost.setVelocity(ghost.getDirection(), getSpeed(ghost)); // Đảm bảo vận tốc đúng
                ghost.updateVelocityAndMove(this);
            }

            teleportEntity(ghost);
            ghostIndex++;
            System.out.println("Ghost speed: " + getSpeed(ghost));
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

    @Override
    public void actionPerformed(ActionEvent e) {
        //pacmanMove();
        if (!isCollision && !pauseGame) {
            mouthCouter++;
            if (!isTeleporting) {
                pacMan.updateVelocityAndMove(this);
            }
            teleportEntity(pacMan);
            if (mouthCouter > CURRENT_MOUTH_OPEN) {
                isMouthOpen = !isMouthOpen;
                mouthCouter = 0;
            }
            updatePacmanImage();
            ghostMove();
            foodEat();
        }
        if (!isCollision) {
            for (Block ghost : ghosts) {
                if (collision(pacMan, ghost)) {
                    isCollision = true;
                    isPaused = true;
                    pacMan.setVelocity(pacMan.getDirection(), 0);
                    if (livesAndScoreBoard.loseLife()) {
                        pauseTimer.start();
                    } else {
                        gameOver = true;
                        gameLoop.stop();
                    }
                    break;
                }
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(!pauseGame){
                pauseGame = true;
                isPaused = true;
                gameLoop.stop();
            }
            else{
                pauseGame = false;
                isPaused = false;
                gameLoop.start();
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
        //pacMan.updateVelocityAndMove(this);
    }

}
