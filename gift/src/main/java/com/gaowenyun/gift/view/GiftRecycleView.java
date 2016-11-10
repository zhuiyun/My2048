package com.gaowenyun.gift.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 * 自定义RecyclerView
 */
public class GiftRecycleView extends RecyclerView {
    public GiftRecycleView(Context context) {
        super(context);
    }

    public GiftRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction()== MotionEvent.ACTION_MOVE) {
//            return true;
//        }
//        return false;
//    }
//



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, View.MeasureSpec.UNSPECIFIED);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
