package com.example.danmu;

import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("gao", "方法名:onCreate--- 类名MainActivity");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("gao", "方法名:onDestroy--- 类名MainActivity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("gao", "方法名:onStart--- 类名MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("gao", "方法名:onRestart--- 类名MainActivity");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("gao", "方法名:onPause--- 类名MainActivity");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("gao", "方法名:onStop--- 类名MainActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("gao", "方法名:onResume--- 类名MainActivity");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("gao", "方法名:onConfigurationChanged--- 类名MainActivity");
    }
}
