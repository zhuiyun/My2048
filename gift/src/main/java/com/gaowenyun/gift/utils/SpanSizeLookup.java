package com.gaowenyun.gift.utils;

import android.support.v7.widget.GridLayoutManager;

/**
 *
 * RecyclerView加头尾工具类
 */
public class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private final GridLayoutManager layoutManager;

    public SpanSizeLookup(GridLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
    @Override
    public int getSpanSize(int position) {
        position = position == layoutManager.getItemCount() ? layoutManager.getSpanCount() : 1;
        return position;
    }
}
