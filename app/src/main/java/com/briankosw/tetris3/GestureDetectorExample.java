package com.briankosw.tetris3;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * GestureDetectorExample class for demonstrating novel API, GestureDetector
 */
public class GestureDetectorExample extends Activity{
    private GestureDetector gestureDetector;

    /**
     * onCreate method that instantiates gestureDetector object
     *
     * @param savedInstanceState savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture);
        TetrisGestureDetector tetrisGestureDetector = new TetrisGestureDetector();
        gestureDetector = new GestureDetector(this, tetrisGestureDetector);
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

    /**
     * Custom GestureDetector class created specifically for this Activity
     */
    class TetrisGestureDetector implements android.view.GestureDetector.OnGestureListener {

        /**
         * Overridden onFling method that handles flinging/swiping gestures
         *
         * @param e1 MotionEvent object caused by gesture on screen
         * @param e2 Another MotionEvent object caused by gesture on screen
         * @param velocityX float representing fling velocity in x direction
         * @param velocityY float representing fling velocity in y direction
         * @return true if onFling was called
         */
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() < e2.getX()) {
                Toast.makeText(getApplicationContext(),
                        "LEFT TO RIGHT SWIPE",
                        Toast.LENGTH_SHORT).show();
            } else if (e1.getX() > e2.getX()) {
                Toast.makeText(getApplicationContext(),
                        "RIGHT TO LEFT SWIPE",
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        /**
         * Overridden onSingleTapUp method that handles single tap up gesture
         *
         * @param e MotionEvent object caused by gesture on screen
         * @return true if onSingleTapUp was called
         */
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Toast.makeText(getApplicationContext(), "TAP UP", Toast.LENGTH_SHORT).show();
            return true;
        }

        /**
         * Overridden onDown method that handles tap down gesture
         *
         * @param e MotionEvent object caused by gesture on screen
         * @return true if onDown method was called
         */
        @Override
        public boolean onDown(MotionEvent e) {
            Toast.makeText(getApplicationContext(), "TAP DOWN", Toast.LENGTH_SHORT).show();
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
}
