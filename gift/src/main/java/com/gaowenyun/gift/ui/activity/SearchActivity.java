package com.gaowenyun.gift.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.gaowenyun.gift.R;


/**
 *
 * 搜索详情
 */
public class SearchActivity extends AbsBaseActivity {

    private TextView cancel;

    @Override
    protected int setLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void initViews() {
        cancel = byView(R.id.cancel_tv);
    }

    @Override
    protected void initDatas() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
