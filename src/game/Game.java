package game;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Game {
    private final ArrayList<Round> rounds = new ArrayList<>();
    private final LocalDateTime startTime;
    private LocalDateTime stopTime = null;

    public Game() {
        startTime = LocalDateTime.now();
    }

    public Round setRound(Figure player1choice, Figure player2choice) {
        Round round = new Round(player1choice, player2choice);
        rounds.add(round);
        return round;
    }

    public ArrayList<Round> getRounds() {
        return rounds;
    }

    public ArrayList<Integer> getResult() {
        ArrayList<Integer> res = new ArrayList<>();
        for (Round round: rounds) {
            res.add(round.getResult());
        }

        return res;
    }

    public void stop() {
        stopTime = LocalDateTime.now();
    }

    public String getGameDuration() {
        if (stopTime == null)
            throw new RuntimeException("Игра ещё не закончена");

        long millis = Duration.between(startTime, stopTime).toMillis();
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }
}
