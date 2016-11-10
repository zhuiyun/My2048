package com.gaowenyun.gift.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.LiteOrmBean;
import com.gaowenyun.gift.model.bean.SelecitonLvDetailBean;
import com.gaowenyun.gift.model.database.MyDataBaseOpernHelper2;
import com.gaowenyun.gift.model.db.LiteOrmInstance;
import com.gaowenyun.gift.model.db.SelectionInfo;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.fragment.profile.ProfileFragment;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 精选ListView行布局详情(后面复用)
 */
public class SelectionLvDetailActivity extends AbsBaseActivity implements View.OnClickListener {
    private WebView detailLvWeb;
    private ImageView backIv;
    private TextView favouriteTv;
    private TextView shareTv;
    private TextView commentTv;
    private ImageView selection;
    private boolean isCollect = false;
    String name;
    String price;
    String weburl;
    String description;
    String imgUrl;
    private ImageView share;
    SelecitonLvDetailBean detailBean;
    Dao<SelectionInfo, Integer> userdao;
    MyDataBaseOpernHelper2 helper;

    @Override
    protected int setLayout() {
        return R.layout.activity_selection_detail;
    }

    @Override
    protected void initViews() {
        detailLvWeb = byView(R.id.detail_lv_web);
        backIv = byView(R.id.back_iv);
        favouriteTv = byView(R.id.favourite_tv);
        shareTv = byView(R.id.share_tv);
        commentTv = byView(R.id.comment_tv);
        selection = byView(R.id.selection);
        share = byView(R.id.share);
        initDao(this);

    }

    @Override
    protected void initDatas() {

        backIv.setOnClickListener(this);
        detailLvWeb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });
        final Intent intent = getIntent();
        String id = intent.getStringExtra("ID");
        final String url = NetUrl.LVDETAIL + id;

        VolleyInstance.getInstance().startRequest(url, new IVolleyResult() {
            @Override
            public void success(String resultStr) {
                Gson gson = new Gson();
                detailBean = gson.fromJson(resultStr, SelecitonLvDetailBean.class);
                detailLvWeb.loadUrl(detailBean.getData().getUrl());
                name = detailBean.getData().getTitle();
                description = detailBean.getData().getIntroduction();
                price = null;
                weburl = detailBean.getData().getUrl();
                imgUrl = detailBean.getData().getCover_image_url();
                favouriteTv.setText(detailBean.getData().getLikes_count() + "");
                shareTv.setText(detailBean.getData().getShares_count() + "");
                commentTv.setText(detailBean.getData().getComments_count() + "");
                if (ProfileFragment.DBName != null) {
                    initDao(getApplicationContext());
                    try {
                        if (userdao.queryBuilder().where().like("webUrl", weburl).query().size() != 0) {
                            selection.setImageResource(R.mipmap.ic_action_compact_favourite_selected);
                            isCollect = true;
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure() {

            }
        });
//


        selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProfileFragment.DBName == null) {
                    Toast.makeText(SelectionLvDetailActivity.this, "请先登录,才能进行收藏操作", Toast.LENGTH_SHORT).show();
                } else {
                    initDao(getApplicationContext());
                    if (isCollect == false) {
                        SelectionInfo info = new SelectionInfo();
                        info.setTitle(name);
                        info.setImg(imgUrl);
                        info.setWebUrl(weburl);
                        info.setAuthor(description);

                        selection.setImageResource(R.mipmap.ic_action_compact_favourite_selected);
                        try {
                            userdao.create(info);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        favouriteTv.setText(detailBean.getData().getLikes_count() + 1 + "");
                        Toast.makeText(SelectionLvDetailActivity.this, "喜欢成功", Toast.LENGTH_SHORT).show();
                        isCollect = true;
                    } else {
                        selection.setImageResource(R.mipmap.ic_action_compact_favourite_normal);
//
                        try {
                            List<SelectionInfo> userEntities = userdao.queryBuilder().where().like("img", imgUrl).query();
                            long id = userEntities.get(0).getId();
                            userdao.deleteById((int) id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        favouriteTv.setText(detailBean.getData().getLikes_count() + "");
                        Toast.makeText(SelectionLvDetailActivity.this, "取消喜欢成功", Toast.LENGTH_SHORT).show();
                        isCollect = false;
                    }
                }
            }
        });

        share.setOnClickListener(this);
    }


    private void initDao(Context context) {
        if (ProfileFragment.DBName != null) {
            helper = MyDataBaseOpernHelper2.getOpenHelper(context, ProfileFragment.DBName);
            try {
                userdao = helper.getDao(SelectionInfo.class);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_iv:
                finish();
                break;

            case R.id.share: {
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle("礼物说");
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//        oks.setTitleUrl("http://sharesdk.cn");
                // text是分享文本，所有平台都需要这个字段
                oks.setText("这里有你想要的哦");
                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                oks.setImageUrl(imgUrl);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl(weburl);
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite("ShareSDK");
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
                oks.show(this);
            }
            break;

        }
    }
}
