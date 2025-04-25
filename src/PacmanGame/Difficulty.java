package PacmanGame;

public enum Difficulty {
    EASY(1, 4),
    MEDIUM(2, 4),
    HARD(3, 4);
    private final int numberOfChasingGhosts;
    private final int ghostSpeed;
    Difficulty(int numberOfChasingGhosts, int ghostSpeed){
        this.numberOfChasingGhosts = numberOfChasingGhosts;
        this.ghostSpeed = ghostSpeed;
    }
    public int getNumberOfChasingGhosts() {
        return numberOfChasingGhosts;
    }

    public int getGhostSpeed() {
        return ghostSpeed;
    }
    public int getNumberOfGhost(){
        return 4;
    }
}
