package com.gaowenyun.gift.ui.fragment.category;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.SingleBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.adapter.SingleLeftLvAdapter;
import com.gaowenyun.gift.ui.adapter.SingleRightLvAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.google.gson.Gson;


import java.util.List;

/**
 * 单品
 */
public class CategorySingleFragment extends AbsFragment {
    private ListView listViewLeft, listViewRight;
    private SingleLeftLvAdapter leftLvAdapter;
    private SingleRightLvAdapter rightLvAdapter;
    private int selectIndex = 0;
    List<SingleBean.DataBean.CategoriesBean> datas;

    public static CategorySingleFragment newInstance() {

        Bundle args = new Bundle();
        CategorySingleFragment fragment = new CategorySingleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_category_single;
    }

    @Override
    protected void initView() {
        listViewLeft = byView(R.id.category_single_lv_left);
        listViewRight = byView(R.id.category_single_lv_right);
    }

    @Override
    protected void initDatas() {
        leftLvAdapter = new SingleLeftLvAdapter(context);
        rightLvAdapter = new SingleRightLvAdapter(context);
        VolleyInstance.getInstance().startRequest(NetUrl.SINGLE, new IVolleyResult() {
            @Override
            public void success(String resultStr) {
                Gson gson = new Gson();
                SingleBean bean = gson.fromJson(resultStr, SingleBean.class);
                datas = bean.getData().getCategories();
                leftLvAdapter.setDatas(datas);
                rightLvAdapter.setDatas(datas);
            }

            @Override
            public void failure() {

            }
        });
        listViewLeft.setAdapter(leftLvAdapter);
        listViewRight.setAdapter(rightLvAdapter);
        listViewLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                //把下标传过去，然后刷新adapter
                leftLvAdapter.setIndex(position);
                leftLvAdapter.notifyDataSetChanged();
                //当点击某个item的时候让这个item自动滑动到listview的顶部第二个(下面item够多，如果点击的是最后一个就不能到达顶部了)

                    listViewLeft.smoothScrollToPositionFromTop(position - 1, 0);
                    rightLvAdapter.setIndex(position);
                    listViewRight.setSelection(position);

            }
        });
        listViewRight.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                leftLvAdapter.setIndex(firstVisibleItem);
                listViewLeft.smoothScrollToPositionFromTop(firstVisibleItem - 1, 0);
                leftLvAdapter.notifyDataSetChanged();
            }
        });

    }
}
