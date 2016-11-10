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


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.toolbox.Volley;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.TypeMsgBean2;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.adapter.TypeAdapter2;
import com.google.gson.Gson;
import com.qf.utillibary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class TypeActivity extends BaseActivity implements IVolleyResult {
    String url;
    TypeAdapter2 adapter2;
    ListView lv;
    List<TypeMsgBean2.DataBean.ItemsBean> data=new ArrayList<>();
    @Override
    public int getContentViewId() {
        return R.layout.column_item;
    }

    @Override
    protected void init() {
        super.init();
        int id=getIntent().getIntExtra("id",-1);
        url=String.format(NetUrl.TYPE2,id);
        lv=findViewByIds(R.id.column_lv);
        adapter2=new TypeAdapter2(this,R.layout.type_item);
        lv.setAdapter(adapter2);
        VolleyInstance.getInstance().startRequest(url,this);
    }

    @Override
    public void success(String resultStr) {
        Gson gson=new Gson();
        TypeMsgBean2 typeMsgBean2 = gson.fromJson(resultStr, TypeMsgBean2.class);
        data=typeMsgBean2.getData().getItems();
        adapter2.setDatas(data);
        adapter2.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent =new Intent(TypeActivity.this,WebActivity.class);
                intent.putExtra("url",data.get(i).getUrl());
                startActivity(intent);
            }
        });


    }

    @Override
    public void failure() {

    }
}
