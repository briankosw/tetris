package com.briankosw.tetris3;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by BrianKo on 07/04/2017.
 */

public enum Tetromino {
    I_BLOCK, O_BLOCK, T_BLOCK, S_BLOCK,
    Z_BLOCK, J_BLOCK, L_BLOCK;

    private static final List<Tetromino> VALUES = Collections.unmodifiableList(Arrays.asList(values()));

    public static Tetromino randomTetromino(int seed) {
        Random random = new Random();
        return VALUES.get(random.nextInt(VALUES.size()));
    }
}
