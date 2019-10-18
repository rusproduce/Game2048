package main;

import java.io.Serializable;

public class Field implements Serializable{
    private int i = 0;
    private boolean empty = true;

    public boolean isEmpty() {
        return empty;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
        empty = i == 0;

    }
}
