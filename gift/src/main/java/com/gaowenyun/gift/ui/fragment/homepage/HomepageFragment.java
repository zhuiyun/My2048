package com.gaowenyun.gift.ui.fragment.homepage;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.ui.activity.SearchActivity;
import com.gaowenyun.gift.ui.adapter.HomepageAdapter;
import com.gaowenyun.gift.ui.adapter.HomepagePopAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.gaowenyun.gift.utils.HomepagePopRvItemClick;
import com.qf.utillibary.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 首页
 */
public class HomepageFragment extends BaseFragment implements View.OnClickListener {
    private ViewPager homepageVp;
    private TabLayout homepageTb;
    private HomepageAdapter homepageAdapter;
    private ImageView downIv, popIv;
    private LinearLayout homepageRootView;
    private RecyclerView popRv;
    private HomepagePopAdapter homepagePopAdapter;
    private String[] titles;
    private int selectIndex = 0;
    private TextView searchTv;
    private PopupWindow pw;


    protected void initDatas() {
        addFragments();
        homepageVp.setAdapter(homepageAdapter);
        homepageTb.setupWithViewPager(homepageVp);
        homepageTbSet(); // 设置TabLayout标签数据
        downIv.setOnClickListener(this);
        searchTv.setText(getResources().getString(R.string.search_text));
        searchTv.setOnClickListener(this);
    }

    private void addFragments() {
        List<Fragment> datas = new ArrayList<>();
        datas.add(SelectionFragment.newInstance());
        datas.add(GiftForGirlFragment.newInstance(NetUrl.GIVEGILRFRIEND));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.BOARD_SHOPPING));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.IDEA_FOR_LIFE));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.GIVE_BOYFRIEND));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.GIVE_PARENT));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.GIVE_COLLEGUE));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.GIVE_BABY));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.DESIGN_FEELS));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.ART_WIND));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.THE_EXIOC));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.FAN_SENCISE));
        datas.add(GiftForGirlFragment.newInstance(NetUrl.KAWAYI));
        homepageAdapter = new HomepageAdapter(getChildFragmentManager(), datas);
    }

    private void homepageTbSet() {
        titles = getResources().getStringArray(R.array.homepage_tab);
        for (int i = 0; i < 13; i++) {
            homepageTb.getTabAt(i).setText(titles[i]);
        }
        homepageTb.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homepage_downIv:
                showWindow(); // popwindow弹出方法
                break;
            case R.id.homepage_search_tv:
//                goTo(SearchActivity.class);
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
                break;
        }
    }


    //弹窗
    private void showWindow() {
        pw = new PopupWindow(getContext());
        pw.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        View v = LayoutInflater.from(getContext()).inflate(R.layout.homepage_popup, null);
        popRv = (RecyclerView) v.findViewById(R.id.pop_rv);
        popIv = (ImageView) v.findViewById(R.id.pop_iv);
        pw.setContentView(v);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setFocusable(true);
        pw.showAsDropDown(homepageRootView);
        homepagePopAdapter = new HomepagePopAdapter(getContext());
        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            datas.add(titles[i]);

        }
        datas.add(null);
        datas.add(null);
        datas.add(null);
        homepagePopAdapter.setDatas(datas);
        popRv.setAdapter(homepagePopAdapter);
        GridLayoutManager sgm = new GridLayoutManager(getContext(), 4);
        popRv.setLayoutManager(sgm);
        popIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        selectIndex = homepageVp.getCurrentItem();
        homepagePopAdapter.setIndex(selectIndex);
        homepagePopAdapter.setPopRvItemClick(new HomepagePopRvItemClick() {
            @Override
            public void OnPopRvItemClickListener(int position, String string) {
                homepageVp.setCurrentItem(position);
                pw.dismiss();
            }
        });

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_homepage;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        homepageVp = (ViewPager) view.findViewById(R.id.homepage_vp);
        homepageTb = (TabLayout) view.findViewById(R.id.homepage_tb);
        downIv = (ImageView) view.findViewById(R.id.homepage_downIv);
        homepageRootView = (LinearLayout) view.findViewById(R.id.homepage_rootview);
        searchTv = (TextView) view.findViewById(R.id.homepage_search_tv);
        initDatas();
    }
}
