package com.example.a2048;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.GridLayout;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hades on 16/10/31.
 * 自定义游戏界面布局
 */
public class GameLayout extends GridLayout {

    private int column;//定义好的列数
    private Card cards[][] = new Card[4][4];//存放卡片的矩阵
    public static final String TAG = "MyGridLayout";
    private int cardWidth;//卡片的长度
    private float startX, startY, offsetX, offsetY;//屏幕滑动坐标
    private List<Point> emptyPoints = new ArrayList<>();//空点的表
    public static final int ADD_SCORE = 0x001;
    public static final int GAME_OVER = 0x002;
    private Handler mainHandler;
    private Vibrator vibrator;
    //定义声音常量
    public static final int CONNECT_AD = 1;
    public static final int GAME_OVER_AD = 2;
    public static final int GET_SCORE_AD = 3;

    private static HashMap<Integer, Integer> soundMap = new HashMap<>();
    //游戏声音池
    private SoundPool soundPool;


    public GameLayout(Context context) {
        super(context);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //初始化
    private void initView() {
        mainHandler = MainActivity.getHandler();
        vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        column = getColumnCount();
        setBackgroundColor(0xffbbada0);
        Log.i(TAG, column + " column ");

        if (Build.VERSION.SDK_INT >= 21) {
            AudioAttributes attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(attr)
                    .setMaxStreams(10)
                    .build();
        } else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        }
        soundMap.put(CONNECT_AD, soundPool.load(getContext(), R.raw.move, 1));
        soundMap.put(GAME_OVER_AD, soundPool.load(getContext(), R.raw.merge, 1));
        soundMap.put(GET_SCORE_AD, soundPool.load(getContext(), R.raw.move, 1));

        for (int y = 0; y < column; y++) {
            for (int x = 0; x < column; x++) {
                Card card = new Card(getContext());
                addView(card, cardWidth, cardWidth);
                cards[y][x] = card;
                card.setNum(0);
            }
        }
        addRandomCard();
        addRandomCard();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        cardWidth = (Math.min(w, h) - 10) / 4;
        initView();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                offsetX = event.getX() - startX;
                offsetY = event.getY() - startY;
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

    private void moveDown() {
        Log.i(TAG, "down");
        checkFail();
        boolean merge = false;
        for (int x = column - 1; x >= 0; x--) {
            for (int y = column - 1; y >= 0; y--) {
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cards[y1][x].getNum() > 0) {
                        //如果不是空位置
                        if (cards[y][x].getNum() == 0) {
                            cards[y][x].setNum(cards[y1][x].getNum());
                            cards[y1][x].setNum(0);
                            y++;
                            merge = true;
                        } else if (cards[y][x].equals(cards[y1][x])) {
                            //加分
                            Message msg = new Message();
                            msg.what = ADD_SCORE;
                            msg.arg1 = cards[y][x].getNum();
                            mainHandler.sendMessage(msg);
                            //如果两个相同就合并
                            cards[y][x].setNum(cards[y1][x].getNum() * 2);
                            cards[y1][x].setNum(0);
                            merge = true;
                            playAudio(this.CONNECT_AD);
                        }
                        break;

                    }
                }
            }
        }

        if (merge) {
            addRandomCard();
            checkFail();
        }
    }

    private void moveUp() {
        Log.i(TAG, "up");
        checkFail();
        boolean merge = false;//合并标志
        //以行为遍历
        for (int x = 0; x < column; x++) {
            for (int y = 0; y < column; y++) {
                for (int y1 = y + 1; y1 < column; y1++) {
                    if (cards[y1][x].getNum() > 0) {
                        //说明不是空位置
                        if (cards[y][x].getNum() == 0) {
                            //如果目前为止是空位置，则交换两个位置
                            cards[y][x].setNum(cards[y1][x].getNum());
                            cards[y1][x].setNum(0);//设为空
                            y--;//如果是空位置交换必须在重新检测一次
                            merge = true;
                        } else if (cards[y][x].equals(cards[y1][x])) {
                            Log.e("gao", "方法名:moveUp--- 类名MyGridLayout");
                            //加分
                            Message msg = new Message();
                            msg.what = ADD_SCORE;
                            msg.arg1 = cards[y][x].getNum();
                            mainHandler.sendMessage(msg);
                            //如果两个相同就合并
                            cards[y][x].setNum(cards[y1][x].getNum() * 2);
                            cards[y1][x].setNum(0);
                            merge = true;
                            playAudio(this.CONNECT_AD);
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomCard();
            checkFail();
        }

    }

    //检测游戏是否失败
    private void checkFail() {
        boolean isFail = true;

        ALL:
        for (int y = 0; y < column; y++) {
            for (int x = 0; x < column; x++) {
                if ((cards[y][x].getNum() == 0)
                        || (x > 0 && cards[y][x].equals(cards[y][x - 1]))
                        || (x + 1 < column && cards[y][x].equals(cards[y][x + 1]))
                        || (y > 0 && cards[y][x].equals(cards[y - 1][x]))
                        || (y + 1 < column && cards[y][x].equals(cards[y + 1][x]))) {
                    isFail = false;
                    break ALL;
                }
            }
        }

        if (isFail) {
            //游戏失败
            playAudio(GAME_OVER_AD);
            new AlertDialog.Builder(getContext()).setTitle("Game Over")
                    .setMessage("少侠，请重新来过！").setPositiveButton("再试一次", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //通知Activity游戏失败
                    mainHandler.sendEmptyMessage(GAME_OVER);
                    restartGame();
                }
            }).show();
        }
    }

    private void moveRight() {
        Log.i(TAG, "right");
        checkFail();
        boolean merge = false;
        for (int i = column-1; i >=0 ; i--) {
            for (int j = column-1; j >=0 ; j--) {
                for (int z = j-1; z >=0 ; z--) {
                    if(cards[i][z].getNum()>0){
                        if(cards[i][j].getNum()==0){
                            cards[i][j].setNum(cards[i][z].getNum());
                            cards[i][z].setNum(0);
                            j--;
                            merge=true;
                        }else if(cards[i][j].equals(cards[i][z])){
                            cards[i][j].setNum(cards[i][z].getNum()*2);
                            cards[i][z].setNum(0);
                            merge=true;
                        }
                    }
                }
            }
        }


//
        if (merge) {
            addRandomCard();
            checkFail();
        }
    }


    private void moveLeft() {
        Log.i(TAG, "left");
        checkFail();
        boolean merge = false;
        //以列遍历
        for (int y = 0; y < column; y++) {
            for (int x = 0; x < column; x++) {
                for (int x1 = x + 1; x1 < column; x1++) {
                    if (cards[y][x1].getNum() > 0) {
                        //如果位置不为空
                        if (cards[y][x].getNum() == 0) {
                            //空位置交换
                            //如果目前为止是空位置，则交换两个位置
                            cards[y][x].setNum(cards[y][x1].getNum());
                            cards[y][x1].setNum(0);//设为空
                            x--;//如果是空位置交换必须在重新检测一次
                            merge = true;
                        } else if (cards[y][x].equals(cards[y][x1])) {
                            //加分
                            Message msg = new Message();
                            msg.what = ADD_SCORE;
                            msg.arg1 = cards[y][x].getNum();
                            mainHandler.sendMessage(msg);
                            cards[y][x].setNum(cards[y][x1].getNum() * 2);
                            cards[y][x1].setNum(0);
                            merge = true;
                            playAudio(this.CONNECT_AD);
                        }
                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomCard();
            checkFail();
        }
    }

    public void restartGame() {
        removeAllViews();
        initView();
    }

    private void addRandomCard() {
        //清除上次遍历的内容
        emptyPoints.clear();
        //遍历空点
        for (int y = 0; y < column; y++) {
            for (int x = 0; x < column; x++) {
                if (cards[y][x].getNum() <= 0) {
                    Point p = new Point(x, y);
                    emptyPoints.add(p);
                }
            }
        }
        //存在空点
        if (emptyPoints.size() > 0) {
            //随即找出一个空点
            Point point = emptyPoints.get(Utils.getRandomInt(emptyPoints.size()));
            cards[point.y][point.x].setNum(Math.random() > 0.1 ? 2 : 4);
        }
    }

    public void playAudio(int TAG) {

        switch (TAG) {
            case CONNECT_AD:
                soundPool.play(soundMap.get(CONNECT_AD), 1, 1, 0, 0, 1);
                Log.i("Main", "play Audio");
                break;
            case GAME_OVER_AD:
                soundPool.play(soundMap.get(GAME_OVER_AD), 1, 1, 0, 0, 1);
                break;
            case GET_SCORE_AD:
                soundPool.play(soundMap.get(GET_SCORE_AD), 1, 1, 0, 0, 1);
                break;
        }
    }
}
