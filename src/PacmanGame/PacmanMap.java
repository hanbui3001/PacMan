package PacmanGame;

import java.util.Random;

public class PacmanMap {
    private  final String[][] tileMaps = {
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X        X        X",
                    "X XX XXX X XXX XX X",
                    "X                 X",
                    "X XX X XXXXX X XX X",
                    "X    X       X    X",
                    "XXXX XXXX XXXX XXXX",
                    "OOOX X       X XOOO",
                    "XXXX X XXrXX X XXXX",
                    "O       bpo       O",
                    "XXXX X XXXXX X XXXX",
                    "OOOX X       X XOOO",
                    "XXXX X XXXXX X XXXX",
                    "X        X        X",
                    "X XX XXX X XXX XX X",
                    "X  X     P     X  X",
                    "XX X X XXXXX X X XX",
                    "X    X   X   X    X",
                    "X XXXXXX X XXXXXX X",
                    "X                 X",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "X    O   X   O    X",
                    "X XXXXX XXX XXXXX X",
                    "X                 X",
                    "X XX XXXO OXXX XX X",
                    "X    X       X    X",
                    "XXXX XXXXXXXXXXXXXO",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X        P        X",
                    "XXXXXXXXXXXXX XXXXX",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X     X     X     X",
                    "XXXXX X XrX X XXXXX",
                    "X   X   bpo    X  X",
                    "X X X XXXXXXX X X X",
                    "X X X    X    X X X",
                    "X X XXXXXX XXXX X X",
                    "X O             O X",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "XO      X       OXX",
                    "X XXXXX X XXXXX X X",
                    "X        P        X",
                    "XXXXX XXXXXXX XXXXX",
                    "X    X       X    X",
                    "X XX X XXXXX X XX X",
                    "X XX X       X XX X",
                    "X XX X XXrXX X XX X",
                    "O       bpo       O",
                    "X XX X XXXXX X XX X",
                    "X XX X       X XX X",
                    "X XX XXXXXXXXX XX X",
                    "X                 X",
                    "XXXXX XXXXXXX XXXXX",
                    "X                 X",
                    "X XXXXXXXXXXXXXXXXX",
                    "X                OX",
                    "XXXXXXXXXXXXXXXXXXX",
                    "XO               OX",
                    "XXXXXXXXXXXXXXXXXXX"
            },
            {
                    "XXXXXXXXXXXXXXXXXXX",
                    "XO   r   X       OX",
                    "X XXXXX XXX XXXXX X",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X       X         X",
                    "XXXXXXX X XXXXXXXXX",
                    "X       X         X",
                    "X XXXXX X XXXXX X X",
                    "X       P       X O",
                    "X XXXXX X XXXXX X X",
                    "X       X         X",
                    "XXXXXXX X XXX    X X",
                    "X     b X      p  X",
                    "X XXXXXXXXXXXXXXXXX",
                    "X                 X",
                    "X XXXXX XXX XXXXX X",
                    "X    X     X      X",
                    "XXXX X XXX X XXXXXX",
                    "X       o      XOOX",
                    "XXXXXXXXXXXXXXXXXXX"
    }
    };

    public String[] getTileMaps() {
        Random rand = new Random();
        return tileMaps[rand.nextInt(tileMaps.length)];
    }
}
