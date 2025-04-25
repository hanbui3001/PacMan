package PacmanGame;

import java.awt.*;
import java.util.HashSet;

public class Block {
    private int x, y, width, height, startX, startY;
    private Image image;
    private char direction = 'W';
    private char intendedDirection = 'W';
    private int velocityX = 0, velocityY = 0;
    private int tileSize = 32;
    private int moveCounter = 0;
    private static final int DIRECTION = 7;
    private char previousDirection = ' ';
    private  int chaseCounter;
    private boolean isChasing = false;

    public Block() {}
    public Block(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.startX = x;
        this.startY = y;
        this.image = image;
    }

    public int getChaseCounter() {
        return chaseCounter;
    }
    public void setChaseCounter(int counter) {
        this.chaseCounter = counter;
    }

    public void incrementChaseCounter() {
        this.chaseCounter++;
    }
    public boolean isChasing() {
        return isChasing;
    }

    public void setChasing(boolean chasing) {
        this.isChasing = chasing;
    }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getStartX() { return startX; }
    public int getStartY() { return startY; }
    public Image getImage() { return image; }
    public char getDirection() { return direction; }
    public int getVelocityX() { return velocityX; }
    public int getVelocityY() { return velocityY; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setImage(Image image) { this.image = image; }

    public char getPreviousDirection() {
        return previousDirection;
    }

    public void updateDirection(char newDirection, PacmanPlay pacman) {
        previousDirection = direction;
        this.intendedDirection = newDirection;
        updateVelocityAndMove(pacman);
    }

    public void updateVelocityAndMove(PacmanPlay pacman) {
        moveCounter ++;
        char tempDirection = intendedDirection;
        setVelocity(intendedDirection, pacman.getSpeed(this));
        int newX = this.x + velocityX;
        int newY = this.y + velocityY;


        Block tempBlock = new Block(newX, newY, width, height, image);

        boolean canMoveIntended = true;
        for (Block wall : pacman.walls) {
            if (pacman.collision(tempBlock, wall)) {
                canMoveIntended = false;
                break;
            }
        }

        if (canMoveIntended) {
            direction = intendedDirection;
            this.x = newX;
            this.y = newY;
        } else {
            setVelocity(direction, pacman.getSpeed(this));
            newX = this.x + velocityX;
            newY = this.y + velocityY;
            tempBlock = new Block(newX, newY, width, height, image);

            boolean canMoveCurrent = true;
            for (Block wall : pacman.walls) {
                if (pacman.collision(tempBlock, wall)) {
                    canMoveCurrent = false;
                    break;
                }
            }

            if (canMoveCurrent) {
                this.x = newX;
                this.y = newY;
            }
            else{
                velocityX = 0;
                velocityY = 0;
            }
        }
        //System.out.println("intendedDirection = " + intendedDirection);
    }

    public void setVelocity(char dir, int speed) {
        switch (dir) {
            case 'W':
                velocityX = 0;
                velocityY = -speed;
                break;
            case 'S':
                velocityX = 0;
                velocityY = speed;
                break;
            case 'A':
                velocityX = -speed;
                velocityY = 0;
                break;
            case 'D':
                velocityX = speed;
                velocityY = 0;
                break;
            default:
                velocityX = 0;
                velocityY = 0;
        }
    }
    public void resetMoveCounter(){
        moveCounter = 0;
    }
}