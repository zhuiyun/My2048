package com.gaowenyun.gift.ui.activity;

/**
 *
 * Activity基类
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public abstract class AbsBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(setLayout());// 订制流程

        initViews();// 初始化组件

        initDatas(); // 初始化数据
    }

    /**
     * - 设置布局文件
     * <p/>
     * - @return R.layout.xx
     */
    protected abstract int setLayout();

    /**
     * 初始化组件
     */
    protected abstract void initViews();

    /**
     * 初始化数据
     */
    protected abstract void initDatas();

    /**
     * 简化findViewById
     */
    protected <T extends View> T byView(int resId) {
        return (T) findViewById(resId);
    }

    /**
     * 跳转不传值
     */
    protected void goTo(Context from, Class<? extends AbsBaseActivity> to) {
        startActivity(new Intent(from, to));
    }

    /**
     * 跳转传值
     *
     * @Bundle : 轻量级的存储类
     * 存储一些key-value形式的数据
     */
    protected void goTo(Context from, Class<? extends AbsBaseActivity> to, Bundle extras) {
        Intent intent = new Intent(from, to);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * Activity结束动画
     */
    @Override
    public void finish() {
        super.finish();

//        overridePendingTransition(android.support.v7.appcompat.R.anim.xx, R.anim.xx);

    }

}

