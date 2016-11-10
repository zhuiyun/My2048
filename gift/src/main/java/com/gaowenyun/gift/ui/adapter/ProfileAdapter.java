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


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.db.SelectionInfo;


import java.util.List;

/**
 * Created by Administrator on 2016/11/1 0001.
 */
public class ProfileAdapter extends BaseAdapter {
    public ProfileAdapter(List<SelectionInfo> data) {
        this.data = data;
    }

    List<SelectionInfo> data;

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_list, viewGroup, false);
            holder = new ViewHolder();
            holder.iv = (ImageView) view.findViewById(R.id.profile_iv);
            holder.tv1 = (TextView) view.findViewById(R.id.profile_tv1);
            holder.delete = (TextView) view.findViewById(R.id.delete);
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteClink.clinkDelete(i);
                }
            });
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }
        Glide.with(viewGroup.getContext()).load(data.get(i).getImg()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(holder.iv);
        holder.tv1.setText(data.get(i).getTitle());
        return view;
    }

    class ViewHolder {
        TextView tv1, delete;
        ImageView iv;
    }

    public interface OnDeleteClink {
        public void clinkDelete(int position);
    }

    OnDeleteClink onDeleteClink;

    public void setOndeleteClink(OnDeleteClink onDeleteClink) {
        this.onDeleteClink = onDeleteClink;
    }
}
