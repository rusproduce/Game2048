package main;

import Garbage.MyTimerTask;

import java.io.*;
import java.util.*;

public class Service {
    /**
     * Начинаем игру!
     */
    public void start(Game2048 game) {
        game.setGameOn(true);
        randomGenerationAndCheckGameOver(game);
        randomGenerationAndCheckGameOver(game);
        autoSaveEveryMin(game);
        startForConsol(game);
    }

    /**
     * Отрисовка в консоли
     */
    public void startForConsol(Game2048 game){
        System.out.println("---------------The game 2048!---------------");
        System.out.println("-----Game settings:-----");
        System.out.println("For saving - \"save\", for loading - \"load\"");
        System.out.println("And for load autoSave game - \"autoLoad\"");
        System.out.println("For return - \"return\"");
        System.out.println("If you don't know what to do - \"help\"");
        System.out.println("Show a quite well move - \"tip\"");
        System.out.println("For quit the game - \"q\"");
        System.out.println();
        System.out.println("-----Control settings:-----");
        System.out.println("Left - \"a\", right - \"d\", up - \"w\", down - \"s\"");
        display(game);
    }

    /**
     * Генерирует значение (2 или 4) для рандомного поля
     * Проверяет возможность сделать ход после генерации, если пустых полей нет
     * Сохраняем игру для отмены хода
     */
    public void randomGenerationAndCheckGameOver(Game2048 game) {
        List<Field> listOfEmpty = new ArrayList<>();
        for (Field field : game.getFields()) {
            if (field.isEmpty()) {
                listOfEmpty.add(field);
            }
        }
        listOfEmpty.get((int) (Math.random() * listOfEmpty.size())).setI(Math.random() < 0.9 ? 2 : 4);
        if (listOfEmpty.size() == 1) {
            checkGameOver(game);
        }
    }

    /**
     * Смещаем 0 против направления движения
     * @param list содержит 4 элемента из списка fields
     */
    private void shiftFilds(Game2048 game, List<Field> list) {
        int x = game.getN();
        for (int i = 0; i < x - 1; i++) {
            if (list.get(i).isEmpty()) {
                for (int j = i; j < x - 1; j++) {
                    list.get(j).setI(list.get(j + 1).getI());
                }
                list.get(x - 1).setI(0);
                x--;
                i--;
            }
        }
    }

    /**
     * Суммируем одинаковые значения, находящиеся рядом
     * Считаем score
     * @param list cодержит 4 элемента из списка fields
     */
    private void sumFields(Game2048 game, List<Field> list){
        if (!(list.get(1).isEmpty())){
            for (int i = 0; i < game.getN() - 1; i++) {
                if (list.get(i).getI() == list.get(i + 1).getI() && !list.get(i).isEmpty()){
                    list.get(i).setI(list.get(i).getI() * 2);
                    list.get(i + 1).setI(0);
                    game.setScore(game.getScore() + list.get(i).getI());
                    i++;
                }
            }
            if (list.get(1).isEmpty() && !list.get(2).isEmpty() || list.get(2).isEmpty() && !list.get(3).isEmpty()){
                shiftFilds(game, list);
            }
        }
    }

    /**
     * Смешаем поля в выбранном пользователем напрвлении
     * @param request параметр указывающий направление
     * @return true если ход был сделан
     */
    public boolean move(Game2048 game, String request) {
        if (checkCanMove(game, request)){
            autoSaveMoveHistory(game);
            switch (request) {
                case ("a"):
                    left(game);
                    break;
                case ("d"):
                    right(game);
                    break;
                case ("w"):
                    up(game);
                    break;
                case ("s"):
                    down(game);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Формирует list из 4 элементов против направления движения и отправляет их на обработку
     */
    private void left(Game2048 game){
        List<Field> list = new ArrayList<>();
        for (int i = 0; i < game.getN() * game.getN(); i++) {
            list.add(game.getFields().get(i));
            if ((i + 1) % game.getN() == 0){
                shiftFilds(game, list);
                sumFields(game, list);
                list.removeAll(list);
            }
        }
    }

    private void right(Game2048 game){
        List<Field> list = new ArrayList<>();
        for (int i = game.getN() * game.getN() - 1; i >= 0; i--) {
            list.add(game.getFields().get(i));
            if (i % game.getN() == 0){
                shiftFilds(game, list);
                sumFields(game, list);
                list.removeAll(list);
            }
        }
    }

    private void up(Game2048 game){
        List<Field> list = new ArrayList<>();
        for (int i = 0; i < game.getN(); i++) {
            for (int j = i; j < game.getN() * game.getN(); j += game.getN()) {
                list.add(game.getFields().get(j));
            }
            shiftFilds(game, list);
            sumFields(game, list);
            list.removeAll(list);
        }
    }
/*    private void upAndDown(Game2048 game){
        List<Field> list = new ArrayList<>();
        for (int i = 0; i < game.getN(); i++) {
            for (int j = i; j < game.getN() * game.getN(); j += game.getN()) {
                list.add(game.getFields().get(j));
            }
            shiftFilds(game, list);
            sumFields(game, list);
            list.removeAll(list);
        }
    }*/

    private void down(Game2048 game){
        List<Field> list = new ArrayList<>();
        for (int i = game.getN() * (game.getN() - 1); i < game.getN() * game.getN(); i++) {
            for (int j = i; j >= 0; j -= game.getN()) {
                list.add(game.getFields().get(j));
            }
            shiftFilds(game, list);
            sumFields(game, list);
            list.removeAll(list);
        }
    }

    /**
     * Выводим игровое поле в консоль
     */
    public void display(Game2048 game) {
        int i = 0;
        for (Field field : game.getFields()){
            i++;
            System.out.print(field.getI() + "\t");
            if (i % game.getN() == 0) {
                System.out.println();
            }
        }
        System.out.println("Your score: " + game.getScore());
    }

    /**
     * Проверка существования полей с одинаковым значением, находящиеся рядом
     */
    private void checkGameOver(Game2048 game) {
        boolean finish = true;
        for (int i = 0; i < game.getN() * game.getN() - 1; i++) {
            if (i < game.getN() * (game.getN() - 1) && game.getFields().get(i).getI() == game.getFields().get(i + game.getN()).getI()){finish = false;}
            if ((i + 1) % game.getN() != 0 && game.getFields().get(i).getI() == game.getFields().get(i + 1).getI()){finish = false;}
        }
        if (finish){
            end(game);
        }
    }

    /**
     * Проверяет возможность сделать ход
     * @param request запрос игрока, указывающий направление
     * @return true если ход возможен
     */
    private boolean checkCanMove(Game2048 game, String request) {
        boolean canMove = false;
        switch (request) {
            case ("a"):
                for (int i = 0; i < game.getN() * game.getN() - 1; i++) {
                    int left = game.getFields().get(i).getI();
                    int right = game.getFields().get(i + 1).getI();
                    if ((i + 1) % game.getN() != 0 && (left == 0 && right != 0 || left != 0 && left == right)) {
                        canMove = true;
                        break;
                    }
                }
                break;
            case ("d"):
                for (int i = 1; i < game.getN() * game.getN(); i++) {
                    int left = game.getFields().get(i - 1).getI();
                    int right = game.getFields().get(i).getI();
                    if (i % game.getN() != 0 && (right == 0 && left != 0 || right != 0 && left == right)) {
                        canMove = true;
                        break;
                    }
                }
                break;
            case ("w"):
                for (int i = 0; i < game.getN() * (game.getN() - 1); i++) {
                    int up = game.getFields().get(i).getI();
                    int down = game.getFields().get(i + game.getN()).getI();
                    if (up == 0 && down != 0 || up != 0 && up == down) {
                        canMove = true;
                        break;
                    }
                }
                break;
            case ("s"):
                for (int i = game.getN(); i < game.getN() * game.getN(); i++) {
                    int up = game.getFields().get(i - game.getN()).getI();
                    int down = game.getFields().get(i).getI();
                    if (canMove = down == 0 && up != 0 || down != 0 && up == down) {
                        canMove = true;
                        break;
                    }
                }
                break;
        }
        return canMove;
    }

    public boolean save(Game2048 game) {
        String fileName = "gameStore";
        return serializeOutput(game, fileName);
    }

    public Game2048 load(Game2048 game) {
        String fileName = "gameStore";
        return serializeInput(game, fileName);
    }

    private boolean serializeOutput(Game2048 game, String fileName) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
            return true;
        }catch(IOException e){
            System.out.println("Exception:");
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    private Game2048 serializeInput(Game2048 game, String fileName) {
        try{
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            Game2048 loadGame = (Game2048) objectInputStream.readObject();
            objectInputStream.close();
            return loadGame;
        }catch (IOException | ClassNotFoundException e){
            System.out.println("Exception: Probably the game has not been saved yet");
            return game;
        }
    }

    public String giveMeTip(Game2048 game){
        String tip = checkCanMove(game, "a") ? "You can go Left " : "";
        tip += checkCanMove(game, "d") ? "You can go Right  " : "";
        tip += checkCanMove(game, "w") ? "You can go Up  " : "";
        tip += checkCanMove(game, "s") ? "You can go Down  " : "";
        return tip;
    }

    public String goodMove(Game2048 game){
        String moves = "awds";
        String move = "";
        String goodMove = "what can i say, press any button";
        Integer bestScore = game.getScore();
        Game2048 tempGame = convertHistoryToGame(game, new Game2048());
        for (char x : moves.toCharArray()){
            if (move(tempGame, String.valueOf(x))){
                switch (x){
                    case('a'): move = "left";
                        break;
                    case('w'): move = "up";
                        break;
                    case('d'): move = "right";
                        break;
                    case('s'): move = "down";
                        break;
                }
                if (tempGame.getScore() >= bestScore){
                    if (tempGame.getScore() == bestScore) {
                        goodMove += " or " + move;
                    }else{
                        bestScore = tempGame.getScore();
                        goodMove = move;
                    }

                }
                returnMove(tempGame);
            }
        }
        return goodMove;
    }

    private void autoSaveMoveHistory(Game2048 game) {
        Game2048 prevStep = convertHistoryToGame(game, new Game2048());
        game.getMoveHistory().addFirst(prevStep);
        if (game.getMoveHistory().size() > 5) {
            game.getMoveHistory().removeLast();
        }
    }

    public Game2048 returnMove(Game2048 game) {
        if (game.getMoveHistory().isEmpty()) {
            return game;
        }
        return convertHistoryToGame(game.getMoveHistory().removeFirst(), game);
    }

    private Game2048 convertHistoryToGame(Game2048 from, Game2048 to) {
        to.setScore(from.getScore());
        for (int i = 0; i < to.getN() * to.getN(); i++) {
            to.getFields().get(i).setI(from.getFields().get(i).getI());
        }
        return to;
    }

    private void autoSaveEveryMin(Game2048 game) {
        Thread autoSaveTread = new Thread(() -> {
            while(game.isGameOn()){
                try {
                    serializeOutput(game, "everyMinSave");
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    System.out.println("Something goes wrong)");
                }
            }
        });
        autoSaveTread.start();
    }

    public Game2048 autoLoad() {
        return serializeInput(new Game2048(), "everyMinSave");
    }

    public void end(Game2048 game){
        game.setGameOn(false);
        System.out.println("Game is over");
        System.out.println("Thank you and good luck!");
    }
}
