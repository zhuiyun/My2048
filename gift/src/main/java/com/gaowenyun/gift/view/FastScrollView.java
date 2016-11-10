package com.gaowenyun.gift.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 *
 */
public class FastScrollView extends ScrollView {
    public FastScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public FastScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FastScrollView(Context context) {
        super(context);
    }

    /**
     * 滑动事件
     */
    @Override
    public void fling(int velocityY) {
        super.fling(velocityY * 10000);
    }
}
