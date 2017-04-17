package com.briankosw.tetris3;

import android.graphics.Color;
import android.view.MotionEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 * Created by BrianKo on 07/04/2017.
 */

public class TetrisGrid {
    private TetrisCell[][] tetrisGrid;
    private static final int spawnLocationCol = 6;
    private String[] colorHex = {"CYAN", "YELLOW", "PURPLE", "GREEN", "RED", "BLUE", "ORANGE"};
    protected HashMap<Integer, Integer> activeCells;
    private Tetromino currTetromino;
    private TetrominoBlock currTetrominoBlock;

    public TetrisGrid() {
        this.tetrisGrid = new TetrisCell[19][10];
        populateGrid();
        activeCells = new HashMap<>();
    }

    public void populateGrid() {
        for (int row = 0; row < tetrisGrid.length; row++) {
            for (int col = 0; col < tetrisGrid[0].length; col++) {
                tetrisGrid[row][col] = new TetrisCell(0);
            }
        }
    }

    public boolean isLineFull(int row) {
        for (int col = 0; col < tetrisGrid[0].length; col++) {
            if (!tetrisGrid[row][col].isFilled()) {
                return false;
            }
        }
        return true;
    }

    public HashSet<Integer> isAnyLineFull() {
        HashSet<Integer> fullRows = new HashSet<>();
        for (int row = 0; row < tetrisGrid.length; row++) {
            if (isLineFull(row)) {
                fullRows.add(row);
            }
        }
        return fullRows;
    }

    public String randomColor(int seed) {
        Random random = new Random(seed);
        return colorHex[(int)(random.nextDouble()*colorHex.length)];
    }

    public TetrisCell addCell(int row, int col, TetrisCell tetrisCell) {
        tetrisGrid[row][col] = tetrisCell;
        activeCells.put(row, col);
        return tetrisCell;
    }

    public void addRandomTetrominoBlock(int seed) {
        Tetromino tetromino = Tetromino.randomTetromino(seed);
        currTetromino = tetromino;
        String color = randomColor(seed);
        TetrominoBlock tetrominoBlock = new TetrominoBlock(tetromino, color);
        currTetrominoBlock = tetrominoBlock;
        for (int row = tetrominoBlock.blockHeight(); row >= 0; row--) {
            for (int col = 0; col < tetrominoBlock.blockWidth(); col++) {
                addCell(row, spawnLocationCol + col, tetrominoBlock.getCell(row, col));
            }
        }
    }

    public boolean isCollide() {
        Iterator iterator = activeCells.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry)iterator.next();
            int row = (int)mapEntry.getKey();
            int col = (int)mapEntry.getValue();
            if (tetrisGrid[row+1][col].isFilled()) {
                return true;
            }
        }
         return false;
    }

    public void drop() {
        Iterator iterator = activeCells.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            int currRow = (int) mapEntry.getKey();
            int currCol = (int) mapEntry.getValue();
            tetrisGrid[currRow + 1][currCol] = tetrisGrid[currRow][currCol];
            tetrisGrid[currRow][currCol] = new TetrisCell(0);
        }
    }

    public void rotateBlock() {
        if (!(currTetromino == Tetromino.O_BLOCK)) {
            HashMap<Integer, Integer> pivotPair = getPivot();
            int pivotX = (int)pivotPair.keySet().toArray()[0];
            int pivotY = (int)pivotPair.values().toArray()[0];
            HashMap<Integer, Integer> futureFillCells = new HashMap<>();
            Iterator iterator = activeCells.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry)iterator.next();
                int row = (int)mapEntry.getKey();
                int col = (int)mapEntry.getValue();
                if (row != pivotX && col != pivotY) {
                    HashMap<Integer, Integer> rotatedCell = rotationOperation(row, col,
                                                                              pivotX, pivotY);
                    int newX = (int)rotatedCell.keySet().toArray()[0];
                    int newY = (int)rotatedCell.values().toArray()[0];
                    futureFillCells.put(newX, newY);
                }
            }
            activeCells.clear();
            iterator = futureFillCells.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry mapEntry = (Map.Entry)iterator.next();
                int row = (int)mapEntry.getKey();
                int col = (int)mapEntry.getValue();
                tetrisGrid[row][col] = new TetrisCell(1);
                activeCells.put(row, col);
            }
        }
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

    public void update(MotionEvent... motionEvent) {
        if (motionEvent.length == 0) {
            if (!isCollide()) {
                drop();
            } else {
                activeCells.clear();
            }
        } else if (motionEvent.length == 1) {
            if ((motionEvent[0].getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
                drop();
            }
        } else {
            rotateBlock();
        }
    }

    public HashMap<Integer, Integer> getPivot() {
        HashMap<Integer, Integer> pivotPair = new HashMap<>();
        Iterator iterator = activeCells.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry mapEntry = (Map.Entry)iterator.next();
            int row = (int)mapEntry.getKey();
            int col = (int)mapEntry.getValue();
            if (tetrisGrid[row][col].isPivot()) {
                pivotPair.put(row, col);
                return pivotPair;
            }
        }
        return null;
    }
}
