package com.gaowenyun.gift.utils;


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




import com.gaowenyun.gift.model.bean.GiftForGilrBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class JsonUtil {
    public static List<GiftForGilrBean.DataBean.ItemsBean> getMsg(String str){
        try {
            List<GiftForGilrBean.DataBean.ItemsBean> list=new ArrayList<>();
            JSONObject jo=new JSONObject(str);
            JSONObject jo1=jo.getJSONObject("data");
            JSONArray ja=jo1.getJSONArray("items");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo2=ja.getJSONObject(i);
                GiftForGilrBean.DataBean.ItemsBean bean=new GiftForGilrBean.DataBean.ItemsBean();
                JSONObject jo3=jo2.getJSONObject("author");
                GiftForGilrBean.DataBean.ItemsBean.AuthorBean bean1=new GiftForGilrBean.DataBean.ItemsBean.AuthorBean();
                bean1.setAvatar_url(jo3.getString("avatar_url"));
                bean1.setCreated_at(jo3.getInt("created_at"));
                bean1.setId(jo3.getInt("id"));
                bean1.setNickname(jo3.getString("nickname"));
                bean1.setIntroduction(jo3.getString("introduction"));
                bean.setAuthor(bean1);
                JSONObject jo4=jo2.getJSONObject("column");
                GiftForGilrBean.DataBean.ItemsBean.ColumnBean bean2=new GiftForGilrBean.DataBean.ItemsBean.ColumnBean();
                bean2.setBanner_image_url("jo2");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
