package game;

public class Round {
    private final Figure player1choice;
    private final Figure player2choice;
    private final int result;

    public Round(Figure player1choice, Figure player2choice) {
        this.player1choice = player1choice;
        this.player2choice = player2choice;
        result = processResult();
    }

    public Figure getPlayer1choice() {
        return player1choice;
    }

    public Figure getPlayer2choice() {
        return player2choice;
    }

    public int getResult() {
        return result;
    }

    private int processResult() {
        int res;
        if (player1choice == player2choice)
            res = 0;
        else if (player1choice == Figure.PAPER && player2choice == Figure.ROCK
                || player1choice == Figure.ROCK && player2choice == Figure.SCISSORS
                || player1choice == Figure.SCISSORS && player2choice == Figure.PAPER)
            res = 1;
        else
            res = 2;
        return res;
    }
}
