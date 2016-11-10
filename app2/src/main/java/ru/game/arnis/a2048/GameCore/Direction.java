package ru.game.arnis.a2048.GameCore;

import android.view.GestureDetector;
import android.view.MotionEvent;

import ru.game.arnis.a2048.GameElements.Grid;

public class Direction extends GestureDetector.SimpleOnGestureListener {
    Grid gridCopy;
    public static boolean didSwipe = false;

    public Direction(Grid grid) {
        gridCopy=grid;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2,
                           float velocityX, float velocityY) {
        didSwipe=true;

        switch (getSlope(e1.getX(), e1.getY(), e2.getX(), e2.getY())) {
            case 1:
//                Log.d("happy", "top");
                gridCopy.moveDirection(1);
                return true;
            case 2:
//                Log.d("happy", "left");
                gridCopy.moveDirection(2);
                return true;
            case 3:
//                Log.d("happy", "down");
                gridCopy.moveDirection(3);
                return true;
            case 4:
//                Log.d("happy", "right");
                gridCopy.moveDirection(4);
                return true;
        }
        return false;
    }

    private int getSlope(float x1, float y1, float x2, float y2) {
        Double angle = Math.toDegrees(Math.atan2(y1 - y2, x2 - x1));
        if (angle > 45 && angle <= 135)
            // top
            return 1;
        if (angle >= 135 && angle < 180 || angle < -135 && angle > -180)
            // left
            return 2;
        if (angle < -45 && angle>= -135)
            // down
            return 3;
        if (angle > -45 && angle <= 45){
            // right
            return 4;}
        return 0;
    }
}
