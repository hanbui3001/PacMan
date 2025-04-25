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
                    "X        P        X",
                    "XXXX X XXXXX X XXXX",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X                 X",
                    "T XX X XX XX XX X T",
                    "X    X        X   X",
                    "XXXX X XX XXX X XXX",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X        P        X",
                    "XXX XXXX XXXX XXXXX",
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
                    "X       X         X",
                    "X XXXXX X XXX XXX X",
                    "X        P        X",
                    "T XXX XXX XXX XXX T",
                    "X                 X",
                    "X XX X XXXXX X XX X",
                    "X XX X       X XX X",
                    "X XX X XXrXX X XX X",
                    "X       bpo       X",
                    "XXXX X XXXXX X XXXX",
                    "X    X       X    X",
                    "T XX X XX XX X XX T",
                    "X                 X",
                    "X XXX XXX XXX XXX X",
                    "X     XXX XXX     X",
                    "X XXX         XXXXX",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X    r            X",
                    "X XX XX XXX XX XX X",
                    "X                 X",
                    "T XXXXX XXX XXXXX T",
                    "X                 X",
                    "X XX XX X XXX XXX X",
                    "X                 X",
                    "X XXXXX X XXXXX X X",
                    "X       P       X X",
                    "XXXX X XX XX X XX X",
                    "X                 X",
                    "T X XXX X XXX XXX T",
                    "X     b        p  X",
                    "X XX XX XX XX XXX X",
                    "X XX XX XX XX     X",
                    "X  o          XXXXX",
                    "XXXXXXXXXXXXXXXXXXX"
            }

    };

    public String[] getTileMaps() {
        Random rand = new Random();
        return tileMaps[rand.nextInt(tileMaps.length)];
    }
}
