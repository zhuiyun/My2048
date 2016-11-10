package com.gaowenyun.gift.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.ui.fragment.profile.ProfileFragment;
import com.gaowenyun.gift.utils.DataCleanManager;
import com.gaowenyun.gift.utils.UpdateInfo;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import io.rong.imkit.fragment.ConversationFragment;


/**
 *
 */
public class SettingActivity extends AbsBaseActivity {
    public static boolean Login=true;
    private RelativeLayout setRl;
    private TextView cleanTv;
    private TextView update;
    private TextView unLogin;


    @Override
    protected int setLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initViews() {
        setRl = byView(R.id.setting_rl);
        cleanTv = byView(R.id.clean_cache_tv);
        update = byView(R.id.update);
        unLogin = byView(R.id.unLogin);

    }


    @Override
    protected void initDatas() {
        try {
            cleanTv.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
        setRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    DataCleanManager.clearAllCache(SettingActivity.this);
                    DataCleanManager.getTotalCacheSize(SettingActivity.this);
                    cleanTv.setText(DataCleanManager.getTotalCacheSize(SettingActivity.this));
                    Toast.makeText(SettingActivity.this, "缓存清除成功", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
        unLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment.isLogin = false;
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                if (qq.isAuthValid()) {
                    qq.removeAccount(true);

                }
              ProfileFragment.isLogin=false;
                ProfileFragment.DBName=null;
                SharedPreferences sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("type");
                editor.remove("name");
                editor.remove("dbname");
                ProfileFragment.TYPE=-1;
                editor.commit();
                Toast.makeText(SettingActivity.this, "退出登录成功", Toast.LENGTH_SHORT).show();
//                Intent in=new Intent(SettingActivity.this,MainActivity.class);
//                startActivity(in);
            }
        });
    }


}
