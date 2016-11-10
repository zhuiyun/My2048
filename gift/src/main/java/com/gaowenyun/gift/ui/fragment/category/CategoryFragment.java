package com.gaowenyun.gift.ui.fragment.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.ui.activity.SearchActivity;
import com.gaowenyun.gift.ui.adapter.CategoryAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.qf.utillibary.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类界面
 */
public class CategoryFragment extends BaseFragment {
    private ViewPager categoryVp;
    private TabLayout categoryTb;
    private CategoryAdapter categoryAdapter;
    private RelativeLayout searchRl;




    protected void initDatas() {
        List<Fragment> datas = new ArrayList<>();
        datas.add(new CategoryStrategyFragment());
        datas.add(new CategorySingleFragment());
        categoryAdapter = new CategoryAdapter(getChildFragmentManager(), datas);
        categoryVp.setAdapter(categoryAdapter);
        categoryTb.setupWithViewPager(categoryVp);
        String[] titles = getResources().getStringArray(R.array.category_tab);
        for (int i = 0; i < 2; i++) {
            categoryTb.getTabAt(i).setText(titles[i]);
        }
        searchRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SearchActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_category;
    }

    @Override
    protected void init(View view) {
        super.init(view);
        categoryTb = (TabLayout) view.findViewById(R.id.category_tb);
        categoryVp = (ViewPager) view.findViewById(R.id.category_vp);
        searchRl = (RelativeLayout) view.findViewById(R.id.search_rl);
        initDatas();
    }
}
