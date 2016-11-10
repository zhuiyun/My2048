package com.example.my2048;


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
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/10 0010.
 */
public class MyGameLayout extends GridLayout {
    public static final int ADD_SCORE = 001;
    public static final int EXCHANGE = 002;
    public static final int AGAIN = 003;
    float startX, startY, endX, endY;//滑动的起始值及终点值
    int width;//每个卡片的宽度
    List<Point> list = new ArrayList<>();//所有空点的集合
    CardView[][] card = new CardView[4][4];//存放卡片
    int column = 4;
    Handler handler;

    public MyGameLayout(Context context) {
        super(context);
    }

    public MyGameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        width = getMeasuredWidth() / 4 - 10;
        width = (Math.min(w, h) - 10) / 4;
        initView();
    }

    private void initView() {
        handler = MainActivity.getHandler();
        setBackgroundColor(0xffbbada0);
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                CardView cardView = new CardView(getContext());
                addView(cardView, width, width);
                card[i][j] = cardView;
                cardView.setNum(0);

            }

        }
        addGameView();
        addGameView();
    }

    //在空的位置中随机选点设置数字
    private void addGameView() {
        list.clear();
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                if (card[i][j].getNum() == 0) {
                    Point point = new Point();
                    point.x = i;
                    point.y = j;
                    list.add(point);
                }
            }
        }
        Point point = list.get((int) (Math.random() * list.size()));
        card[point.x][point.y].setNum(2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float offsetX = event.getX() - startX;
                float offsetY = event.getY() - startY;
                if (Math.abs(offsetX) > Math.abs(offsetY)) {
                    //说明是在水平轴滑动
                    if (offsetX < -5) {
                        //左滑动
                        moveLeft();
                    } else if (offsetX > 5) {
                        //右滑动
                        moveRight();
                    }
                } else {
                    //说明是在垂直方向滑动
                    if (offsetY < -5) {
                        //上滑动
                        moveUp();
                    } else if (offsetY > 5) {
                        //下滑动
                        moveDown();
                    }
                }
        }
        return true;
    }

    boolean isMerge;

    private void moveRight() {
        Log.e("gao", "方法名:moveRight--- 类名MyGameLayout");
        isMerge = false;
        for (int i = column - 1; i >= 0; i--) {
            for (int j = column - 1; j >= 0; j--) {
                for (int z = j - 1; z >= 0; z--) {
                    if (card[i][z].getNum() > 0) {
                        if (card[i][j].getNum() == 0) {
                            card[i][j].setNum(card[i][z].getNum());
                            card[i][z].setNum(0);
                            j--;
                            isMerge = true;
                            //与主线程通信
                            Message message = Message.obtain();
                            message.what = EXCHANGE;
                            handler.sendMessage(message);
                        } else if (card[i][j].equals(card[i][z])) {
                            card[i][j].setNum(card[i][j].getNum() * 2);
                            card[i][z].setNum(0);
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = ADD_SCORE;
                            message.arg1 = card[i][j].getNum();
                            handler.sendMessage(message);
                            checkWin();
                        }
                        break;
                    }
                }
            }
        }
        if (isMerge) {
            addGameView();
            checkFail();
        }
    }

    private void moveLeft() {
        Log.e("gao", "方法名:moveLeft--- 类名MyGameLayout");
        isMerge = false;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                for (int z = j + 1; z < column; z++) {
                    if (card[i][z].getNum() > 0) {
                        if (card[i][j].getNum() == 0) {
                            card[i][j].setNum(card[i][z].getNum());
                            card[i][z].setNum(0);
                            j++;
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = EXCHANGE;
                            handler.sendMessage(message);
                        } else if (card[i][j].equals(card[i][z])) {
                            card[i][j].setNum(card[i][z].getNum() * 2);
                            card[i][z].setNum(0);
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = ADD_SCORE;
                            message.arg1 = card[i][j].getNum();
                            handler.sendMessage(message);
                            checkWin();
                        }
                        break;
                    }
                }
            }
        }
        if (isMerge) {
            addGameView();
            checkFail();
        }
    }

    private void moveDown() {
        Log.e("gao", "方法名:moveDown--- 类名MyGameLayout");
        isMerge = false;
        for (int i = column - 1; i >= 0; i--) {
            for (int j = column - 1; j >= 0; j--) {
                for (int z = j - 1; z >= 0; z--) {
                    if (card[z][i].getNum() > 0) {
                        if (card[j][i].getNum() == 0) {
                            card[j][i].setNum(card[z][i].getNum());
                            card[z][i].setNum(0);
                            j--;
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = EXCHANGE;
                            handler.sendMessage(message);
                        } else if (card[z][i].equals(card[j][i])) {
                            card[j][i].setNum(card[j][i].getNum() * 2);
                            card[z][i].setNum(0);
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = ADD_SCORE;
                            message.arg1 = card[j][i].getNum();
                            handler.sendMessage(message);
                            checkWin();
                        }
                        break;
                    }
                }
            }
        }
        if (isMerge) {
            addGameView();
            checkFail();
        }
    }

    private void moveUp() {
        Log.e("gao", "方法名:moveUp--- 类名MyGameLayout");
        isMerge = false;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                for (int z = j + 1; z < column; z++) {
                    if (card[z][i].getNum() > 0) {
                        if (card[j][i].getNum() == 0) {
                            card[j][i].setNum(card[z][i].getNum());
                            card[z][i].setNum(0);
                            j++;
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = EXCHANGE;
                            handler.sendMessage(message);
                        } else if (card[j][i].equals(card[z][i])) {
                            card[j][i].setNum(card[z][i].getNum() * 2);
                            card[z][i].setNum(0);
                            isMerge = true;
                            Message message = Message.obtain();
                            message.what = ADD_SCORE;
                            message.arg1 = card[z][i].getNum();
                            handler.sendMessage(message);
                            checkWin();
                        }
                        break;
                    }
                }
            }
        }
        if (isMerge) {
            addGameView();
            checkFail();

        }
    }


    public boolean checkFail() {
        boolean isFail = true;
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                if ((card[i][j].getNum() == 0)
                        || i > 0 && card[i][j].equals(card[i - 1][j])
                        || j > 0 && card[i][j].equals(card[i][j - 1])) {
                    isFail = false;
                }
            }
        }
        if (isFail) {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    removeAllViews();
                    initView();
                    Message message = new Message();
                    message.what = AGAIN;
                    handler.sendMessage(message);
                }
            }).setTitle("您已失败,是否重新开始").setCancelable(false).create();
            dialog.show();
        }
        return isFail;
    }

    public void checkWin() {
        for (int i = 0; i < column; i++) {
            for (int j = 0; j < column; j++) {
                if (card[i][j].getNum() == 2048) {
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle("恭喜你").setPositiveButton("再来一次", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    removeAllViews();
                                    initView();
                                }
                            }).create();
                    dialog.show();
                }
            }
        }
    }



}
