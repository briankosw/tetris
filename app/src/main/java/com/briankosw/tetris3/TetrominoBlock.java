package com.briankosw.tetris3;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by BrianKo on 07/04/2017.
 */

public class TetrominoBlock {
    protected TetrisCell[][] tetrominoBlock;
    private Tetromino tetrominoType;
    private String color;

    public TetrominoBlock(Tetromino tetromino, String color) {
        createTetrominoBlock(tetromino);
        this.tetrominoType = tetromino;
        this.color = color;
    }

    private void createTetrominoBlock(Tetromino tetromino) {
        switch (tetromino) {
            case I_BLOCK:
                tetrominoBlock = new TetrisCell[1][4];
                populateBlock();
                tetrominoBlock[0][0] = new TetrisCell(1);
                tetrominoBlock[0][1] = new TetrisCell(2);
                tetrominoBlock[0][2] = new TetrisCell(1);
                tetrominoBlock[0][3] = new TetrisCell(1);
                break;
            case O_BLOCK:
                tetrominoBlock = new TetrisCell[2][2];
                populateBlock();
                tetrominoBlock[0][0] = new TetrisCell(1);
                tetrominoBlock[0][1] = new TetrisCell(1);
                tetrominoBlock[1][0] = new TetrisCell(1);
                tetrominoBlock[1][1] = new TetrisCell(1);
                break;
            case T_BLOCK:
                tetrominoBlock = new TetrisCell[2][3];
                populateBlock();
                tetrominoBlock[0][1] = new TetrisCell(1);
                tetrominoBlock[1][1] = new TetrisCell(2);
                tetrominoBlock[1][0] = new TetrisCell(1);
                tetrominoBlock[1][2] = new TetrisCell(1);
                break;
            case S_BLOCK:
                tetrominoBlock = new TetrisCell[2][3];
                populateBlock();
                tetrominoBlock[0][1] = new TetrisCell(1);
                tetrominoBlock[0][2] = new TetrisCell(1);
                tetrominoBlock[1][0] = new TetrisCell(1);
                tetrominoBlock[1][1] = new TetrisCell(2);
                break;
            case Z_BLOCK:
                tetrominoBlock = new TetrisCell[2][3];
                populateBlock();
                tetrominoBlock[0][0] = new TetrisCell(1);
                tetrominoBlock[0][1] = new TetrisCell(1);
                tetrominoBlock[1][1] = new TetrisCell(2);
                tetrominoBlock[1][2] = new TetrisCell(1);
                break;
            case J_BLOCK:
                tetrominoBlock = new TetrisCell[3][2];
                populateBlock();
                tetrominoBlock[0][1] = new TetrisCell(1);
                tetrominoBlock[1][1] = new TetrisCell(2);
                tetrominoBlock[2][0] = new TetrisCell(1);
                tetrominoBlock[2][1] = new TetrisCell(1);
                break;
            case L_BLOCK:
                tetrominoBlock = new TetrisCell[3][2];
                populateBlock();
                tetrominoBlock[0][0] = new TetrisCell(1);
                tetrominoBlock[1][1] = new TetrisCell(2);
                tetrominoBlock[0][1] = new TetrisCell(1);
                tetrominoBlock[2][1] = new TetrisCell(1);
                break;
        }
    }

    public TetrisCell getCell(int row, int col) {
        return tetrominoBlock[row][col];
    }

    public void rotateBlock(){
        if (!(this.tetrominoType == Tetromino.O_BLOCK)) {
            int pivotX = (int)getPivot().keySet().toArray()[0];
            int pivotY = (int)getPivot().values().toArray()[0];
            HashMap<Integer, Integer> futureFillCells = new HashMap<>();
            futureFillCells.put(pivotX, pivotY);
            for (int row = 0; row < tetrominoBlock.length; row++) {
                for (int col = 0; col < tetrominoBlock[0].length; col++) {
                    if (tetrominoBlock[row][col].isFilled()) {
                        HashMap<Integer, Integer> rotatedCell =
                                rotationOperation(row, col, pivotX, pivotY);
                        int newX = (int)rotatedCell.keySet().toArray()[0];
                        int newY = (int)rotatedCell.values().toArray()[0];
                        futureFillCells.put(newX, newY);
                    }
                }
            }
            populateBlock();
            Iterator iterator = futureFillCells.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry)iterator.next();
                int x = (int)mapEntry.getKey();
                int y = (int)mapEntry.getValue();
                if (x == 1 && y == 1) {
                    tetrominoBlock[x][y] = new TetrisCell(2);
                } else {
                    tetrominoBlock[x][y] = new TetrisCell(1);
                }
            }
        }
    }

    public int blockWidth() {
        return tetrominoBlock[0].length;
    }

    public int blockHeight() {
        return tetrominoBlock.length;
    }

    public HashMap<Integer, Integer> getPivot() {
        HashMap<Integer, Integer> pivotCoord = new HashMap<>();
        for (int row = 0; row < tetrominoBlock.length; row++) {
            for (int col = 0; col < tetrominoBlock[0].length; col++) {
                if (tetrominoBlock[row][col].isPivot())
                    pivotCoord.put(row, col);
                    return pivotCoord;
            }
        }
        return null;
    }

    public HashMap<Integer, Integer> rotationOperation(int x, int y, int pivotX, int pivotY) {
        HashMap<Integer, Integer> rotatedCell = new HashMap<>();
        int tempX = x - pivotX;
        int tempY = y - pivotY;
        int translateX = (0 * tempX) + (-1 * tempY);
        int translateY = (1 * tempX) + (0 * tempY);
        rotatedCell.put(translateX + pivotX, translateY + pivotY);
        return rotatedCell;
    }

    public void populateBlock() {
        for (int row = 0; row < tetrominoBlock.length; row++) {
            for (int col = 0; col < tetrominoBlock[0].length; col++) {
                tetrominoBlock[row][col] = new TetrisCell(0);
            }
        }
    }
}
