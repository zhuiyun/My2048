package com.gaowenyun.gift.ui.fragment.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.GiftForGilrBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.activity.SelectionLvDetailActivity;
import com.gaowenyun.gift.ui.adapter.GiftForGirlAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.gaowenyun.gift.view.ReFlashListView;
import com.google.gson.Gson;


import java.util.List;

/**
 * Created by dllo on 16/9/9.
 * 送女票(后面开始复用)
 */
public class GiftForGirlFragment extends AbsFragment implements IVolleyResult, ReFlashListView.IReflashListener {

    private ReFlashListView girlLv;
    private GiftForGirlAdapter giftForGirlAdapter;
    private List<GiftForGilrBean.DataBean.ItemsBean> datas;


    public static GiftForGirlFragment newInstance(String url) {
        
        Bundle args = new Bundle();
        args.putString("url", url);
        GiftForGirlFragment fragment = new GiftForGirlFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int setLayout() {
        return R.layout.fragment_forgirl;
    }

    @Override
    protected void initView() {
        girlLv = byView(R.id.girl_lv);
    }

    @Override
    protected void initDatas() {

        Bundle bundle = getArguments();
        String string = bundle.getString("url");
//        Log.e("gao", "方法名:initDatas--- 类名GiftForGirlFragment"+string);
        giftForGirlAdapter = new GiftForGirlAdapter(context);
        VolleyInstance.getInstance().startRequest(string, this);
        girlLv.setAdapter(giftForGirlAdapter);
        girlLv.setInterface(this);
        girlLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, SelectionLvDetailActivity.class);
                intent.putExtra("ID", datas.get(position - 1).getId() + "");

                intent.putExtra("name", datas.get(position-1).getShort_title());
                intent.putExtra("like", datas.get(position - 1).getLikes_count() + "");
                startActivity(intent);
            }
        });
    }

    @Override
    public void success(String resultStr) {
        Gson gson = new Gson();
        GiftForGilrBean bean = gson.fromJson(resultStr, GiftForGilrBean.class);
        datas = bean.getData().getItems();
        giftForGirlAdapter.setDatas(datas);
    }

    @Override
    public void failure() {

    }

    @Override
    public void onReflash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                girlLv.reflshComplete();
            }
        }, 2000);
        giftForGirlAdapter.notifyDataSetChanged();
    }
}
