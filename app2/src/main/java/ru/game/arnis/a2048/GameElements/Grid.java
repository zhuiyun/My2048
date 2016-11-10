package ru.game.arnis.a2048.GameElements;



import android.view.View;

import ru.game.arnis.a2048.Game;
import ru.game.arnis.a2048.GameCore.Coordinates;

import java.util.ArrayList;

public class Grid {
    public Cell[][] cells;

    public int getMergedAmount() {
        return mergedAmount;
    }

    public void resetMergedAmount() {
        this.mergedAmount = 0;
    }

    private int mergedAmount=0;
    private int size;

    public int getSize() {
        return size;
    }

    public Grid(int _size) {
        size=_size;
        cells = new Cell[size][size];
    }

    public void fillGrid(int cellSize) {
        float xOffset=0;
        float yOffset=0;

        for (int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                cells[i][j] = new Cell();
                cells[i][j].pos = new Coordinates(0+xOffset,0+yOffset);
                xOffset+=cellSize;
            }
            yOffset+=cellSize;
            xOffset=0;}
    }


    public void moveDirection(int direction) {
        int mergedLocal=0;
        for (int i = 0;i<size;i++) {
            Cell[] a = new Cell[size];

            switch (direction){
                case 1:
                    for (int j = 0; j < size; j++)
                        a[j] = cells[j][i];
                    break;
                case 2:
                    System.arraycopy(cells[i], 0, a, 0, size);
                    break;
                case 3:
                    for (int j = 0; j < size; j++)
                        a[j] = cells[j][i];
                    reverseArray(a);
                    break;
                case 4:
                    System.arraycopy(cells[i], 0, a, 0, size);reverseArray(a);
                    break;
            }
            int buf;
            for (int k = 1; k < size; k++) {
                buf = k;
                if (a[buf].getLook()!=null) {
                    while (buf >= 1 + mergedLocal) {
                        if (a[buf-1].getLook() == null) {
                            a[buf - 1].migrateData(a[buf]);
                        }
                        else if (buf > 0 && a[buf-1].getLvl()<17 && a[buf].getLvl()<17 && a[buf - 1].getLook() != null && a[buf - 1].getLvl() == a[buf].getLvl() && a[buf - 1].getLvl() + a[buf].getLvl() > 1) {
                            a[buf - 1].mergeWithCell(a[buf]);
                            mergedLocal++;
                            mergedAmount++;
                            break;
                        }
                        buf--;
                        if (k!=buf) a[buf].animateMigration();
                    }
                }
            }
            mergedLocal=0;
        }
    }

    public void reverseArray(Cell[] validData){
        Cell temp;
        for(int i = 0; i < validData.length / 2; i++)
        {
            temp = validData[i];
            validData[i] = validData[validData.length - i - 1];
            validData[validData.length - i - 1] = temp;
        }
    }

    public void undo(ArrayList<Cell> undoStateCells) {
        for (int i = 0; i < Game.GAME_SIZE; i++)
            for (int j = 0; j < Game.GAME_SIZE; j++){
                cells[i][j].wipeCell();
                if (undoStateCells.size()>0&&Coordinates.checkCoordinatesMatch(cells[i][j],undoStateCells.get(0))){
                    cells[i][j]=undoStateCells.get(0);
                    cells[i][j].setLook(Game.cellQueue.get());
                    cells[i][j].getLook().setX(cells[i][j].pos.x);
                    cells[i][j].getLook().setY(cells[i][j].pos.y);
                    cells[i][j].updateView();
                    cells[i][j].getLook().setScaleX(0);
                    cells[i][j].getLook().setScaleY(0);
                    cells[i][j].getLook().setVisibility(View.VISIBLE);
                    cells[i][j].getLook().animate().scaleXBy(1).scaleYBy(1).setDuration(200).start();
                    undoStateCells.remove(0);
                }

            }
    }

    public void removeTrash() {
        for (int i = 0; i < Game.GAME_SIZE; i++)
            for (int j = 0; j < Game.GAME_SIZE; j++){
                if (cells[i][j].getLvl()<=2){
                    switch (cells[i][j].getLvl()){
                        case 1: Game.statsRub.setAmount(Game.statsRub.getAmount()-0.1f);break;
                        case 2: Game.statsRub.setAmount(Game.statsRub.getAmount()-0.5f);break;
                    }
                    cells[i][j].wipeCell();
                }
            }
    }
}
