package PacmanGame;

import java.util.Random;

public class PacmanMap {
    private  final String[][] tileMaps = {
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X        X        X",
                    "X XX XXX X XXX XX X",
                    "X                 X",
                    "T XX X XXXXX X XX T",
                    "X    X       X    X",
                    "X XX XXXX XXXX XX X",
                    "X XX X       X XX X",
                    "X XX X XXrXX X XX X",
                    "X       bpo       X",
                    "X XX X XXXXX X XX X",
                    "X    X       X    X",
                    "T XX X XXXXX X XX T",
                    "X        X        X",
                    "X XX XXX X XXX XX X",
                    "X  X     P     X  X",
                    "XX X X XXXXX X X XX",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X    X   X   X    X",
                    "X XXXXX XXX XXXXX X",
                    "X                 X",
                    "T XX XXXX XXXXX XX T",
                    "X    X       X    X",
                    "XXXX XXXXXXXXXXXXXX",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X        P        X",
                    "XXXXXXXXXXXXX XXXXX",
                    "X                 X",
                    "T XXXXX XXX XXXXX T",
                    "X     X     X     X",
                    "X XXX X XrX X XXX X",
                    "X       bpo       X",
                    "X X X XXXXXXX X X X",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X       X       XXX",
                    "X XXXXX X XXXXX X X",
                    "X        P        X",
                    "TXXXX XXXXXXX XXXXT",
                    "X                 X",
                    "X XX X XXXXX X XX X",
                    "X XX X       X XX X",
                    "X XX X XXrXX X XX X",
                    "X       bpo       X",
                    "XXXX X XXXXX X XXXX",
                    "X XX X       X XX X",
                    "T XX XXXXXXXXX XX T",
                    "X                 X",
                    "X XXX XXXXXXX XXX X",
                    "X      XXXXX      X",
                    "X XXX         XXXXX",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X    r   X        X",
                    "X XXXXX XXX XXXXX X",
                    "X                 X",
                    "T XXXXX XXX XXXXX T",
                    "X       X         X",
                    "XXXXXXX X XXXXXXXXX",
                    "X       X         X",
                    "X XXXXX X XXXXX X X",
                    "X       P       X X",
                    "XXXX X XXXXX X XXXX",
                    "X       X         X",
                    "T X XXX X XXX   X T",
                    "X     b X      p  X",
                    "X XXXXX XX XXXXXX X",
                    "X X  XX XX XX  XX X",
                    "X                 X",
                    "XXXXXXXXXXXXXXXXXXX"
            }

    };

    public String[] getTileMaps() {
        Random rand = new Random();
        return tileMaps[rand.nextInt(tileMaps.length)];
    }
}
