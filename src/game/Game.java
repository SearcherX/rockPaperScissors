package game;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private final ArrayList<Round> rounds = new ArrayList<>();
    private final LocalDateTime startTime;
    private LocalDateTime stopTime = null;

    HashMap<Figure, Integer> figuresCount = new HashMap<>() {{
        put(Figure.ROCK, 0);
        put(Figure.SCISSORS, 0);
        put(Figure.PAPER, 0);
    }};

    public Game() {
        startTime = LocalDateTime.now();
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public void stop() {
        stopTime = LocalDateTime.now();
        setFigureCountMap();
    }

    public HashMap<Figure, Integer> getFiguresCountMap() {
        return figuresCount;
    }

    //метод подсчета количества выборов каждой фигуры
    public void setFigureCountMap() {
        for (Round round: getRounds()) {
            for (Map.Entry<Figure, Integer> entry: round.getFigureCountMap().entrySet()) {
                figuresCount.put(entry.getKey(), figuresCount.get(entry.getKey()) + entry.getValue());
            }
        }
    }

    public long getGameDuration() {
        if (stopTime == null)
            throw new RuntimeException("Игра ещё не закончена");

        return Duration.between(startTime, stopTime).toMillis();
    }
}
