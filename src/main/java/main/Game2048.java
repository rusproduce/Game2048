package main;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game2048 implements Serializable{
    private final int n = 4;
    private int score;
    private List<Field> fields = new ArrayList<>();
    private LinkedList<Game2048> moveHistory = new LinkedList<>();
    private boolean gameOn;

    public LinkedList<Game2048> getMoveHistory() {
        return moveHistory;
    }

    public void setMoveHistory(LinkedList<Game2048> moveHistory) {
        this.moveHistory = moveHistory;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public int getN() {
        return n;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public Game2048() {
        for (int i = 0; i < n * n; i++){
            fields.add(new Field());
        }
    }



}
