package com.example.a2048;


//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易  
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//                  不见满街漂亮妹，哪个归得程序员？  


import android.content.Context;

import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/11/4 0004.
 */
public class Card extends FrameLayout {
    TextView card;
    int num;

    public Card(Context context) {
        super(context);
        card = new TextView(getContext());
        card.setTextSize(30);
        FrameLayout.LayoutParams params = new LayoutParams(-1, -1);
        params.setMargins(10, 10, 0, 0);
        card.setGravity(Gravity.CENTER);
        addView(card,params);
        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int temp) {
        num=temp;
        if (temp == 0) {
            card.setText("");

        } else {
            card.setText(getNum()+"");
        }

        switch (num) {
            case 0:
                card.setBackgroundColor(0x33ffffff);
                break;
            case 2:
                card.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                card.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                card.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                card.setBackgroundColor(0xfff59563);
                break;
            case 32:
                card.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                card.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                card.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                card.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                card.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                card.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                card.setBackgroundColor(0xffedc22e);
                break;
            default:
                card.setBackgroundColor(0x33ffffff);
                break;
        }
    }

    public boolean equals(Card card) {
        return card.getNum() == getNum();
    }

    public void merge(Card card) {
        if (this.equals(card)) {
            setNum(card.getNum() + getNum());
        }
    }
}
