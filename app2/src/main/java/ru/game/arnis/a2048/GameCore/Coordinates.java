package ru.game.arnis.a2048.GameCore;


import ru.game.arnis.a2048.GameElements.Cell;

public class Coordinates {
    public final float x;
    public final float y;

    public Coordinates(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static boolean checkCoordinatesMatch(Cell cell1,Cell cell2){
        if (cell1.pos.x==cell2.pos.x&&cell1.pos.y==cell2.pos.y)
            return true;
        else return false;
    }
}
