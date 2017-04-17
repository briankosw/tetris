package com.briankosw.tetris3;

/**
 * Created by BrianKo on 07/04/2017.
 */
public class TetrisCell {
    private int cellValue;
    private boolean landed;

    public TetrisCell(int cellValue) {
        this.cellValue = cellValue;
        this.landed = false;
    }

    public boolean isFilled() {
        return (cellValue != 0);
    }

    public boolean isPivot() {
        return (cellValue == 2);
    }

    public void setLanded() {
        this.landed = true;
    }
}
