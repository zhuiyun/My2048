package com.gaowenyun.gift.ui.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.ui.fragment.category.CategoryFragment;
import com.gaowenyun.gift.ui.fragment.gift.GiftFragment;
import com.gaowenyun.gift.ui.fragment.homepage.HomepageFragment;
import com.gaowenyun.gift.ui.fragment.profile.ProfileFragment;
import com.qf.utillibary.base.BaseActivity;
import com.squareup.picasso.Picasso;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;


public class MainActivity extends BaseActivity {
    private RadioGroup radioGroup;
//    @Override
//    protected int setLayout() {
//        return R.layout.activity_main;
//    }

//    @Override
//    protected void initViews() {
//        radioGroup = byView(R.id.main_radio_group);
//    }

    @Override
    protected void init() {
        super.init();
        radioGroup = findViewByIds(R.id.main_radio_group);
        if (!ProfileFragment.isLogin) {
            SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

            int TYPE = sharedPreferences.getInt("type", -1);

            Log.e("gao", "方法名:onResume--- 类名ProfileFragment" + TYPE);
            if (TYPE == 1) {
                Platform platform = ShareSDK.getPlatform(QQ.NAME);
                String openId = platform.getDb().getUserId();
                String name = platform.getDb().getUserName();
                String icon = platform.getDb().getUserIcon();
                ProfileFragment.DBName = openId + ".db";


            } else if (TYPE == 0) {

//                    sharedPreferences.getString("name",null);
                ProfileFragment.DBName = sharedPreferences.getString("dbname", null);



            }

        }
        initDatas();
    }


    protected void initDatas() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch (checkedId) {
                    case R.id.homepage_rb:
                        fragmentManager(R.id.replace_view, new HomepageFragment(), "fragment1");

                        break;
                    case R.id.gift_rb:

                        fragmentManager(R.id.replace_view, new GiftFragment(), "fragment2");
                        break;
                    case R.id.category_rb:

                        fragmentManager(R.id.replace_view, new CategoryFragment(), "fragment3");
                        break;
                    case R.id.profile_rb:

                        fragmentManager(R.id.replace_view, new ProfileFragment(), "fragment4");
                        break;
                }
                transaction.commit();
            }
        });
        radioGroup.check(R.id.homepage_rb);
    }


    private static boolean isExit = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    /**
     * 物理返回键按俩下退出
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出" + getResources().getString(R.string.app_name),
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            if (ProfileFragment.isLogin) {
                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                try {
                    editor.putInt("type", ProfileFragment.TYPE);
                    editor.putString("name", LoginActivity.user.getNikeName());
                    editor.putString("dbname", LoginActivity.user.getDbName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                editor.commit();
            }
            finish();
            System.exit(0);
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }

}
