package console;

import communication.IUDPSocket;
import communication.UDPClient;
import communication.UDPServer;
import game.Figure;
import game.Game;
import game.Match;
import game.Round;
import org.w3c.dom.ls.LSOutput;
import statistics.Statistics;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private static final int PORT = 1024;
    private String nickname;

    public void start() {
        Scanner in = new Scanner(System.in);
        boolean stop = false;

        System.out.print("Enter nickname: ");
        nickname = in.next();

        System.out.println("Добро пожаловать в игру «Камень-ножницы-бумага», " + nickname);

        while (!stop) {
            System.out.println("Выберите режим игры:");
            System.out.println("1 - Человек-компьютер");
            System.out.println("2 - Компьютер-компьютер");
            System.out.println("3 - Человек-человек");
            System.out.println("0 - Выход");
            int choice1 = in.nextInt();

            if (choice1 == 0)
                break;
            else if (choice1 >= 1 && choice1 <= 3)
                stop = selectFigure(in, choice1);
        }

        in.close();

    }

    public boolean selectFigure(Scanner in, int choice) {
        if (choice < 1 || choice > 3)
            throw new IllegalArgumentException(choice + " не поддерживается");

        String player1;
        String player2;
        String mode;
        IUDPSocket socket = null;
        Match match = new Match();

        Statistics statistics = null;
        try {
            if (choice == 1) {
                player1 = nickname;
                player2 = "Компьютер";
                mode = "Человек-компьютер";
            } else if (choice == 2) {
                player1 = "Компьютер 1";
                player2 = "Компьютер 2";
                mode = "Компьютер-компьютер";
            } else {
                System.out.println("Выберите действие:");
                System.out.println("1 - создать игру");
                System.out.println("2 - подключиться к игре");
                System.out.println("0 - выход");

                while (true) {
                    int choice2 = in.nextInt();

                    if (choice2 == 0)
                        return true;
                    else if (choice2 == 1) {
                        UDPServer receiver = new UDPServer(PORT);
                        System.out.println("Ожидаем оппонента");
                        player2 = receiver.receive();

                        System.out.println("Игрок " + player2 + "(" + receiver.getSenderAddress().getHostAddress() + ":" +
                                receiver.getSenderPort() + ") подключился");

                        receiver.send(nickname);
                        socket = receiver;
                        break;
                    } else if (choice2 == 2) {
                        System.out.print("Введите ip сервера:");
                        String ipStr = in.next();

                        System.out.print("Введите port сервера:");
                        int port = in.nextInt();

                        UDPClient sender = new UDPClient(ipStr, port);
                        sender.send(nickname);

                        player2 = sender.receive();
                        System.out.println("Вы подлючились к " + player2 + "(" + ipStr + ":" +
                                port + ")");

                        socket = sender;
                        break;
                    } else {
                        System.out.println(choice2 + " не поддерживается");
                    }
                }
                player1 = nickname;
                mode = "Человек-человек";
            }

            System.out.println("Вы выбрали режим " + mode);
            System.out.println("Начинается матч");
            statistics = new Statistics(player1, player2);
            if (choice == 1 || choice == 3) {
                System.out.println("Выберите действие:");
                System.out.println("1 - выбрать камень");
                System.out.println("2 - выбрать ножницы");
                System.out.println("3 - выбрать бумагу");
                System.out.println("4 - предложить ничью");
                System.out.println("5 - признать поражение");
                System.out.println("0 - выход");
            }

            for (int i = 0; i < Match.GAMES_PER_MATCH_COUNT; i++) {
                Game game = new Game();
                match.getGames().add(game);
                System.out.println("===" + (i + 1) + "-ая игра===");
                for (int j = 0; j < Match.ROUNDS_PER_GAME_COUNT; j++) {
                    System.out.println((j + 1) + "-ый раунд");
                    Figure player1FigureChoice;
                    Figure player2FigureChoice;
                    int choice2 = 0;

                    if (choice == 2) {
                        player1FigureChoice = getComputerChoice();
                        System.out.println(player1 + " выбрал " + player1FigureChoice);
                        //задержка компьютера после хода
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        while (true) {
                            choice2 = in.nextInt();

                            if (choice2 == 0) {
                                if (choice == 3) {
                                    socket.send(String.valueOf(choice2));
                                    return true;
                                }
                            } else if (choice2 >= 1 && choice2 <= 3) {
                                player1FigureChoice = intToFigure(choice2 - 1);
                                System.out.println("Вы (" + player1 + ") выбрали " + player1FigureChoice +
                                        ". Ожидаем ход оппонента");
                                break;
                            } else
                                System.out.println(choice2 + " не поддерживается");
                        }
                    }

                    if (choice == 3) {
                        socket.send(String.valueOf(choice2));
                        int player2Choice = Integer.parseInt(socket.receive());
                        player2FigureChoice = intToFigure(player2Choice - 1);
                        if (player2Choice == 0) {
                            System.out.println("Оппонент " + player2 + " покинул игру");
                            return true;
                        }
                    } else
                        player2FigureChoice = getComputerChoice();

                    Round curRound = game.setRound(player1FigureChoice, player2FigureChoice);
                    System.out.println("Оппонент " + player2 + " выбрал " + player2FigureChoice);
                    printWinner(player1, player2, curRound.getResult());

                }

                game.stop();

                System.out.println("Результат игры:");
                System.out.println(statistics.getGameResult(game, i));
                System.out.println("Длительность игры: " + statistics.getTime(game.getGameDuration()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (choice == 3) {
                assert socket != null;
                socket.close();
            }
        }

        System.out.println("Результат матча:");
        System.out.println(statistics.getMatchResult(match));
        System.out.println("Длительность матча: " + statistics.getTime(statistics.sumTime(match)));
        System.out.println("Самая популярная фигура матча: " + statistics.getMostPopularFigure(match));
        System.out.println("Самая непопулярная фигура матча: " + statistics.getMostUnPopularFigure(match));
        return false;
    }

    public String getNickname() {
        return nickname;
    }

    public Figure getComputerChoice() {
        Random random = new Random();
        int choice = random.nextInt(3);
        return intToFigure(choice);
    }

    public Figure intToFigure(int choice) {
        Figure res;

        switch (choice) {
            case 0 -> res = Figure.ROCK;
            case 1 -> res = Figure.SCISSORS;
            case 2 -> res = Figure.PAPER;
            default -> throw new RuntimeException("Не допустимый выбор");
        }

        return res;
    }

    public void printWinner(String player1, String player2, int result) {
        switch (result) {
            case 0 -> System.out.println("Ничья");
            case 1 -> System.out.println("Победил " + player1);
            case 2 -> System.out.println("Победил " + player2);
            default -> throw new RuntimeException("Не допустимый выбор");
        }
    }
}
