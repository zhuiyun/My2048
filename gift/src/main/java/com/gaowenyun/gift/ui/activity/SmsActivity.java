package com.gaowenyun.gift.ui.activity;


//                            _ooOoo_  
//                           o8888888o  
//                           88" . "88  
//                           (| -_- |)  
//                            O\ = /O  
//                        ____/`---'\____  
//                      .   ' \\| |// `.  
//                       / \\||| : |||// \  
//                     / _||||| -:- |||||- \  
//                       | | \\\ - /// | |  
//                     | \_| ''\---/'' | |  
//                      \ .-\__ `-` ___/-. /  
//                   ___`. .' /--.--\ `. . __  
//                ."" '< `.___\_<|>_/___.' >'"".  
//               | | : `- \`.;`\ _ /`;.`/ - ` : | |  
//                 \ \ `-. \_ __\ /__ _/ .-` / /  
//         ======`-.____`-.___\_____/___.-`____.-'======  
//                            `=---='  
//  
//         .............................................  
//                  佛祖镇楼                  BUG辟易  
//          佛曰:  
//                  写字楼里写字间，写字间里程序员；  
//                  程序人员写程序，又拿程序换酒钱。  
//                  酒醒只在网上坐，酒醉还来网下眠；  
//                  酒醉酒醒日复日，网上网下年复年。  
//                  但愿老死电脑间，不愿鞠躬老板前；  
//                  奔驰宝马贵者趣，公交自行程序员。  
//                  别人笑我忒疯癫，我笑自己命太贱；  
//                  不见满街漂亮妹，哪个归得程序员？  


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.database.MyDataBaseOpenHelper;
import com.gaowenyun.gift.model.database.MyDataBaseOpernHelper2;
import com.gaowenyun.gift.model.db.SelectionInfo;
import com.gaowenyun.gift.model.db.UserInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;
import cn.bmob.sms.listener.VerifySMSCodeListener;

/**
 * Created by Administrator on 2016/11/3 0003.
 */
public class SmsActivity extends AbsBaseActivity {
   private EditText name;
    private EditText sms;
    private  EditText password;
    private TextView send;
    private Button reg;
    Dao<UserInfo, Integer> userdao;
    MyDataBaseOpenHelper helper;
    @Override
    protected int setLayout() {
        return R.layout.sms;
    }

    @Override
    protected void initViews() {
        name=byView(R.id.login_user_et);
        sms=byView(R.id.sms_code);
        password=byView(R.id.login_password_et);
        send=byView(R.id.send);
        reg=byView(R.id.login_btn);
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
    String number;
    @Override
    protected void initDatas() {
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              number=name.getText().toString();
                if(number.length()==11){
                    BmobSMS.requestSMSCode(getApplicationContext(), number, "login",new RequestSMSCodeListener() {

                        @Override
                        public void done(Integer smsId,BmobException ex) {
                            // TODO Auto-generated method stub
                            if(ex==null){//验证码发送成功
                                Log.i("bmob", "短信id："+smsId);//用于查询本次短信发送详情
                            }
                        }
                    });
                }else{
                    Toast.makeText(SmsActivity.this, "手机号码输入有误", Toast.LENGTH_SHORT).show();
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String code=sms.getText().toString();
                BmobSMS.verifySmsCode(getApplicationContext(),number, code, new VerifySMSCodeListener() {

                    @Override
                    public void done(BmobException ex) {
                        // TODO Auto-generated method stub
                        if(ex==null){//短信验证码已验证成功
                            if(password.getText().toString()!=null){
                                Toast.makeText(SmsActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }
                            UserInfo user=new UserInfo();
                            user.setNikeName(number);
                            user.setPassword(password.getText().toString());
                            user.setDbName(number+".db");
                            user.setUsernumeber(number);
                            try {
                                userdao.create(user);
                            } catch (SQLException e) {
                                e.printStackTrace();
                                Log.e("gao", "方法名:done--- 类名SmsActivity"+e.toString());
                            }

                            finish();
                        }else{
                            Log.i("bmob", "验证失败：code ="+ex.getErrorCode()+",msg = "+ex.getLocalizedMessage());
                        }
                    }
                });
            }
        });
    }
}
