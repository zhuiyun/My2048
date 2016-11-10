package hades.org.androidgame2048;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends Activity {

    private MyGridLayout myGridLayout;
    private static TextView score_tv,highScore_tv;
    private static int score = 0 , highScore;
    private static SharedPreferences sp;
    private static Vibrator vibrator;
    //定义声音常量
    public static final int CONNECT_AD = 1;
    public static final int GAME_OVER_AD = 2;
    public static final int GET_SCORE_AD = 3;

    private static HashMap<Integer, Integer> soundMap = new HashMap<>();
    //游戏声音池
    private SoundPool soundPool;

    private static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MyGridLayout.ADD_SCORE:
                    //加分
                    score += msg.arg1;
                    score_tv.setText(score+"");
                    break;
                case MyGridLayout.GAME_OVER:
                    if (score > highScore) {
                        highScore_tv.setText(highScore + "");
                        SharedPreferences.Editor ed = sp.edit();
                        ed.putInt("HighScore", score);
                        ed.commit();
                        score = 0;
                        score_tv.setText(score+"");
                    }
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        myGridLayout = (MyGridLayout) findViewById(R.id.gameview);
        score_tv = (TextView) findViewById(R.id.score_tv);
        highScore_tv = (TextView) findViewById(R.id.high_score_tv);
        sp = getPreferences(MODE_PRIVATE);
        highScore = sp.getInt("HighScore", 0);
        highScore_tv.setText(highScore+"");
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
        soundMap.put(CONNECT_AD, soundPool.load(this, R.raw.connect,1));
        soundMap.put(GAME_OVER_AD, soundPool.load(this, R.raw.gameover, 1));
        soundMap.put(GET_SCORE_AD, soundPool.load(this, R.raw.get, 1));
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.restart_bt:
                myGridLayout.restartGame();
                if (score > highScore) {
                    highScore_tv.setText(highScore + "");
                    SharedPreferences.Editor ed = sp.edit();
                    ed.putInt("HighScore", score);
                    ed.commit();
                }
                score = 0;
                score_tv.setText(score+" ");
                soundPool.play(soundMap.get(GET_SCORE_AD), 1, 1, 0, 0, 1);
                vibrator.vibrate(1000);
                break;
        }
    }

    public static Handler getHandle() {
        return handler;
    }


}
