package ru.game.arnis.a2048.GameElements;

import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import ru.game.arnis.a2048.Game;
import ru.game.arnis.a2048.GameCore.Anim;
import ru.game.arnis.a2048.GameCore.Coordinates;

public class Cell {
    private ImageView look;
    public Coordinates pos;
    private int lvl;

    public int getLvl() {
        return lvl;
    }

    public Cell setLvl(int lvl) {
        this.lvl = lvl;
        return this;
    }

    public void updateView(){
        Game.addLastKnown(lvl);
        switch (lvl){
            case 1: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.kop10);break;//10kop
            case 2: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.kop50);break;//50kop+++
            case 3: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub1);break;//1rub
            case 4: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub2);break;//2rub
            case 5: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub5);break;//5rub+++
            case 6: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub10);break;//10rub
            case 7: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub50);break;//50rub+++
            case 8: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub100);break;//100rub
            case 9: this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub500);break;//500rub+++
            case 10:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub1000);break;//1000rub
            case 11:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.rub5000);break;//5000rub+++
            case 12:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd1);break;//1dol
            case 13:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd2);break;//2dol
            case 14:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd5);break;//5dol+++
            case 15:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd10);break;//10dol
            case 16:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd20);break;//20dol
            case 17:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd50);break;//50dol+++
            case 18:this.getLook().setBackgroundResource(ru.game.arnis.a2048.R.drawable.usd100);break;//100dol
        }
    }

    public ImageView getLook() {
        return look;
    }
    public void setLook(ImageView _look) {
        this.look=_look;
    }

    public Cell migrateData(Cell cell) {
        this.look=cell.look;
        this.setLvl(cell.getLvl());
        cell.lvl=0;
        cell.look=null;
        return this;
    }

    public void animateMigration(){
        this.look.animate()
                .x(this.pos.x)
                .y(this.pos.y)
                .setDuration(150)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }

    public void mergeWithCell(final Cell cell){
        Game.cellQueue.put(this.getLook(),true);
        cell.setLvl(cell.getLvl()+1);
        this.migrateData(cell);
        Anim.migration(this,this.pos);
        if (this.getLvl()<12){
            Game.statsRub.addAmountBasedOnLvl(this.getLvl(),false);
        } else Game.statsUsd.addAmountBasedOnLvl(this.getLvl(),false);
    }

    public void wipeCell() {
        if (look!=null) {
            Game.cellQueue.put(look,false);
            look.setVisibility(View.INVISIBLE);
            look=null;
        }
        lvl=0;
    }

    public void copyCell(Cell to){
        this.pos=to.pos;
        this.lvl=to.lvl;
    }


    public void inject(ImageView view, int lvl) {
        this.setLook(view);
        this.getLook().setX(this.pos.x);
        this.getLook().setY(this.pos.y);
        this.getLook().setVisibility(View.VISIBLE);
        this.setLvl(lvl).updateView();
    }
}

