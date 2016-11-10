package com.gaowenyun.gift.ui.fragment.gift;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.ui.adapter.GiftAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.qf.utillibary.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * -
 * 榜单页面
 */
public class GiftFragment extends BaseFragment{
    private TabLayout giftTabLayout;
    private ViewPager giftViewPager;
    private GiftAdapter giftAdapter;
    private String[] titles;

//    public static GiftFragment newInstance() {
//
//        Bundle args = new Bundle();
//
//        GiftFragment fragment = new GiftFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

//    @Override
//    protected int setLayout() {
//        return R.layout.fragment_gift;
//    }


    @Override
    protected void init(View view) {
        super.init(view);
        giftTabLayout = (TabLayout) view.findViewById(R.id.gift_tb);
        giftViewPager = (ViewPager) view.findViewById(R.id.gift_vp);
        initDatas();
    }

//    protected void initView() {
//        giftTabLayout = (R.id.gift_tb);
//        giftViewPager = byView(R.id.gift_vp);
//    }


    protected void initDatas() {
        List<Fragment> datas = new ArrayList<>();
        datas.add(DailyFragment.newInstance(NetUrl.URLDAILY));
        datas.add(TOPFragment.newInstance(NetUrl.TOP100));
        datas.add(TOPFragment.newInstance(NetUrl.ORIGINAL));
        datas.add(TOPFragment.newInstance(NetUrl.NEWSTAR));

        giftAdapter = new GiftAdapter(getChildFragmentManager(), datas);
        giftViewPager.setAdapter(giftAdapter);
        giftTabLayout.setupWithViewPager(giftViewPager);
        titles = getResources().getStringArray(R.array.gift_tab);
        for (int i = 0; i < 4; i++) {
            giftTabLayout.getTabAt(i).setText(titles[i]);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_gift;
    }
}
