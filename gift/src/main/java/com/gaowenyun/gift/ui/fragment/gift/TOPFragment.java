package com.gaowenyun.gift.ui.fragment.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.DailyRvBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.activity.GiftDetailActivity;
import com.gaowenyun.gift.ui.adapter.Top100RvAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.gaowenyun.gift.utils.GiftOnRvItemClick;
import com.gaowenyun.gift.view.GiftRecycleView;
import com.google.gson.Gson;


import java.util.List;

/**
 *
 * TOP100(复用)
 */
public class TOPFragment extends AbsFragment implements IVolleyResult {

    private GiftRecycleView topRv;
    private Top100RvAdapter topRvAdapter;

    public static TOPFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        TOPFragment fragment = new TOPFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_top;
    }

    @Override
    protected void initView() {
        topRv = byView(R.id.top_rv);
    }

    @Override
    protected void initDatas() {
        Bundle bundle = getArguments();
        String string = bundle.getString("url");
        topRvAdapter = new Top100RvAdapter(context);
        GridLayoutManager manager = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        topRv.setLayoutManager(manager);
        topRv.setAdapter(topRvAdapter);
        VolleyInstance.getInstance().startRequest(string, this);
    }

    @Override
    public void success(String resultStr) {
        Gson gson = new Gson();
        DailyRvBean bean = gson.fromJson(resultStr, DailyRvBean.class);
        String imgUrl = bean.getData().getCover_image();
        final List<DailyRvBean.DataBean.ItemsBean> datas = bean.getData().getItems();
        topRvAdapter.setDatas(datas, imgUrl);
        topRvAdapter.setGiftOnRvItemClick(new GiftOnRvItemClick() {
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
