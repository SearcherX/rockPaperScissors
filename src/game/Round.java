package game;

import java.util.HashMap;

public class Round {
    private Figure player1choice = null;
    private Figure player2choice = null;
    private int result;
    private final HashMap<Figure, Integer> figureCountMap = new HashMap<>() {{
        put(Figure.ROCK, 0);
        put(Figure.SCISSORS, 0);
        put(Figure.PAPER, 0);
    }};

    public Round() {
    }

    public Round(Figure playerChoice) {
        setFigureCountMap(playerChoice);
    }

    public Round(Figure player1choice, Figure player2choice) {
        this.player1choice = player1choice;
        this.player2choice = player2choice;
        setFigureCountMap(player1choice, player2choice);
        result = processResult();
    }

    public HashMap<Figure, Integer> getFigureCountMap() {
        return figureCountMap;
    }

    public Figure getPlayer1FigureChoice() {
        return player1choice;
    }

    public Figure getPlayer2FigureChoice() {
        return player2choice;
    }

    public void setPlayer1FigureChoice(Figure player1choice) {
        this.player1choice = player1choice;
        setFigureCountMap(player1choice);

        if (player2choice != null)
            result = processResult();
    }

    public void setPlayer2FigureChoice(Figure player2choice) {
        this.player2choice = player2choice;
        setFigureCountMap(player2choice);

        if (player1choice != null)
            result = processResult();
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

    public void setResult(int result) {
        this.result = result;
    }

    //запомнить выбор
    private void setFigureCountMap(Figure player1choice, Figure player2choice) {
        setFigureCountMap(player1choice);
        setFigureCountMap(player2choice);
    }

    private void setFigureCountMap(Figure playerChoice) {
        figureCountMap.put(playerChoice, figureCountMap.get(playerChoice) + 1);
    }
}
