package ru.game.arnis.a2048;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.Random;

import ru.game.arnis.a2048.GameCore.Anim;
import ru.game.arnis.a2048.GameCore.Direction;
import ru.game.arnis.a2048.GameCore.ImageQueue;
import ru.game.arnis.a2048.GameCore.Stats;
import ru.game.arnis.a2048.GameElements.Cell;
import ru.game.arnis.a2048.GameElements.Grid;

public class Game extends AppCompatActivity {

    private Grid grid;
    GestureDetector gd;
    public RelativeLayout gridView;
    private RelativeLayout gameView;
    private TextView bonusText;
    private int bonus=0;
    public int cellSize;
    ArrayList<Cell> undoStateCells;
    PopupWindow gameoverPopup;
    public static final String GAME_DB = "db";
    public static final String SAVE_GAME_DB = "save";
    public static final String HS = "mountr";
    public static final String LAST_KNOWN = "klown";
    public static final String SAVE_STATE = "svaeste";
    public static final String NEW_GAME_BOOL = "new_game";
    SharedPreferences.Editor editor;
    public static int lastKnown;
    int currLVL;
    int curr2048;
    boolean notify1;
    boolean notify2;
    boolean notify3;
    boolean undoDone;
    public Firebase analytics;
    boolean canSwipe = true;
    public Stats highScore;

    public static ImageQueue cellQueue;
    public static Stats statsRub;
    public static Stats statsUsd;

    private TextView showRub;
    private TextView showUsd;
    private TextView highScoreText;

    public static final int GAME_SIZE = 4;
    public static final int UNDO_COST = 20;
    public static final int REMOVE_TRASH_COST = 40;
    public static final int ADD_SPECIAL_COST = 60;
    private int search=0;
    private ImageView undo;
    private ImageView trash;
    private ImageView add;
    private View dim;
    public int size;
    private ImageView updateButon;
    private boolean gameSaved=false;
    private boolean newGame;

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        hideStatusBar();

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-9520295956795786/9134882556");


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                startNewActivity(true);
            }
        });

        requestNewInterstitial();


        gridView = (RelativeLayout)findViewById(R.id.grid_view);
        gameView = (RelativeLayout)findViewById(R.id.game_layout);
        showRub = (TextView)findViewById(R.id.text_curr1);
        showUsd = (TextView)findViewById(R.id.text_curr2);
        bonusText = (TextView) findViewById(R.id.text_bonus);
        highScoreText = (TextView)findViewById(R.id.text_record);
        undo = (ImageView)findViewById(R.id.undoMove);
        trash = (ImageView)findViewById(R.id.trash);
        add = (ImageView)findViewById(R.id.add);
        dim = findViewById(R.id.dim_view);
        updateButon = (ImageView)findViewById(R.id.update_button);

        analytics = new Firebase(this);
        cellQueue = new ImageQueue(gridView);
        undoStateCells= new ArrayList<>();

        loadLastKnown();
        loadHighScore();
        isNewGame();



        //debug help
//        bonusText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                assignBonus(4);
//            }
//        });

        gameView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    size = gridView.getWidth();
                    gridView.getLayoutParams().height= size;
                    gridView.getLayoutParams().width= size;
                    cellSize=size /GAME_SIZE;
                    cellQueue.setParams(new RelativeLayout.LayoutParams(cellSize,cellSize));
                    setupPopup(size);

                    grid = new Grid(GAME_SIZE);
                    grid.fillGrid(cellSize);
                    Direction dir = new Direction(grid);
                    gd = new GestureDetector(getApplicationContext(), dir);
                    statsRub = new Stats();
                    statsUsd = new Stats();
                    if (!newGame)
                        loadGameState();
                    else {
                        spawnRandomCell(false);
                        spawnRandomCell(false);
                    }

                    gameView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

    }

    private void isNewGame() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            newGame = bundle.getBoolean(NEW_GAME_BOOL,false);
        }
    }

    private void startNewActivity(boolean newGAME){
        Intent intent = new Intent(this,Game.class);
        intent.putExtra(NEW_GAME_BOOL,newGAME);
        startActivity(intent);
        finish();

    }

    private void requestNewInterstitial() {
//        String id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void hideStatusBar(){
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void loadLastKnown() {
        SharedPreferences prefs = getSharedPreferences(GAME_DB,MODE_PRIVATE);
        lastKnown = prefs.getInt(LAST_KNOWN,1);
    }

    public static void addLastKnown(int lvl){
        if (lvl>lastKnown){
            lastKnown=lvl;
        }
    }


    private void loadHighScore() {
        SharedPreferences prefs = getSharedPreferences(GAME_DB,MODE_PRIVATE);
        highScore = new Stats();
        highScore.setAmount(prefs.getFloat(HS,0f));
        highScoreText.setText(highScore.getStrAmount());
    }

    private void setupPopup(int width) {
        View popup = LayoutInflater.from(this).inflate(R.layout.game_over,null);
        gameoverPopup = new PopupWindow(popup, width*2/3, width*2/3);
        final TextView getMoney = (TextView) popup.findViewById(R.id.get_money);
        final ImageView exit = (ImageView) popup.findViewById(R.id.close_button2);
        getMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anim.popupAnimation(getMoney,true);
                if (getOverallScore()>highScore.getAmount()){
                    Anim.popupAnimation(highScoreText,false);
                    highScore.setAmount(getOverallScore());
                    highScoreText.setText(highScore.getStrAmount());
                    saveHS();
                }
                highScoreText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"new_game_button").logEvent("Game_over");
                        if (mInterstitialAd.isLoaded()) {
                            analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"interstitial_ad_showed").logEvent("Ads");
                            mInterstitialAd.show();
                        } else {
                            analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"ad_not_loaded").logEvent("Ads");
                            startNewActivity(true);
                        }
                    }
                },800);

            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Anim.popupAnimation(exit,true);
                canSwipe=true;
                Anim.undimAnim(dim);
                exit.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"continue_button").logEvent("Game_over");
                        gameoverPopup.dismiss();
                    }
                },400);
            }
        });
    }

    private float getOverallScore(){
        return statsRub.getAmount()+(statsUsd.getAmount()*60f);
    }

    private void saveHS(){
        editor = getSharedPreferences(GAME_DB,MODE_PRIVATE).edit();
        editor.putFloat(HS,highScore.getAmount());
        editor.apply();
    }

    private void refreshStats(){
        statsUsd.clear();
        statsRub.clear();
        for (int i = 0; i < GAME_SIZE; i++)
            for (int j = 0; j < GAME_SIZE; j++) {
                if (grid.cells[i][j].getLook()!=null){
                    if (grid.cells[i][j].getLvl()<12)
                        statsRub.addAmountBasedOnLvl(grid.cells[i][j].getLvl(),true);
                    else statsUsd.addAmountBasedOnLvl(grid.cells[i][j].getLvl(),true);

                }
        }
        showUsd.setText(statsUsd.getStrAmount());
        showRub.setText(statsRub.getStrAmount());
    }

    public void spawnRandomCell(boolean ownLvl){
        Random rnd = new Random();
        int i,j;
        do{
//            Log.d("happysearch", "spawnRandomCell: "+Integer.toString(search));
            search++;
            i=rnd.nextInt(grid.getSize());
            j=rnd.nextInt(grid.getSize());
            if (search>200){
                Anim.dimAnim(dim);
                canSwipe=false;
                analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"board_full").logEvent("Game_over");
                gameoverPopup.showAtLocation(gameView, Gravity.CENTER,0,0);
                search=0;
                return;
            }
        }while (grid.cells[i][j].getLook()!=null);
        search=0;

        grid.cells[i][j].setLook(cellQueue.get());

        grid.cells[i][j].getLook().setX(grid.cells[i][j].pos.x);
        grid.cells[i][j].getLook().setY(grid.cells[i][j].pos.y);

        if (!ownLvl)
            grid.cells[i][j].setLvl(1).updateView();
        else {
            grid.cells[i][j].setLvl(rnd.nextInt(13)+3).updateView();
        }
        grid.cells[i][j].getLook().setScaleX(0);
        grid.cells[i][j].getLook().setScaleY(0);

        grid.cells[i][j].getLook().setVisibility(View.VISIBLE);
        grid.cells[i][j].getLook().animate()
                .scaleX(1)
                .scaleY(1)
                .setDuration(300)
                .setInterpolator(new AccelerateDecelerateInterpolator());


        if (!ownLvl){
            statsRub.addAmountBasedOnLvl(grid.cells[i][j].getLvl(),true);
            showRub.setText(statsRub.getStrAmount());
        }
        else {
            if (grid.cells[i][j].getLvl()<12){
                statsRub.addAmountBasedOnLvl(grid.cells[i][j].getLvl(),true);
                showRub.setText(statsRub.getStrAmount());
            } else {
                statsUsd.addAmountBasedOnLvl(grid.cells[i][j].getLvl(),true);
                showUsd.setText(statsUsd.getStrAmount());
            }
        }



    }

    //-------------------------------------------bonus section----------------------------------
    public void setBonus(int _bonus) {
        bonus = bonus + _bonus;
        bonusText.setText(Integer.toString(bonus));
    }
    public boolean useBonus(int howmany){
        if (howmany<=bonus) {
            Anim.popupAnimation(bonusText,true);
            bonus = bonus - howmany;
            bonusText.setText(Integer.toString(bonus));
            return true;
        } else return false;
    }
    public void resetBonus(){
        bonus=0;
        bonusText.setText("0");
    }
    public void assignBonus(int merged){

        switch (merged){
            case 2:setBonus(1);Anim.popupAnimation(bonusText,false);break;
            case 3:setBonus(2);Anim.popupAnimation(bonusText,false);break;
            case 4:setBonus(5);Anim.popupAnimation(bonusText,false);break;
        }
    }

//--------------------------------------------------------------------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canSwipe) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                saveCellsForUndo();
//                Log.d("happy", "last known " + Integer.toString(lastKnown));
            }

            gd.onTouchEvent(event);

            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Direction.didSwipe) {
                    spawnRandomCell(false);
                    Direction.didSwipe = false;
                }
                undoDone = false;
                assignBonus(grid.getMergedAmount());
                showUsd.setText(statsUsd.getStrAmount());
                if (highScore.getAmount() < getOverallScore()) {
                    Anim.popupAnimation(highScoreText, false);
                    highScore.setAmount(getOverallScore());
                    highScoreText.setText(highScore.getStrAmount());
                    saveHS();
                }
                notifyAvaliableBonus();
                grid.resetMergedAmount();
            }
        }
        return super.onTouchEvent(event);
    }

    private void notifyAvaliableBonus() {
        if (bonus>=UNDO_COST&&!notify1){
            notify1=true;
            undo.setImageResource(R.drawable.undo);
            Anim.popupAnimation(undo,false);
        }
        else if (bonus>=REMOVE_TRASH_COST&&!notify2){
            notify2=true;
            trash.setImageResource(R.drawable.trash);
            Anim.popupAnimation(trash,false);
        }
        else if (bonus>=ADD_SPECIAL_COST&&!notify3){
            notify3=true;
            add.setImageResource(R.drawable.add);
            Anim.popupAnimation(add,false);
        }

    }

    private void resetNotify(int bonus){
        if (bonus<UNDO_COST){
            undo.setImageResource(R.drawable.bonus_hint20);
            notify1=false;
        }
        if (bonus<REMOVE_TRASH_COST){
            trash.setImageResource(R.drawable.bonus_hint40);
            notify2=false;
        }
        if (bonus<ADD_SPECIAL_COST){
            add.setImageResource(R.drawable.bonus_hint60);
            notify3=false;
        }
    }


    @Override
    protected void onPause() {
        SharedPreferences.Editor prefs = getSharedPreferences(GAME_DB,MODE_PRIVATE).edit();
        prefs.putInt(LAST_KNOWN,lastKnown).apply();
        saveHS();
        analytics.createBundle().put(FirebaseAnalytics.Param.SCORE,highScore.getStrAmount()).logEvent("High_score");
        saveGameState();
        super.onPause();
    }

    private void loadGameState() {
        boolean ret=false;
        SharedPreferences prefs = getSharedPreferences(SAVE_GAME_DB,MODE_PRIVATE);
        setBonus(prefs.getInt(SAVE_STATE,0));
        for (int i = 0; i < GAME_SIZE; i++) {
            for (int j = 0; j < GAME_SIZE; j++) {
                int lvl = prefs.getInt(SAVE_STATE+Integer.toString(i)+Integer.toString(j),0);
                if (lvl!=0){
                    ret=true;
                    grid.cells[i][j].inject(cellQueue.get(),lvl);
                }

            }
        }
        refreshStats();
        if(!ret){
            spawnRandomCell(false);
            spawnRandomCell(false);
        }
    }

    private void saveGameState() {
        if (!gameSaved){
            SharedPreferences.Editor prefEdit = getSharedPreferences(SAVE_GAME_DB,MODE_PRIVATE).edit();
            prefEdit.clear();
            for (int i = 0; i < GAME_SIZE; i++) {
                for (int j = 0; j < GAME_SIZE; j++) {
                    if (grid.cells[i][j].getLook()!=null)
                    prefEdit.putInt(SAVE_STATE+Integer.toString(i)+Integer.toString(j),grid.cells[i][j].getLvl());
                }
            }
            prefEdit.putInt(SAVE_STATE,bonus).apply();
            gameSaved=true;
        }
    }

    @Override
    protected void onDestroy() {
        clearStatics();
        saveGameState();
        super.onDestroy();
    }

    private void clearStatics(){
//            cellQueue=null;
//            statsRub=null;
//            statsUsd=null;
    }
        //-------------------------------------------------------------------------------

    private void showToast() {
        Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_cant_use_specials),Toast.LENGTH_SHORT).show();
    }

    public void undoMove(View view) {
        Anim.popupAnimation(undo,true);
        if (!undoDone&&useBonus(UNDO_COST)){
            analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"undo").logEvent("Specials");
            undoDone=true;
            undoStats();
            grid.undo(undoStateCells);
            resetNotify(bonus);
        } else if (undoDone) Toast.makeText(getApplicationContext(),getResources().getString(R.string.toast_cant_undo),Toast.LENGTH_SHORT).show();
        else showToast();
    }
    private void undoStats(){
        statsUsd.clear();
        statsRub.clear();
        for (Cell c:undoStateCells){
            if (c.getLvl()<12)
                statsRub.addAmountBasedOnLvl(c.getLvl(),true);
            else statsUsd.addAmountBasedOnLvl(c.getLvl(),true);
        }
        showUsd.setText(statsUsd.getStrAmount());
        showRub.setText(statsRub.getStrAmount());
    }
    private void saveCellsForUndo(){
        if (bonus>=UNDO_COST-2){
            undoStateCells.clear();
            for (int i = 0; i < GAME_SIZE; i++) {
                for (int j = 0; j < GAME_SIZE; j++) {
                    if (grid.cells[i][j].getLook()!=null){
                        Cell cell = new Cell();
                        cell.copyCell(grid.cells[i][j]);
                        undoStateCells.add(cell);
                    }
                }
            }
        }
    }

    public void addHighLevel(View view) {
        Anim.popupAnimation(add,true);
        if (useBonus(ADD_SPECIAL_COST)){
            analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"add_high_level").logEvent("Specials");
            resetNotify(bonus);
            spawnRandomCell(true);
        }else showToast();
    }

    public void removeLowLevel(View view) {
        Anim.popupAnimation(trash,true);
        if (useBonus(REMOVE_TRASH_COST)){
            analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"clear_low_level").logEvent("Specials");
            resetNotify(bonus);
            grid.removeTrash();
            showUsd.setText(statsUsd.getStrAmount());
            showRub.setText(statsRub.getStrAmount());
        }else showToast();

    }

    public void info(final View view) {
        analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"open").logEvent("Info_tab");
        Anim.dimAnim(dim);
        view.setClickable(false);
        Anim.popupAnimation(view,true);
        View popup = LayoutInflater.from(this).inflate(R.layout.info_popup,null);
        final PopupWindow infoPopup = new PopupWindow(popup, size*2/3, size*2/3);
        final ImageView scrollLeft = (ImageView)popup.findViewById(R.id.scroll_left);
        final ImageView scrollRight = (ImageView)popup.findViewById(R.id.scroll_right);
        final ImageView preview = (ImageView) popup.findViewById(R.id.preview_currency);
        final ImageView close = (ImageView)popup.findViewById(R.id.close_button);
        final TextView from2048 = (TextView)popup.findViewById(R.id.from_2048);

        curr2048=2;
        currLVL=1;

        String line2 = "- "+Integer.toString(curr2048)+" -";
        from2048.setText(line2);
        preview.setImageResource(R.drawable.kop10);

        scrollLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currLVL>1){
                Anim.popupAnimation(scrollLeft,true);
                    currLVL--;
                    curr2048/=2;
                    String line2 = "- "+Integer.toString(curr2048)+" -";
                    from2048.setText(line2);
                    switch (currLVL){
                        case 1: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.kop10); else preview.setImageResource(R.drawable.hidden_coin);break;//10kop
                        case 2: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.kop50);else preview.setImageResource(R.drawable.hidden_coin);break;//50kop+++
                        case 3: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub1);else preview.setImageResource(R.drawable.hidden_coin);break;//1rub
                        case 4: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub2);else preview.setImageResource(R.drawable.hidden_coin);break;//2rub
                        case 5: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub5);else preview.setImageResource(R.drawable.hidden_coin);break;//5rub+++
                        case 6: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub10);else preview.setImageResource(R.drawable.hidden_coin);break;//10rub
                        case 7: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub50);else preview.setImageResource(R.drawable.hidden_card);break;//50rub+++
                        case 8: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub100);else preview.setImageResource(R.drawable.hidden_card);break;//100rub
                        case 9: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub500);else preview.setImageResource(R.drawable.hidden_card);break;//500rub+++
                        case 10:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub1000);else preview.setImageResource(R.drawable.hidden_card);break;//1000rub
                        case 11:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub5000);else preview.setImageResource(R.drawable.hidden_card);break;//5000rub+++
                        case 12:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd1);else preview.setImageResource(R.drawable.hidden_dollar);break;//1dol
                        case 13:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd2);else preview.setImageResource(R.drawable.hidden_dollar);break;//2dol
                        case 14:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd5);else preview.setImageResource(R.drawable.hidden_dollar);break;//5dol+++
                        case 15:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd10);else preview.setImageResource(R.drawable.hidden_dollar);break;//10dol
                        case 16:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd20);else preview.setImageResource(R.drawable.hidden_dollar);break;//20dol
                        case 17:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd50);else preview.setImageResource(R.drawable.hidden_dollar);break;//50dol+++
                        case 18:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd100);else preview.setImageResource(R.drawable.hidden_dollar);break;//100dol
                    }
                }
            }
        });

        scrollRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currLVL<18){
                Anim.popupAnimation(scrollRight,true);
                    curr2048*=2;
                    currLVL++;
                    String line2 = "- "+Integer.toString(curr2048)+" -";
                    from2048.setText(line2);
                    switch (currLVL){
                        case 1: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.kop10); else preview.setImageResource(R.drawable.hidden_coin);break;//10kop
                        case 2: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.kop50);else preview.setImageResource(R.drawable.hidden_coin);break;//50kop+++
                        case 3: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub1);else preview.setImageResource(R.drawable.hidden_coin);break;//1rub
                        case 4: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub2);else preview.setImageResource(R.drawable.hidden_coin);break;//2rub
                        case 5: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub5);else preview.setImageResource(R.drawable.hidden_coin);break;//5rub+++
                        case 6: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub10);else preview.setImageResource(R.drawable.hidden_coin);break;//10rub
                        case 7: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub50);else preview.setImageResource(R.drawable.hidden_card);break;//50rub+++
                        case 8: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub100);else preview.setImageResource(R.drawable.hidden_card);break;//100rub
                        case 9: if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub500);else preview.setImageResource(R.drawable.hidden_card);break;//500rub+++
                        case 10:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub1000);else preview.setImageResource(R.drawable.hidden_card);break;//1000rub
                        case 11:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.rub5000);else preview.setImageResource(R.drawable.hidden_card);break;//5000rub+++
                        case 12:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd1);else preview.setImageResource(R.drawable.hidden_dollar);break;//1dol
                        case 13:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd2);else preview.setImageResource(R.drawable.hidden_dollar);break;//2dol
                        case 14:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd5);else preview.setImageResource(R.drawable.hidden_dollar);break;//5dol+++
                        case 15:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd10);else preview.setImageResource(R.drawable.hidden_dollar);break;//10dol
                        case 16:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd20);else preview.setImageResource(R.drawable.hidden_dollar);break;//20dol
                        case 17:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd50);else preview.setImageResource(R.drawable.hidden_dollar);break;//50dol+++
                        case 18:if (currLVL<=lastKnown)preview.setImageResource(R.drawable.usd100);else preview.setImageResource(R.drawable.hidden_dollar);break;//100dol
                    }
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setClickable(true);
                canSwipe=true;
                Anim.popupAnimation(close,true);
                Anim.undimAnim(dim);
                preview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        infoPopup.dismiss();
                    }
                },400);
            }
        });
        canSwipe=false;
        infoPopup.showAtLocation(gameView, Gravity.CENTER,0,0);
    }

    public void newGame(final View view) {
        updateButon.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                Anim.popupAnimation(view,true);
                analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"new_game").logEvent("New_game");
                updateButon.setClickable(true);
                if (mInterstitialAd.isLoaded()) {
                    analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"interstitial_ad_showed").logEvent("Ads");
                    mInterstitialAd.show();
                } else {
                    analytics.createBundle().put(FirebaseAnalytics.Param.CONTENT_TYPE,"ad_not_loaded").logEvent("Ads");
                    startNewActivity(true);
                }
            }
        },400);

    }
}
