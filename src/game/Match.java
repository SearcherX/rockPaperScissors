package game;

import java.util.ArrayList;

public class Match {
    public static final int ROUNDS_PER_GAME_COUNT = 5;
    public static final int GAMES_PER_MATCH_COUNT = 3;
    private final ArrayList<Game> games = new ArrayList<>();

    public ArrayList<Game> getGames() {
        return games;
    }

}
