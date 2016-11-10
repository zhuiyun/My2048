package com.gaowenyun.gift.ui.fragment.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.ColomnBean;
import com.gaowenyun.gift.model.bean.StrategyBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.activity.ColumnActivity;
import com.gaowenyun.gift.ui.activity.TypeActivity;
import com.gaowenyun.gift.ui.adapter.ColomnRvAdapter;
import com.gaowenyun.gift.ui.adapter.StrategyRvAdapter;
import com.gaowenyun.gift.ui.fragment.AbsFragment;
import com.gaowenyun.gift.utils.SpanSizeLookup;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

/**
 * 攻略
 */
public class CategoryStrategyFragment extends AbsFragment {
    private RecyclerView colomnRecyclerView, categoryRv, styleRv, targetRv;
    private ColomnRvAdapter colomnRvAdapter;
    private StrategyRvAdapter categoryAdapter, styleAdapter, targetAdapter;
    private TextView strategyCategoryTv, strategyStyleTv, strategyTargetTv;
    List<StrategyBean.DataBean.ChannelGroupsBean.ChannelsBean> categoryBean;

    public static CategoryStrategyFragment newInstance() {
        Bundle args = new Bundle();
        CategoryStrategyFragment fragment = new CategoryStrategyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayout() {
        return R.layout.fragment_category_strategy;
    }

    @Override
    protected void initView() {
        colomnRecyclerView = byView(R.id.colomn_rv);
        categoryRv = byView(R.id.category_rv);
        styleRv = byView(R.id.style_rv);
        targetRv = byView(R.id.target_rv);
        strategyCategoryTv = byView(R.id.strategy_category_tv);
        strategyStyleTv = byView(R.id.strategy_style_tv);
        strategyTargetTv = byView(R.id.strategy_target_tv);
    }

    @Override
    protected void initDatas() {
        colomnData(); // 攻略上面横向Rv
        strategyData(); // 下面三个网格Rv
    }

    private void strategyData() {
        categoryAdapter = new StrategyRvAdapter(context);
        categoryRv.setAdapter(categoryAdapter);
        styleAdapter = new StrategyRvAdapter(context);
        styleRv.setAdapter(styleAdapter);
        targetAdapter = new StrategyRvAdapter(context);
        targetRv.setAdapter(targetAdapter);
        GridLayoutManager glm1 = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        GridLayoutManager glm2 = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        GridLayoutManager glm3 = new GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        categoryRv.setLayoutManager(glm1);
        styleRv.setLayoutManager(glm2);
        targetRv.setLayoutManager(glm3);
        VolleyInstance.getInstance().startRequest(NetUrl.STRATEGY, new IVolleyResult() {
            @Override
            public void success(String resultStr) {

                Gson gson = new Gson();
                StrategyBean bean = gson.fromJson(resultStr, StrategyBean.class);
                final List<StrategyBean.DataBean.ChannelGroupsBean> datas = bean.getData().getChannel_groups();
                if (datas.get(0).getName().equals("品类")) {
                    categoryBean = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        categoryBean.add(bean.getData().getChannel_groups().get(0).getChannels().get(i));
                    }

                    categoryAdapter.setDatas(categoryBean);
                    strategyCategoryTv.setText(datas.get(0).getName());
                    categoryAdapter.getOnitemClink(new StrategyRvAdapter.OnitemClink() {
                        @Override
                        public void itemClink(int position) {
                            Intent intent = new Intent(getActivity(), TypeActivity.class);
                            intent.putExtra("id", categoryBean.get(position).getId());
                            startActivity(intent);
                        }
                    });
                }
                if (datas.get(1).getName().equals("风格")) {
                    final List<StrategyBean.DataBean.ChannelGroupsBean.ChannelsBean> styleBean = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        styleBean.add(bean.getData().getChannel_groups().get(1).getChannels().get(i));
                    }
                    styleAdapter.setDatas(styleBean);
                    strategyStyleTv.setText(datas.get(1).getName());
                    styleAdapter.getOnitemClink(new StrategyRvAdapter.OnitemClink() {
                        @Override
                        public void itemClink(int position) {
                            Intent intent = new Intent(getActivity(), TypeActivity.class);
                            intent.putExtra("id", styleBean.get(position).getId());
                            startActivity(intent);
                        }
                    });
                }

                if (datas.get(2).getName().equals("对象")) {
                    final List<StrategyBean.DataBean.ChannelGroupsBean.ChannelsBean> targetBean = new ArrayList<>();
                    for (int i = 0; i < 6; i++) {
                        targetBean.add(bean.getData().getChannel_groups().get(2).getChannels().get(i));
                    }
                    targetAdapter.setDatas(targetBean);
                    strategyTargetTv.setText(datas.get(2).getName());
                    targetAdapter.getOnitemClink(new StrategyRvAdapter.OnitemClink() {
                        @Override
                        public void itemClink(int position) {
                            Intent intent = new Intent(getActivity(), TypeActivity.class);
                            intent.putExtra("id", targetBean.get(position).getId());
                            startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public void failure() {

            }
        });
    }

    private void colomnData() {
        colomnRvAdapter = new ColomnRvAdapter(context);
        colomnRecyclerView.setAdapter(colomnRvAdapter);
        GridLayoutManager manager = new GridLayoutManager(context, 3, GridLayoutManager.HORIZONTAL, false);
        manager.setSpanSizeLookup(new SpanSizeLookup(manager));
        colomnRecyclerView.setLayoutManager(manager);
        VolleyInstance.getInstance().startRequest(NetUrl.COLUMN, new IVolleyResult() {
            @Override
            public void success(String resultStr) {
                Gson gson = new Gson();
                ColomnBean bean = gson.fromJson(resultStr, ColomnBean.class);
                final List<ColomnBean.DataBean.ColumnsBean> datas = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    datas.add(bean.getData().getColumns().get(i));
                }
                colomnRvAdapter.setDatas(datas);
                colomnRvAdapter.setOnitemClink(new ColomnRvAdapter.OnitemClink() {
                    @Override
                    public void itemClink(int position) {
                        Intent intent = new Intent(getActivity(), ColumnActivity.class);
                        intent.putExtra("id", datas.get(position).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void failure() {

            }
        });
    }
}
