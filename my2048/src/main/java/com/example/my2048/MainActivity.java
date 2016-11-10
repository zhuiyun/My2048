package com.example.my2048;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static SoundPool soundPool;
    public static final int ADD_SCORE = 001;
    public static final int EXCHANGE = 002;
    public static final int AGAIN = 003;
    static int score = 0;
    static TextView nowScore;
    TextView bestScore;
    static int best;

    private static HashMap<Integer, Integer> soundMap = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences=getSharedPreferences("abc",MODE_PRIVATE);
        best=sharedPreferences.getInt("best",0);

        nowScore = (TextView) findViewById(R.id.nowScore);
        bestScore = (TextView) findViewById(R.id.bestScore);

        score = 0;
        nowScore.setText(score + "");
        bestScore.setText(best+"");
        AudioAttributes attr = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            attr = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(attr)
                    .setMaxStreams(10)
                    .build();
        }
        soundMap.put(ADD_SCORE, soundPool.load(this, R.raw.merge,1));
        soundMap.put(EXCHANGE, soundPool.load(this, R.raw.move, 1));


    }


    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ADD_SCORE: {
                    score += msg.arg1;
                    if(score>best){
                        best=score;
                    }
                    nowScore.setText(score + "");
                    soundPool.play(soundMap.get(ADD_SCORE), 1, 1, 0, 0, 1);
                }
                break;
                case EXCHANGE: {
                    soundPool.play(soundMap.get(EXCHANGE), 1, 1, 0, 0, 1);
                }break;
                case AGAIN:{
                    score=0;
                }
            }
        }
    };


    public static Handler getHandler() {
        return handler;
    }
    private static boolean isExit = false;
    /**
     * 监听返回键
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    AlertDialog dialog;
    private void exit() {
         dialog=new AlertDialog.Builder(this).setTitle("确定要离开吗")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                })
                 .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         SharedPreferences shared=getSharedPreferences("abc",MODE_PRIVATE);
                         SharedPreferences.Editor editor = shared.edit();
                         editor.putInt("best",best);
                         editor.commit();
                         finish();
                     }
                 }).create();
        dialog.show();
    }
}
