package ru.game.arnis.a2048.GameCore;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ru.game.arnis.a2048.Game;

import java.util.ArrayList;

/**
 * Created by arnis on 10.06.2016.
 */
public class ImageQueue {
    private ArrayList<ImageView> arr;

    public ImageQueue(RelativeLayout grid) {
        int vol = Game.GAME_SIZE*Game.GAME_SIZE;
        this.arr = new ArrayList<>(vol);
        for (int i = 0; i<vol; i++) {
            ImageView iv = new ImageView(grid.getContext());
            iv.setVisibility(View.INVISIBLE);
            this.arr.add(iv);
            grid.addView(iv);
        }
    }

    public void setParams(RelativeLayout.LayoutParams params){
        for (ImageView iv:arr){
            iv.setLayoutParams(params);
        }
    }

    public ImageView get(){
        ImageView get = arr.get(0);
        arr.remove(0);
        return get;
    }


    public void put(ImageView iv,boolean hideImmideately){
            arr.add(iv);
        if (hideImmideately)
            iv.setVisibility(View.INVISIBLE);
    }

}
