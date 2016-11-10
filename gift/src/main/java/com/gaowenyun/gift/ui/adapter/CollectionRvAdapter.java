package com.gaowenyun.gift.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gaowenyun.gift.R;
import com.gaowenyun.gift.model.bean.LiteOrmBean;
import com.gaowenyun.gift.utils.GiftOnRvItemClick;


import java.util.List;

/**
 * 废弃
 * 收藏适配器
 */
public class CollectionRvAdapter extends RecyclerView.Adapter<CollectionRvAdapter.ViewHolder> {
    private Context context;
    private List<LiteOrmBean> datas;
    private GiftOnRvItemClick giftOnRvItemClick;

    public void setGiftOnRvItemClick(GiftOnRvItemClick giftOnRvItemClick) {
        this.giftOnRvItemClick = giftOnRvItemClick;
    }


    public CollectionRvAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<LiteOrmBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }



        @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_daily_rv, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final LiteOrmBean bean = datas.get(position);
        Glide.with(context).load(bean.getImgUrl()).thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.dailyIv);
        if (bean.getDescription() != "") {
            holder.dailyName.setText(bean.getName());
            holder.dailyDescription.setText(bean.getDescription());
            holder.dailyPrice.setText("￥ " + subZeroAndDot(bean.getPrice()));
        } else {
            holder.dailyDescription.setText(bean.getName());// 当dailyDescryption为"" 设置到dailyName的位置上显示
            holder.dailyName.setText("");
            holder.dailyPrice.setText("￥ " + subZeroAndDot(bean.getPrice()));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (giftOnRvItemClick != null) {
                    int p = holder.getLayoutPosition();
                    giftOnRvItemClick.onRvItemClickListener(p, bean);
                }
            }
        });
    }

    /**
     * 去掉多余的0 和 小数点
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是"."则去掉
        }
        return s;
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dailyIv;
        TextView dailyName, dailyDescription, dailyPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            dailyIv = (ImageView) itemView.findViewById(R.id.daily_item_iv);
            dailyDescription = (TextView) itemView.findViewById(R.id.daily_item_short_description);
            dailyName = (TextView) itemView.findViewById(R.id.daily_item_name);
            dailyPrice = (TextView) itemView.findViewById(R.id.daily_item_price);
        }
    }
}
