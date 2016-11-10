package com.gaowenyun.gift.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.database.MyDataBaseOpenHelper;
import com.gaowenyun.gift.model.db.UserInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;


public class LoginActivity extends AbsBaseActivity implements View.OnClickListener {
    private ImageView closeBtn, loginQQ;
    private Button loginBtn;
    private EditText userName, passWord;
    private SharedPreferences sp;
    private TextView sms;
    Dao<UserInfo, Integer> userdao;
    MyDataBaseOpenHelper helper;
    public static UserInfo user;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViews() {
        loginBtn = byView(R.id.login_btn);
        closeBtn = byView(R.id.login_close_btn);
        userName = byView(R.id.login_user_et);
        passWord = byView(R.id.login_password_et);
        loginQQ = byView(R.id.login_qq);
        sms=byView(R.id.sms);
        initDao(this);

    }
    private void initDao(Context context) {
        helper = MyDataBaseOpenHelper.getOpenHelper(context);

        try {
            userdao = helper.getDao(UserInfo.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initDatas() {
        closeBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        loginQQ.setOnClickListener(this);
        sms.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_close_btn:
                finish();
                break;
            case R.id.login_btn:
                login1();
                break;
            case R.id.login_qq:
                login();
                break;
            case R.id.sms:
                Intent intent =new Intent(LoginActivity.this,SmsActivity.class);
                startActivity(intent);
        }
    }

    private void login1() {
        String name=userName.getText().toString();
        String password=passWord.getText().toString();
        try {
            List<UserInfo> userEntities=userdao.queryBuilder().where().like("usernumber",name).or().like("password",password).query();
            if(userEntities!=null){
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                user=userEntities.get(0);
                setResult(99);
                finish();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void login() {
        // 获取第三方平台
        final Platform platform1 = ShareSDK.getPlatform(this, QQ.NAME);
       if(platform1.isAuthValid()){
           platform1.removeAccount(true);
       }
        // 授权
        platform1.authorize();
//        platform1.showUser(null);
        platform1.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                setResult(101);
                finish();
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(LoginActivity.this, "错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(LoginActivity.this, "取消", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
