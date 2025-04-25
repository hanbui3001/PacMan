package PacmanGame;

public class PlayerScore implements Comparable<PlayerScore>{
    private String name;
    private int score;
    private String difficulty;

    public PlayerScore(String name, int score, String difficulty) {
        this.name = name;
        this.score = score;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getDifficulty() {
        return difficulty;
    }
    @Override
    public int compareTo(PlayerScore other) {
        return Integer.compare(other.score, this.score);
    }
    public static PlayerScore fromString(String line){
        String[] parts = line.split(",");
        if (parts.length == 3) {
            return new PlayerScore(parts[0], Integer.parseInt(parts[1]), parts[2]);
        }
        return null;
    }
    @Override
    public String toString(){
        return name + "," + score + "," + difficulty;
    }
}
