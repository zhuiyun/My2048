package com.gaowenyun.gift.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.SingleBean;

import java.util.List;

/**
 *
 * 分类-单品页面-左边ListView适配器
 */
public class SingleLeftLvAdapter extends BaseAdapter {
    private Context context;
    private List<SingleBean.DataBean.CategoriesBean> datas;
    private int selectIndex;
    public SingleLeftLvAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<SingleBean.DataBean.CategoriesBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }



    @Override
    public int getCount() {
      return datas != null && datas.size() > 0 ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas != null && datas.size() > 0 ? datas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_single_left, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SingleBean.DataBean.CategoriesBean bean = (SingleBean.DataBean.CategoriesBean) getItem(position);
        viewHolder.textView.setText(bean.getName());
        /**
         * 选中状态判断
         */
        if (position == selectIndex) {
            viewHolder.textView.setBackgroundColor(Color.WHITE);
            viewHolder.textView.setTextColor(Color.parseColor("#ED2D44"));
            viewHolder.mView.setBackgroundColor(Color.parseColor("#ED2D44"));
        }else {
            viewHolder.textView.setTextColor(Color.BLACK);
            viewHolder.textView.setBackgroundColor(Color.parseColor("#ededed"));
            viewHolder.mView.setBackgroundColor(Color.WHITE);
        }
        /**
         * 最后一行选中后状态不变(同原版)
         */
        if (selectIndex == datas.size() - 1) {
            viewHolder.textView.setTextColor(Color.BLACK);
            viewHolder.textView.setBackgroundColor(Color.parseColor("#ededed"));
            viewHolder.mView.setBackgroundColor(Color.WHITE);
            if (position == selectIndex - 1) {
                viewHolder.textView.setBackgroundColor(Color.WHITE);
                viewHolder.textView.setTextColor(Color.parseColor("#ED2D44"));
                viewHolder.mView.setBackgroundColor(Color.parseColor("#ED2D44"));
            }
        }
        return convertView;
    }
    public void setIndex(int index) {
        selectIndex = index;
    }
    class ViewHolder {
        TextView textView;
        View mView;
        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.item_left_tv);
            mView = view.findViewById(R.id.item_left_v);
        }
    }
}
