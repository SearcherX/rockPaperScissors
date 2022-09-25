package statistics;

import game.Figure;
import game.Game;
import game.Match;
import game.Round;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Statistics {
    private static final int MAX_COLUMN_LENGTH = 20;
    private final String player1;
    private final String player2;

    public Statistics(String player1, String player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    //получить отступ на основе максимальной длины колонки
    private String getIndent(String str) {
        return getIndent(str, MAX_COLUMN_LENGTH);
    }

    //получить отступ на основе максимальной длины колонки
    private String getIndent(String str, int maxLength) {
        return " ".repeat(maxLength - str.length());
    }

    private String getLeftIndent(String str, int maxLength) {
        return " ".repeat((maxLength - str.length()) / 2);
    }

    private String getLeftIndent(String str) {
        return getLeftIndent(str, MAX_COLUMN_LENGTH);
    }

    private String getRightIndent(String str, int maxLength) {
        return " ".repeat((maxLength - str.length()) / 2);
//        rightIndent += ((maxLength - str.length()) % 2 == 0 ? "" : " ");
    }

    private String getRightIndent(String str) {
        return getRightIndent(str, MAX_COLUMN_LENGTH);
    }

    private String getHorizontalLine() {
        return "—".repeat(MAX_COLUMN_LENGTH * 3 + 8);
    }

    //получить строку шапки
    private String getHeader() {
        return getHorizontalLine() + "\n" + "N игры " + "|Количество побед" +
                getIndent("|Количество побед") + "|Количество побед" + getIndent("|Количество побед") +
                "|Количесво ничьих" + getIndent("|Количесво ничьих") + "|\n" + " ".repeat(7) +
                "|" + player1 + getIndent("|" + player1) + "|" + player2 + getIndent("|" + player2) + "|" +
                "\n" + getHorizontalLine();
    }

    //получить строку статистики игры без шапки
    private String getGameStatistics(Game game, int number) {
        int player1wins = 0;
        int player2wins = 0;
        int draws = 0;

        for (Round round : game.getRounds()) {
            int roundRes = round.getResult();
            if (roundRes == 0)
                draws++;
            else if (roundRes == 1)
                player1wins++;
            else
                player2wins++;
        }

        return getLeftIndent(String.valueOf(number + 1), 7) + (number + 1) +
                getRightIndent(String.valueOf(number + 1), 7) + "|" +
                getLeftIndent(String.valueOf(player1wins)) + player1wins +
                getRightIndent(String.valueOf(player1wins)) + "|" + getLeftIndent(String.valueOf(player2wins)) +
                player2wins + getRightIndent(String.valueOf(player2wins)) + "|" + getLeftIndent(String.valueOf(draws)) +
                draws + getRightIndent(String.valueOf(draws)) + "|";
    }

    //получить строку со статистикой игры + шапка
    public String getGameResult(Game game, int number) {
        return getHeader() + "\n" + getGameStatistics(game, number) + "\n" + getHorizontalLine();
    }

    //получить строку статистики матча
    public String getMatchResult(Match match) {
        StringBuilder sb = new StringBuilder(getHeader());
        sb.append("\n");

        for (int i = 0; i < match.getGames().size(); i++) {
            sb.append(getGameStatistics(match.getGames().get(i), i)).append("\n").
                    append(getHorizontalLine()).append("\n");
        }

        sb.deleteCharAt(sb.length() - 1);

        return sb.toString();
    }

    //просуммировать время всех игр матча
    public long sumTime(Match match) {
        long sum = 0;

        for (Game game : match.getGames()) {
            sum += game.getGameDuration();
        }

        return sum;
    }

    //преобразовать милисекунды в форматированное время
    public String getTime(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    //метод нахождения самой популярной фигуры за матч
    public Figure getMostPopularFigure(Match match) {
        Figure mostPopularFigure = Figure.ROCK;
        int max = match.getGames().get(0).getFiguresCountMap().get(Figure.ROCK);;

        //внешний цикл анализирует игры из матча
        for (Game game : match.getGames()) {
            HashMap<Figure, Integer> figuresCount = game.getFiguresCountMap();
            //внутренний цикл анализирует словари<фигура, количество> игры
            for (Map.Entry<Figure, Integer> entry : figuresCount.entrySet()) {
                //запомнить максимальное число и соответствующую фигуру
                if (entry.getValue() > max) {
                    max = entry.getValue();
                    mostPopularFigure = entry.getKey();
                }
            }
        }
        return mostPopularFigure;
    }

    //метод нахождения самой популярной фигуры за матч
    public Figure getMostUnPopularFigure(Match match) {
        Figure mostUnPopularFigure = Figure.ROCK;
        int min = match.getGames().get(0).getFiguresCountMap().get(Figure.ROCK);

        //внешний цикл анализирует игры из матча
        for (Game game : match.getGames()) {
            HashMap<Figure, Integer> figuresCount = game.getFiguresCountMap();
            //внутренний цикл анализирует словари<фигура, количество> игры
            for (Map.Entry<Figure, Integer> entry : figuresCount.entrySet()) {
                //запомнить минимальное число и соответствующую фигуру
                if (entry.getValue() < min) {
                    min = entry.getValue();
                    mostUnPopularFigure = entry.getKey();
                }
            }
        }
        return mostUnPopularFigure;
    }

}
