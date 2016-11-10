package hades.org.androidgame2048;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Hades on 16/10/31.
 */
public class Card extends FrameLayout{

    //保存当前数字
    private int num;
    private TextView content;

    public Card(Context context) {
        super(context);
        content = new TextView(getContext());
        content.setTextSize(32);
        LayoutParams lp = new LayoutParams(-1, -1);
        lp.setMargins(10,10,0,0);
        content.setGravity(Gravity.CENTER);
        addView(content,lp);
        setNum(0);
    }


    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num == 0) {
            content.setText(" ");
        } else {
            content.setText(getNum() + "");
        }

        switch (num) {
            case 0:
                content.setBackgroundColor(0x33ffffff);
                break;
            case 2:
                content.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                content.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                content.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                content.setBackgroundColor(0xfff59563);
                break;
            case 32:
                content.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                content.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                content.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                content.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                content.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                content.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                content.setBackgroundColor(0xffedc22e);
                break;
            default:
                content.setBackgroundColor(0x33ffffff);
                break;
        }
    }


    public boolean equals(Card card) {
        return card.getNum() == getNum() ? true : false;
    }

    public void merge(Card card) {
        if (this.equals(card)) {
            setNum(card.getNum() + getNum());
        } else {
            System.out.println("not equals can't merge");
        }
    }
}
