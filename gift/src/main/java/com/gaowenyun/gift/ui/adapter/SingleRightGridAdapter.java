package com.gaowenyun.gift.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.SingleBean;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 *
 * 分类-单品-右边ListView行布局中GridView适配器
 */
public class SingleRightGridAdapter extends BaseAdapter {
    private Context context;
    private List<SingleBean.DataBean.CategoriesBean.SubcategoriesBean> datas;

    public SingleRightGridAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<SingleBean.DataBean.CategoriesBean.SubcategoriesBean> datas) {
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
        GridViewHolder gridViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_view, parent, false);
            gridViewHolder = new GridViewHolder(convertView);
            convertView.setTag(gridViewHolder);
        }else {
            gridViewHolder = (GridViewHolder) convertView.getTag();
        }
        gridViewHolder.textView.setText(datas.get(position).getName());
        Glide.with(parent.getContext()).load(datas.get(position).getIcon_url()).diskCacheStrategy(DiskCacheStrategy.ALL).into(gridViewHolder.imageView);
//        Picasso.with(context).load(datas.get(position).getIcon_url()).into(gridViewHolder.imageView);
        return convertView;
    }
    class GridViewHolder {
        ImageView imageView;
        TextView textView;

        public GridViewHolder(View view) {
            imageView = (ImageView) view.findViewById(R.id.item_grid_iv);
            textView = (TextView) view.findViewById(R.id.item_grid_tv);
        }
    }
}
