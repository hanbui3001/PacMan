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

    public void updateDirection(char newDirection, PacmanPlay pacman) {
        this.intendedDirection = newDirection;
        updateVelocityAndMove(pacman);
    }

    public void updateVelocityAndMove(PacmanPlay pacman) {
        char tempDirection = intendedDirection;
        setVelocity(tempDirection);
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
            setVelocity(direction);
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
        }
    }

    private void setVelocity(char dir) {
        switch (dir) {
            case 'W':
                velocityX = 0;
                velocityY = -tileSize / 4;
                break;
            case 'S':
                velocityX = 0;
                velocityY = tileSize / 4;
                break;
            case 'A':
                velocityX = -tileSize / 4;
                velocityY = 0;
                break;
            case 'D':
                velocityX = tileSize / 4;
                velocityY = 0;
                break;
            default:
                velocityX = 0;
                velocityY = 0;
        }
    }
}