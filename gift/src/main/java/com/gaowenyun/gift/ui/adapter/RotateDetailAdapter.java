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
import com.gaowenyun.gift.model.bean.RotateDetailBean;
import com.gaowenyun.gift.utils.ScreenSizeUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *
 * 轮播图详情适配器
 */
public class RotateDetailAdapter extends BaseAdapter {
    private Context context;
    private List<RotateDetailBean.DataBean.PostsBean> datas;


    public RotateDetailAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<RotateDetailBean.DataBean.PostsBean> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_girl_lv, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RotateDetailBean.DataBean.PostsBean bean = (RotateDetailBean.DataBean.PostsBean) getItem(position);
        if (null != bean.getColumn() && null != bean.getColumn().getCategory()) {
            viewHolder.categoryTv.setText(bean.getColumn().getCategory());
            viewHolder.categoryTv.setVisibility(View.VISIBLE);
            viewHolder.columnTitleTv.setText(bean.getColumn().getTitle());
            viewHolder.columnTitleTv.setVisibility(View.VISIBLE);
            viewHolder.nickNameTv.setText(bean.getAuthor().getNickname());
            viewHolder.titleTv.setText(bean.getTitle());
            viewHolder.likesCountTv.setText(bean.getLikes_count() + "");
            Glide.with(context).load(bean.getAuthor().getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(viewHolder.authorAvatorImg);
//            Picasso.with(context).load(bean.getAuthor().getAvatar_url()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(viewHolder.authorAvatorImg);
            viewHolder.coverImg.setMinimumWidth(ScreenSizeUtil.getScreenWidth(context));
            Glide.with(context).load(bean.getCover_image_url()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(viewHolder.coverImg);
//            Picasso.with(context).load(bean.getCover_image_url()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(viewHolder.coverImg);
        }else {
            viewHolder.categoryTv.setVisibility(View.INVISIBLE);
            viewHolder.columnTitleTv.setVisibility(View.INVISIBLE);
            viewHolder.nickNameTv.setText(bean.getAuthor().getNickname());
            viewHolder.titleTv.setText(bean.getTitle());
            viewHolder.likesCountTv.setText(bean.getLikes_count() + "");
            Glide.with(context).load(bean.getAuthor().getAvatar_url()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(viewHolder.authorAvatorImg);
//            Picasso.with(context).load(bean.getAuthor().getAvatar_url()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(viewHolder.authorAvatorImg);
            viewHolder.coverImg.setMinimumWidth(ScreenSizeUtil.getScreenWidth(context));
            Glide.with(context).load(bean.getCover_image_url()).diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f).into(viewHolder.coverImg);
//            Picasso.with(context).load(bean.getCover_image_url()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).into(viewHolder.coverImg);
        }


        return convertView;
    }

    class ViewHolder {
        TextView categoryTv, columnTitleTv, nickNameTv, titleTv, likesCountTv;
        ImageView coverImg;
        CircleImageView authorAvatorImg;

        public ViewHolder(View view) {
            categoryTv = (TextView) view.findViewById(R.id.list_category_tv);
            columnTitleTv = (TextView) view.findViewById(R.id.list_column_title_tv);
            nickNameTv = (TextView) view.findViewById(R.id.list_nickname_tv);
            titleTv = (TextView) view.findViewById(R.id.list_title_tv);
            likesCountTv = (TextView) view.findViewById(R.id.list_likescount_tv);
            authorAvatorImg = (CircleImageView) view.findViewById(R.id.list_author_avatar_img);
            coverImg = (ImageView) view.findViewById(R.id.list_cover_img);
        }
    }
}
