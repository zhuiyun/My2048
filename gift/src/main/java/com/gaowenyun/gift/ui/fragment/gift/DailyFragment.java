package com.gaowenyun.gift.ui.fragment.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.DailyRvBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.activity.GiftDetailActivity;
import com.gaowenyun.gift.ui.adapter.DailyRvAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.gaowenyun.gift.utils.GiftOnRvItemClick;
import com.gaowenyun.gift.view.GiftRecycleView;
import com.google.gson.Gson;


import java.util.List;

/**
 *
 * 每日推荐(复用)
 */
public class DailyFragment extends AbsFragment implements IVolleyResult {

    private GiftRecycleView dailyRv;
    private DailyRvAdapter dailyRvAdapter;

    public static DailyFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        DailyFragment fragment = new DailyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_daily;
    }

    @Override
    protected void initView() {
        dailyRv = byView(R.id.daily_rv);
    }

    @Override
    protected void initDatas() {
        Bundle bundle = getArguments();
        String string = bundle.getString("url");
        dailyRvAdapter = new DailyRvAdapter(context);
        GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        dailyRv.setLayoutManager(manager);
        dailyRv.setAdapter(dailyRvAdapter);
        VolleyInstance.getInstance().startRequest(string, this);
    }

    @Override
    public void success(String resultStr) {
        Gson gson = new Gson();
        DailyRvBean bean = gson.fromJson(resultStr,DailyRvBean.class);
        String imgUrl = bean.getData().getCover_image();
        final List<DailyRvBean.DataBean.ItemsBean> datas =  bean.getData().getItems();
        dailyRvAdapter.setDatas(datas, imgUrl);
        dailyRvAdapter.setGiftOnRvItemClick(new GiftOnRvItemClick() {
            @Override
            public void onRvItemClickListener(int positon, Object o) {
                Intent intent = new Intent(context, GiftDetailActivity.class);
                intent.putExtra("url", datas.get(positon - 1).getUrl());
                intent.putExtra("taobaoUrl", datas.get(positon - 1).getPurchase_url());
                intent.putExtra("imgUrl", datas.get(positon - 1).getCover_image_url());
                intent.putExtra("name", datas.get(positon - 1).getName());
                intent.putExtra("description", datas.get(positon - 1).getShort_description());
                intent.putExtra("price", datas.get(positon - 1).getPrice());
                startActivity(intent);
            }
        });
    }

    @Override
    public void failure() {

    }

}
