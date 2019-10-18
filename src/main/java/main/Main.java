package main;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Game2048 game = new Game2048();
        Service service = new Service();
        service.start(game);
        Scanner scanner = new Scanner(System.in);
        while(game.isGameOn()){
            String request = scanner.nextLine();
            switch (request) {
                case("a"):
                case("d"):
                case("w"):
                case("s"):
                    if (service.move(game, request)) {
                        service.randomGenerationAndCheckGameOver(game);
                        service.display(game);
                    }
                    break;
                case("return"):
                    game = service.returnMove(game);
                    service.display(game);
                    break;
                case("save"):
                    if (service.save(game)){
                        System.out.println("Game saved");
                    }
                    break;
                case("load"):
                    game = service.load(game);
                    service.display(game);
                    break;
                case("autoload"):
                    game = service.autoLoad();
                    service.display(game);
                    break;
                case("tip"):
                    String tip = service.giveMeTip(game);
                    System.out.println(tip);
                    break;
                case("bestTip"):
                    System.out.println("The best move is " + service.bestMove(game));
                    break;
                case("q"):
                    service.end(game);
                    break;
                default:
                    System.out.println("There is no a such command, try again");
            }
        }
    }
}
