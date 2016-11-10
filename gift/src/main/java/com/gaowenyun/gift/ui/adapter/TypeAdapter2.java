package com.gaowenyun.gift.ui.adapter;


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

import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.TypeMsgBean2;
import com.qf.utillibary.base.AbsBaseAdapter;

/**
 * Created by Administrator on 2016/11/5 0005.
 */
public class TypeAdapter2 extends AbsBaseAdapter<TypeMsgBean2.DataBean.ItemsBean> {
    public TypeAdapter2(Context context, int resId) {
        super(context, resId);
    }

    @Override
    public void bindDatas(ViewHodler viewHodler, TypeMsgBean2.DataBean.ItemsBean data) {
        viewHodler.setImageView(R.id.type_icon, data.getAuthor().getAvatar_url()).setTextView(R.id.title1, data.getAuthor().getNickname())
                .setTextView(R.id.title2, data.getAuthor().getIntroduction()).setImageView(R.id.type_iv, data.getCover_image_url())
                .setTextView(R.id.tv1, data.getTitle()).setTextView(R.id.tv2, data.getIntroduction()).setTextView(R.id.address, "栏目  " + data.getColumn().getTitle())
                .setTextView(R.id.love, data.getLikes_count() + "");
    }
}
