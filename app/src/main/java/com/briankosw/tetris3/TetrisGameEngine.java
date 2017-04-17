package com.briankosw.tetris3;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TetrisGameEngine extends Activity  {
    private GameView gameView;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TetrisGestureDetector tetrisGestureDetector = new TetrisGestureDetector();
        gestureDetector = new GestureDetector(this, tetrisGestureDetector);
        gameView = new GameView(this);

        setContentView(gameView);
    }

    class GameView extends SurfaceView implements Runnable {
        private Thread gameThread = null;
        private SurfaceHolder gameSurfaceHolder;
        private volatile boolean playing;
        private Canvas canvas;
        private Paint paint;
        private long timeThisFrame;
        private long fps;
        private TetrisGrid tetrisGrid;

        public GameView (Context context) {
            super(context);
            setBackgroundResource(R.drawable.tetrisbackground);
            gameSurfaceHolder = getHolder();
            paint = new Paint();
            playing = true;
            tetrisGrid = new TetrisGrid();
        }

        @Override
        public void run() {
            while (playing) {
                long startFrameTime = System.currentTimeMillis();
                update();
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame > 0) {
                    fps = 1000 / timeThisFrame;
                }
                try {
                    Thread.sleep(1000);
                    tetrisGrid.drop();
                } catch (InterruptedException i) {
                    Log.e("TetrisGameEngine", i.getMessage());
                }
            }
        }

        public void update(MotionEvent... motionEvent) {
            if (tetrisGrid.activeCells.isEmpty()) {
                tetrisGrid.addRandomTetrominoBlock((int)System.currentTimeMillis());
            }
            tetrisGrid.update(motionEvent);
        }

        public void draw() {
            if (gameSurfaceHolder.getSurface().isValid()) {
                canvas = gameSurfaceHolder.lockCanvas();
                gameSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

        public void resume() {
            playing = true;
            gameThread = new Thread(this);
            gameThread.start();
        }

        public void pause() {
            //when the game is paused
            //setting the variable to false
            playing = false;
            try {
                //stopping the thread
                gameThread.join();
            } catch (InterruptedException e) {
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    /**
     * Custom GestureDetector class created specifically for this Activity
     */
    public class TetrisGestureDetector implements android.view.GestureDetector.OnGestureListener {

        /**
         * Overridden onFling method that handles flinging/swiping gestures
         *
         * @param motionEvent1 MotionEvent object caused by gesture on screen
         * @param motionEvent2 Another MotionEvent object caused by gesture on screen
         * @param velocityX float representing fling velocity in x direction
         * @param velocityY float representing fling velocity in y direction
         * @return true if onFling was called
         */
        @Override
        public boolean onFling(MotionEvent motionEvent1, MotionEvent motionEvent2, float velocityX, float velocityY) {
            gameView.update(motionEvent1, motionEvent2);
            return true;
        }

        /**
         * Overridden onSingleTapUp method that handles single tap up gesture
         *
         * @param motionEvent MotionEvent object caused by gesture on screen
         * @return true if onSingleTapUp was called
         */
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        /**
         * Overridden onDown method that handles tap down gesture
         *
         * @param motionEvent MotionEvent object caused by gesture on screen
         * @return true if onDown method was called
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            gameView.update(motionEvent);
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public void onShowPress(MotionEvent e) {

        }
    }

    /**
     * Overridden onTouchEvent that calls super method
     *
     * @param event MotionEvent object caused by gestures on screen
     * @return returns the super method
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}
