package com.gaowenyun.gift.ui.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.database.MyDataBaseOpenHelper;
import com.gaowenyun.gift.model.database.MyDataBaseOpernHelper2;
import com.gaowenyun.gift.model.db.SelectionInfo;
import com.gaowenyun.gift.model.db.UserInfo;
import com.gaowenyun.gift.ui.activity.LoginActivity;
import com.gaowenyun.gift.ui.activity.SettingActivity;
import com.gaowenyun.gift.ui.adapter.ProfileAdapter;
import com.gaowenyun.gift.utils.QQListView;
import com.j256.ormlite.dao.Dao;
import com.qf.utillibary.base.BaseFragment;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.Conversation;


/**
 * 个人界面
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    public static boolean isLogin;

    private RadioGroup radioGroup;

    private ImageView boyIv, msgIv;
    private ImageView setIv;
    private TextView loginTv;
    private TextView sigtv;
    private TextView kefu;

    List<SelectionInfo> datas = new ArrayList<>();
    FrameLayout fl;
    ProfileAdapter adapter;
    QQListView lv;
    Dao<SelectionInfo, Integer> userdao;
    MyDataBaseOpernHelper2 helper;
    Dao<UserInfo, Integer> userdao1;
    MyDataBaseOpenHelper helper1;
    public static String DBName;

//

    @Override
    protected void init(View view) {
        super.init(view);
        radioGroup = (RadioGroup) view.findViewById(R.id.profile_rg);
        boyIv = (ImageView) view.findViewById(R.id.boy);
        msgIv = (ImageView) view.findViewById(R.id.message);
        setIv = (ImageView) view.findViewById(R.id.setting);
        loginTv = (TextView) view.findViewById(R.id.profile_login_tv);
        kefu = (TextView) view.findViewById(R.id.kefu);
        lv = (QQListView) view.findViewById(R.id.select_lv);
        adapter = new ProfileAdapter(datas);
        lv.setAdapter(adapter);

        initDao(getContext());

        adapter.setOndeleteClink(new ProfileAdapter.OnDeleteClink() {
            @Override
            public void clinkDelete(int position) {
                try {
                    userdao.deleteById((int) datas.get(position).getId());
                    lv.turnToNormal();
                    loadData();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        initDatas();
        final String Token = "wEWVk9AaYju3Fy4oQwKi0haIaFG9jFp0bHbCZu7/LkUYn9Ldw90ry1qy0C/tPwrBa5GlMp/qMrMyo4uBAmjWl3N29KYLIF5T";
        new Thread(new Runnable() {
            @Override
            public void run() {

                /**
                 * IMKit SDK调用第二步
                 *
                 * 建立与服务器的连接
                 *
                 */
                RongIM.connect(Token, new RongIMClient.ConnectCallback() {
                    @Override
                    public void onTokenIncorrect() {

                    }

                    @Override
                    public void onSuccess(String userId) {
                        Log.e("ckp", "类名==MainActivity" + ",方法名==onSuccess,userId=" + userId);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        Log.e("ckp", "类名==MainActivity" + ",方法名==onSuccess,errorCode=" + errorCode);
                    }
                });


            }
        }).start();
    }

    private void initDao(Context context) {
        helper1 = MyDataBaseOpenHelper.getOpenHelper(context);
        try {
            userdao1 = helper1.getDao(UserInfo.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

    }

    private void initDao1(Context context) {
        helper = MyDataBaseOpernHelper2.getOpenHelper(context, DBName);

        try {
            userdao = helper.getDao(SelectionInfo.class);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }


    }


    protected void initDatas() {

//        loadData();
        boyIv.setOnClickListener(this);
        msgIv.setOnClickListener(this);
        setIv.setOnClickListener(this);
        kefu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.boy:
                if (!isLogin) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, 100);
                } else {
                    Toast.makeText(getContext(), "已登录", Toast.LENGTH_SHORT).show();
                }

//                goTo(LoginActivity.class);
                break;
            case R.id.message:
                if (!isLogin) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.setting:
//
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.kefu:

                //首先需要构造使用客服者的用户信息
                CSCustomServiceInfo.Builder csBuilder = new CSCustomServiceInfo.Builder();
                CSCustomServiceInfo csInfo = csBuilder.nickName("融云").build();
                RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU147822657446599", "在线客服", csInfo);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isLogin) {
            datas.clear();
            adapter.notifyDataSetChanged();
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
            TYPE = sharedPreferences.getInt("type", -1);
            Log.e("gao", "方法名:onResume--- 类名ProfileFragment" + TYPE);
            if (TYPE == 1) {
                Platform platform = ShareSDK.getPlatform(QQ.NAME);
                String openId = platform.getDb().getUserId();
                String name = platform.getDb().getUserName();
                String icon = platform.getDb().getUserIcon();

                if (!icon.isEmpty()) {
                    Picasso.with(getContext()).load(icon).into(boyIv);
                    loginTv.setText(name);
//                fl.setVisibility(View.GONE);
                    initDao1(getContext());
                    loadData();
                    adapter.notifyDataSetChanged();
                    isLogin = true;
                } else {
                    Picasso.with(getContext()).load(R.mipmap.me_avatar_boy).into(boyIv);
                    loginTv.setText("请登录");
                }
            } else if (TYPE == 0) {
//                    sharedPreferences.getString("name",null);
                DBName = sharedPreferences.getString("dbname", null);
                loginTv.setText(sharedPreferences.getString("name", null) + "");
                initDao1(getContext());
                loadData();
                adapter.notifyDataSetChanged();
                isLogin = true;
            } else if (TYPE == -1) {
                Picasso.with(getContext()).load(R.mipmap.me_avatar_boy).into(boyIv);
                loginTv.setText("请登录");
                Log.e("gao", "请登录方法名:onResume--- 类名ProfileFragment");
                isLogin = false;
                datas.clear();
                adapter.notifyDataSetChanged();
            }

        }
    }

    UserInfo userInfo;
    public static int TYPE = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        //密码登录后返回
        if (resultCode == 99) {
            loginTv.setText(LoginActivity.user.getNikeName());
            isLogin = true;
            initDao1(getContext());
            loadData();
            TYPE = 0;
        } else if (resultCode == 101) {
            //三方登录后返回
            Platform platform = ShareSDK.getPlatform(QQ.NAME);
            String openId = platform.getDb().getUserId();
            String name = platform.getDb().getUserName();
            String icon = platform.getDb().getUserIcon();
            try {
                List<UserInfo> userEntities = userdao1.queryBuilder().where().like("nickname", name).or().like("usernumber", openId).query();
                if (userEntities.size() != 0) {
                    userInfo = userEntities.get(0);
                    userInfo.setUsernumeber(openId);
                    userInfo.setDbName(openId + ".db");
                    userInfo.setNikeName(name);
                    userdao1.update(userInfo);
                } else {
                    userInfo = new UserInfo();
                    userInfo.setUsernumeber(openId);
                    userInfo.setDbName(openId + ".db");
                    userInfo.setNikeName(name);
                    try {
                        userdao1.create(userInfo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Picasso.with(getContext()).load(R.mipmap.me_avatar_boy).into(boyIv);
            if (!icon.isEmpty()) {
                Picasso.with(getContext()).load(icon).into(boyIv);
                loginTv.setText(name);
            }
            LoginActivity.user = userInfo;
            isLogin = true;
            TYPE = 1;
            DBName = userInfo.getDbName();
            initDao1(getContext());
            loadData();
        }

    }

    public void loadData() {
        try {
            List<SelectionInfo> infos = userdao.queryForAll();
            datas.clear();
            datas.addAll(infos);
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        LiteOrmInstance liteOrm = LiteOrmInstance.getLiteOrmInstance();
//        datas = liteOrm.getQueryAll(LiteOrmBean.class);
//        Log.e("gao", "方法名:loadData--- 类名ProfileFragment"+ datas.size());

    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e("gao", "方法名:onHiddenChanged--- 类名ProfileFragment" + hidden);
        if (ProfileFragment.isLogin) {
            initDao1(getContext());
            loadData();
        } else {
            loginTv.setText("请登录");
            Picasso.with(getContext()).load(R.mipmap.me_avatar_boy).into(boyIv);
            datas.clear();
            adapter.notifyDataSetChanged();
        }
    }
}
