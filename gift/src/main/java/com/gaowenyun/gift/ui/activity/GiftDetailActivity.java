package com.gaowenyun.gift.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.LiteOrmBean;
import com.gaowenyun.gift.model.database.MyDataBaseOpernHelper2;
import com.gaowenyun.gift.model.db.LiteOrmInstance;
import com.gaowenyun.gift.model.db.SelectionInfo;
import com.gaowenyun.gift.ui.fragment.profile.ProfileFragment;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 *
 */
public class GiftDetailActivity extends AbsBaseActivity {
    private WebView giftDetailWb;
    private Button goToTaobao;
    private ImageView collectIv, backIv;

    private boolean isCollect = false;
    private ImageView shareIv;
    String imgUrl;
    Dao<SelectionInfo, Integer> userdao;
    MyDataBaseOpernHelper2 helper;

    @Override
    protected int setLayout() {
        return R.layout.activity_giftdetail;
    }

    @Override
    protected void initViews() {
        giftDetailWb = byView(R.id.giftdetail_wb);
        goToTaobao = byView(R.id.taobao_btn);
        collectIv = byView(R.id.gift_detail_collect);
        backIv = byView(R.id.giftdetail_back_iv);
        shareIv = byView(R.id.gift_detail_share);
        initDao(this);
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
    protected void initDatas() {
        giftDetailWb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
        final String name = intent.getStringExtra("name");
        final String price = intent.getStringExtra("price");
        final String description = intent.getStringExtra("description");
        final String url = intent.getStringExtra("url");
        final String taobaoUrl = intent.getStringExtra("taobaoUrl");
        giftDetailWb.loadUrl(url);
        goToTaobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("taobao", taobaoUrl);
                goTo(GiftDetailActivity.this, TaobaoShoppingActivity.class, bundle);
            }
        });

        collectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProfileFragment.DBName == null) {
                    Toast.makeText(GiftDetailActivity.this, "请先登录,才能进行收藏操作", Toast.LENGTH_SHORT).show();
                } else {
                    initDao(getApplicationContext());

                    if (isCollect == false) {
                        SelectionInfo info = new SelectionInfo();
                        info.setTitle(name);
                        info.setImg(imgUrl);
                        info.setWebUrl(url);
                        info.setAuthor(description);
                        collectIv.setImageResource(R.mipmap.ic_action_compact_favourite_selected);
//
                        try {
                            userdao.create(info);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(GiftDetailActivity.this, "喜欢成功", Toast.LENGTH_SHORT).show();
                        isCollect = true;
                    } else {
                        collectIv.setImageResource(R.mipmap.ic_action_compact_favourite_normal);
//
                        try {
                            List<SelectionInfo> userEntities = userdao.queryBuilder().where().like("img", imgUrl).query();
                            long id = userEntities.get(0).getId();
                            userdao.deleteById((int) id);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(GiftDetailActivity.this, "取消喜欢成功", Toast.LENGTH_SHORT).show();
                        isCollect = false;
                    }
                }
            }
        });
        if (ProfileFragment.DBName != null) {
            initDao(getApplicationContext());
            try {
                if (userdao.queryBuilder().where().like("img", imgUrl).query().size() != 0) {
                    collectIv.setImageResource(R.mipmap.ic_action_compact_favourite_selected);
                    isCollect = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        backIv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();
            }
        });
    }


    private void share() {
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
        oks.setUrl("http://www.baidu.com");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}
