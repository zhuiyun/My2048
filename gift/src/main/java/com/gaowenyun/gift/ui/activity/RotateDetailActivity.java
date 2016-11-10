package com.gaowenyun.gift.ui.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.RotateDetailBean;
import com.gaowenyun.gift.model.bean.SelecitonLvDetailBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.adapter.RotateDetailAdapter;
import com.google.gson.Gson;

import java.util.List;

/**
 *
 * 轮播图详情
 */
public class RotateDetailActivity extends AbsBaseActivity {
    private ListView rotateLv;
    private WebView rotateWb;
    private RotateDetailAdapter rotateDetailAdapter;
    private List<RotateDetailBean.DataBean.PostsBean> datas;
    private TextView favouriteTv;
    private TextView shareTv;
    private TextView commentTv;
    private LinearLayout linearLayout;


    @Override
    protected int setLayout() {
        return R.layout.activity_rotate_detail;
    }

    @Override
    protected void initViews() {
        rotateLv = byView(R.id.rotate_detail_lv);
        rotateWb = byView(R.id.rotate_detail_wb);
        favouriteTv = byView(R.id.rotate_favourite_tv);
        shareTv = byView(R.id.rotate_share_tv);
        commentTv = byView(R.id.rotate_comment_tv);
        linearLayout = byView(R.id.rotate_detail_ll);
    }

    @Override
    protected void initDatas() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        String url = NetUrl.ROTATE_BEFOREID + id + NetUrl.ROTATE_AFTERID;
        String wbUrl = NetUrl.LVDETAIL + id;
        rotateDetailAdapter = new RotateDetailAdapter(this);
        if (type.equals("collection")) {
            linearLayout.setVisibility(View.GONE);
            rotateWb.setVisibility(View.GONE);
            VolleyInstance.getInstance().startRequest(url, new IVolleyResult() {
                @Override
                public void success(String resultStr) {
                    Gson gson = new Gson();
                    RotateDetailBean rotateDetailBean = gson.fromJson(resultStr, RotateDetailBean.class);
                    datas = rotateDetailBean.getData().getPosts();
                    rotateDetailAdapter.setDatas(datas);
                    rotateLv.setAdapter(rotateDetailAdapter);
                }

                @Override
                public void failure() {

                }
            });
        }
        if (type.equals("post")) {
            rotateLv.setVisibility(View.GONE);
            VolleyInstance.getInstance().startRequest(wbUrl, new IVolleyResult() {
                @Override
                public void success(String resultStr) {
                    final Gson gson = new Gson();
                    SelecitonLvDetailBean detailBean = gson.fromJson(resultStr, SelecitonLvDetailBean.class);
                    rotateWb.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            return false;
                        }

                        @Override
                        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                            return super.shouldOverrideKeyEvent(view, event);
                        }
                    });
                    rotateWb.loadUrl(detailBean.getData().getUrl());
                    favouriteTv.setText(detailBean.getData().getLikes_count() + "");
                    shareTv.setText(detailBean.getData().getShares_count() + "");
                    commentTv.setText(detailBean.getData().getComments_count() + "");
                }

                @Override
                public void failure() {

                }
            });

        }
    }

}
