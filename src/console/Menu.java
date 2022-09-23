package console;

import communication.UdpReceiver;
import communication.UdpSender;
import game.Figure;
import game.Game;
import game.Match;
import game.Round;
import java.util.Random;
import java.util.Scanner;

public class Menu {
    private static final String IP = "192.168.1.4";
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
            else if (choice1 == 1 || choice1 == 2)
                stop = selectFigure(in, choice1, null);
            else if (choice1 == 3) {
                System.out.println("Выберите действие:");
                System.out.println("1 - создать игру");
                System.out.println("2 - подключиться к игре");
                System.out.println("0 - выход");
                int choice2 = in.nextInt();

                if (choice2 == 0)
                    break;
                else if (choice2 == 1) {
                    UdpReceiver receiver = new UdpReceiver(IP, PORT);
                    System.out.println("Ожидаем оппонента");
                    String opponent = receiver.receive();
                    int port = Integer.parseInt(receiver.receive());

                    System.out.println("Игрок " + opponent + "(" + receiver.getSenderIp() + ":" +
                            receiver.getSenderPort() + ") подключился");

                    receiver.send(nickname);
//                    stop = selectFigure(in, choice1, opponent);
                } else if (choice2 == 2) {
                    System.out.print("Введите ip сервера:");
                    String ipStr = in.next();

                    System.out.print("Введите port сервера:");
                    int port = in.nextInt();

                    UdpSender sender = new UdpSender(ipStr, port);
                    sender.send(nickname);
                    sender.send(Integer.toString(PORT));

                    String nicknameFromServer = sender.receive();
                    System.out.println("Вы подлючились к " + nicknameFromServer + "(" + ipStr + ":" +
                            port + ")");
                }
            }
        }


    }

    public boolean selectFigure(Scanner in, int choice, String opponent) {
        String player1;
        String player2;
        String mode;

        if (choice == 1) {
            player1 = nickname;
            player2 = "Компьютер";
            mode = "Человек-компьютер";
        } else if (choice == 2) {
            player1 = "Компьютер 1";
            player2 = "Компьютер 2";
            mode = "Компьютер-компьютер";
        } else {
            player1 = nickname;
            player2 = opponent;
            mode = "Человек-человек";
        }
        boolean stop = false;
        Match match = new Match();

        System.out.println("Вы выбрали режим " + mode);
        System.out.println("Начинается матч");
        if (choice == 1) {
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
                Figure player1Choice = Figure.ROCK;
                Figure player2Choice = getComputerChoice();
                if (choice == 2)
                    player1Choice = getComputerChoice();
                else {
                    while (!stop) {
                        int choice2 = in.nextInt();

                        if (choice2 == 0) {
                            stop = true;
                        } else if (choice2 >= 1 && choice2 <= 3) {
                            player1Choice = intToFigure(choice2 - 1);
                            break;
                        } else
                            System.out.println(choice2 + " не поддерживается");
                    }
                }
                if (stop) {
                    break;
                }

                Round curRound = game.setRound(player1Choice, player2Choice);
                System.out.println(player1 + " выбрал " + player1Choice + ", " +
                        player2 + " выбрал " + player2Choice);
                printWinner(player1, player2, curRound.getResult());

            }

            if (stop) {
                break;
            }
        }
        System.out.println("Результат матча:");
        for (Game game : match.getGames()) {
            System.out.println(game.getResult());
        }
        return stop;
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
