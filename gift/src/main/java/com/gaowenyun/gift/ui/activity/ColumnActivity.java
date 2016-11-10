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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.Volley;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.TypeMsgBean;
import com.gaowenyun.gift.model.net.IVolleyResult;
import com.gaowenyun.gift.model.net.NetUrl;
import com.gaowenyun.gift.model.net.VolleyInstance;
import com.gaowenyun.gift.ui.adapter.TypeAdapter1;
import com.gaowenyun.gift.utils.MyTask;
import com.google.gson.Gson;
import com.qf.utillibary.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class ColumnActivity extends BaseActivity implements IVolleyResult {
    int id;
    String url;
    ImageView iv, back, share;
    TextView header_tv, header_love, ins;
    List<TypeMsgBean.DataBean.PostsBean> data = new ArrayList<>();
    TypeAdapter1 adapter1;
    ListView lv;

    @Override
    public int getContentViewId() {
        return R.layout.column_item;
    }

    @Override
    protected void init() {
        super.init();
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        url = String.format(NetUrl.TYPE, id);
        lv = findViewByIds(R.id.column_lv);
        VolleyInstance.getInstance().startRequest(url, this);
        View view = getLayoutInflater().inflate(R.layout.column_lv_header, null);
        lv.addHeaderView(view);
        ins = (TextView) view.findViewById(R.id.ins);
        header_tv = (TextView) view.findViewById(R.id.header_tv);
        header_love = (TextView) view.findViewById(R.id.header_love);
        back = (ImageView) view.findViewById(R.id.header_back);
        iv = (ImageView) view.findViewById(R.id.header_iv);
        adapter1 = new TypeAdapter1(this, R.layout.column_lv_item);
        lv.setAdapter(adapter1);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    public void success(String resultStr) {
        Gson gson = new Gson();
        final TypeMsgBean typeMsgBean = gson.fromJson(resultStr, TypeMsgBean.class);
        data = typeMsgBean.getData().getPosts();
        header_tv.setText(typeMsgBean.getData().getTitle());
        header_love.setText(typeMsgBean.getData().getLikes_count() + "人喜欢");
        ins.setText(typeMsgBean.getData().getDescription());
        MyTask task = new MyTask(iv);
        task.execute(typeMsgBean.getData().getBanner_image_url());
        adapter1.setDatas(data);
        adapter1.notifyDataSetChanged();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ColumnActivity.this, WebActivity.class);
                intent.putExtra("url", data.get(i).getUrl());
                startActivity(intent);
            }
        });
    }

    @Override
    public void failure() {

    }
}
